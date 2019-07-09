package net.sourceforge.peers.G729.codec;

public class DecLD8K {
    float[] old_exc_array = new float[234];
    int exc;
    float[] lsp_old = new float[]{0.9595F, 0.8413F, 0.6549F, 0.4154F, 0.1423F, -0.1423F, -0.4154F, -0.6549F, -0.8413F, -0.9595F};
    float[] mem_syn = new float[10];
    float sharp;
    int old_t0;
    FloatPointer gain_code = new FloatPointer();
    FloatPointer gain_pitch = new FloatPointer();
    LspDec lspDec = new LspDec();
    DecGain decGain = new DecGain();

    public DecLD8K() {
    }

    public void init_decod_ld8k() {
        this.exc = 154;
        Util.set_zero(this.old_exc_array, 154);
        Util.set_zero(this.mem_syn, 10);
        this.sharp = 0.2F;
        this.old_t0 = 60;
        this.gain_code.value = 0.0F;
        this.gain_pitch.value = 0.0F;
        this.lspDec.lsp_decw_reset();
    }

    public void decod_ld8k(int[] parm, int parms, int voicing, float[] synth, int ss, float[] A_t, IntegerPointer t0_first) {
        float[] lsp_new = new float[10];
        float[] code = new float[40];
        IntegerPointer t0 = new IntegerPointer();
        IntegerPointer t0_frac = new IntegerPointer();
        int bfi = parm[parms++];
        this.lspDec.d_lsp(parm, parms, lsp_new, bfi);
        parms += 2;
        LpcFunc.int_qlpc(this.lsp_old, lsp_new, A_t);
        Util.copy(lsp_new, this.lsp_old, 10);
        int Az = 0;

        for(int i_subfr = 0; i_subfr < 80; i_subfr += 40) {
            int index = parm[parms++];
            int i;
            if (i_subfr == 0) {
                i = parm[parms++];
                int bad_pitch = bfi + i;
                if (bad_pitch == 0) {
                    DecLag.dec_lag3(index, 20, 143, i_subfr, t0, t0_frac);
                    this.old_t0 = t0.value;
                } else {
                    t0.value = this.old_t0;
                    t0_frac.value = 0;
                    ++this.old_t0;
                    if (this.old_t0 > 143) {
                        this.old_t0 = 143;
                    }
                }

                t0_first.value = t0.value;
            } else if (bfi == 0) {
                DecLag.dec_lag3(index, 20, 143, i_subfr, t0, t0_frac);
                this.old_t0 = t0.value;
            } else {
                t0.value = this.old_t0;
                t0_frac.value = 0;
                ++this.old_t0;
                if (this.old_t0 > 143) {
                    this.old_t0 = 143;
                }
            }

            PredLt.pred_lt_3(this.old_exc_array, this.exc + i_subfr, t0.value, t0_frac.value, 40);
            if (bfi != 0) {
                parm[parms + 0] = Util.random_g729() & 8191;
                parm[parms + 1] = Util.random_g729() & 15;
            }

            DecAcelp.decod_ACELP(parm[parms + 1], parm[parms + 0], code);
            parms += 2;

            for(i = t0.value; i < 40; ++i) {
                code[i] += this.sharp * code[i - t0.value];
            }

            index = parm[parms++];
            this.decGain.dec_gain(index, code, 40, bfi, this.gain_pitch, this.gain_code);
            this.sharp = this.gain_pitch.value;
            if (this.sharp > 0.7945F) {
                this.sharp = 0.7945F;
            }

            if (this.sharp < 0.2F) {
                this.sharp = 0.2F;
            }

            if (bfi != 0) {
                if (voicing == 0) {
                    for(i = 0; i < 40; ++i) {
                        this.old_exc_array[this.exc + i + i_subfr] = this.gain_code.value * code[i];
                    }
                } else {
                    for(i = 0; i < 40; ++i) {
                        this.old_exc_array[this.exc + i + i_subfr] = this.gain_pitch.value * this.old_exc_array[this.exc + i + i_subfr];
                    }
                }
            } else {
                for(i = 0; i < 40; ++i) {
                    this.old_exc_array[this.exc + i + i_subfr] = this.gain_pitch.value * this.old_exc_array[this.exc + i + i_subfr] + this.gain_code.value * code[i];
                }
            }

            Filter.syn_filt(A_t, Az, this.old_exc_array, this.exc + i_subfr, synth, ss + i_subfr, 40, this.mem_syn, 0, 1);
            Az += 11;
        }

        Util.copy(this.old_exc_array, 80, this.old_exc_array, 0, 154);
    }
}
