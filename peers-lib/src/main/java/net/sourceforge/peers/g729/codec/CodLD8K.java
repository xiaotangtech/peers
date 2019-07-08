package net.sourceforge.peers.g729.codec;

public class CodLD8K {
    float[] old_speech_array = new float[240];
    int old_speech;
    int speech;
    int p_window;
    int new_speech;
    float[] old_wsp_array = new float[223];
    int old_wsp;
    int wsp;
    float[] old_exc_array = new float[234];
    int old_exc;
    int exc;
    float[] ai_zero_array = new float[51];
    int ai_zero;
    int zero;
    float[] lsp_old = new float[]{0.9595F, 0.8413F, 0.6549F, 0.4154F, 0.1423F, -0.1423F, -0.4154F, -0.6549F, -0.8413F, -0.9595F};
    float[] lsp_old_q = new float[10];
    float[] mem_syn = new float[10];
    float[] mem_w0 = new float[10];
    float[] mem_w = new float[10];
    float[] mem_err_array = new float[50];
    int mem_err;
    int error;
    float sharp;
    Lpc lpc = new Lpc();
    QuaLsp quaLsp = new QuaLsp();
    Pwf pwf = new Pwf();
    Taming tamingFunc = new Taming();
    CelpCo acelp = new CelpCo();
    QuaGain quaGain = new QuaGain();

    public CodLD8K() {
    }

    public void init_coder_ld8k() {
        this.new_speech = this.old_speech + 240 - 80;
        this.speech = this.new_speech - 40;
        this.p_window = this.old_speech + 240 - 240;
        this.wsp = this.old_wsp + 143;
        this.exc = this.old_exc + 143 + 11;
        this.zero = this.ai_zero + 11;
        this.error = this.mem_err + 10;
        this.sharp = 0.2F;
        System.arraycopy(this.lsp_old, 0, this.lsp_old_q, 0, 10);
        this.quaLsp.lsp_encw_reset();
        this.tamingFunc.init_exc_err();
    }

    public void loadSpeech(float[] newSpeech) {
        int i = 239;

        for(int q = 79; q >= 0; --q) {
            this.old_speech_array[i--] = newSpeech[q];
        }

    }

    public void coder_ld8k(int[] ana_array, int ana) {
        float[] r = new float[11];
        float[] A_t = new float[22];
        float[] Aq_t = new float[22];
        float[] Ap1 = new float[11];
        float[] Ap2 = new float[11];
        float[] lsp_new = new float[10];
        float[] lsp_new_q = new float[10];
        float[] lsf_int = new float[10];
        float[] lsf_new = new float[10];
        float[] rc = new float[10];
        float[] gamma1 = new float[2];
        float[] gamma2 = new float[2];
        float[] synth = new float[80];
        float[] h1 = new float[40];
        float[] xn = new float[40];
        float[] xn2 = new float[40];
        float[] code = new float[40];
        float[] y1 = new float[40];
        float[] y2 = new float[40];
        float[] g_coeff = new float[5];
        IntegerPointer t0_frac = new IntegerPointer();
        IntegerPointer t0_min = new IntegerPointer();
        IntegerPointer t0_max = new IntegerPointer();
        float gain_code = 0.0F;
        this.lpc.autocorr(ArrayUtils.subArray(this.old_speech_array, this.p_window), 10, r);
        this.lpc.lag_window(10, r);
        float[] tmp = ArrayUtils.subArray(A_t, 11);
        this.lpc.levinson(r, tmp, rc);
        ArrayUtils.replace(A_t, 11, tmp);
        this.lpc.az_lsp(tmp, lsp_new, this.lsp_old);
        ArrayUtils.replace(A_t, 11, tmp);
        this.quaLsp.qua_lsp(lsp_new, lsp_new_q, ana_array);
        ana += 2;
        LpcFunc.int_lpc(this.lsp_old, lsp_new, lsf_int, lsf_new, A_t);
        LpcFunc.int_qlpc(this.lsp_old_q, lsp_new_q, Aq_t);

        int i;
        for(i = 0; i < 10; ++i) {
            this.lsp_old[i] = lsp_new[i];
            this.lsp_old_q[i] = lsp_new_q[i];
        }

        this.pwf.perc_var(gamma1, gamma2, lsf_int, lsf_new, rc);
        LpcFunc.weight_az(A_t, 0, gamma1[0], 10, Ap1, 0);
        LpcFunc.weight_az(A_t, 0, gamma2[0], 10, Ap2, 0);
        Filter.residu(Ap1, 0, this.old_speech_array, this.speech, this.old_wsp_array, this.wsp, 40);
        Filter.syn_filt(Ap2, 0, this.old_wsp_array, this.wsp, this.old_wsp_array, this.wsp, 40, this.mem_w, 0, 1);
        LpcFunc.weight_az(A_t, 11, gamma1[1], 10, Ap1, 0);
        LpcFunc.weight_az(A_t, 11, gamma2[1], 10, Ap2, 0);
        Filter.residu(Ap1, 0, this.old_speech_array, this.speech + 40, this.old_wsp_array, this.wsp + 40, 40);
        Filter.syn_filt(Ap2, 0, this.old_wsp_array, this.wsp + 40, this.old_wsp_array, this.wsp + 40, 40, this.mem_w, 0, 1);
        int T_op = Pitch.pitch_ol(this.old_wsp_array, this.wsp, 20, 143, 80);
        t0_min.value = T_op - 3;
        if (t0_min.value < 20) {
            t0_min.value = 20;
        }

        t0_max.value = t0_min.value + 6;
        if (t0_max.value > 143) {
            t0_max.value = 143;
            t0_min.value = t0_max.value - 6;
        }

        int A = 0;
        int Aq = 0;
        int i_gamma = 0;

        for(int i_subfr = 0; i_subfr < 80; i_subfr += 40) {
            LpcFunc.weight_az(A_t, A, gamma1[i_gamma], 10, Ap1, 0);
            LpcFunc.weight_az(A_t, A, gamma2[i_gamma], 10, Ap2, 0);
            ++i_gamma;

            for(i = 0; i <= 10; ++i) {
                this.ai_zero_array[this.ai_zero + i] = Ap1[i];
            }

            Filter.syn_filt(Aq_t, Aq, this.ai_zero_array, this.ai_zero, h1, 0, 40, this.ai_zero_array, this.zero, 0);
            Filter.syn_filt(Ap2, 0, h1, 0, h1, 0, 40, this.ai_zero_array, this.zero, 0);
            Filter.residu(Aq_t, Aq, this.old_speech_array, this.speech + i_subfr, this.old_exc_array, this.exc + i_subfr, 40);
            Filter.syn_filt(Aq_t, Aq, this.old_exc_array, this.exc + i_subfr, this.mem_err_array, this.error, 40, this.mem_err_array, this.mem_err, 0);
            Filter.residu(Ap1, 0, this.mem_err_array, this.error, xn, 0, 40);
            Filter.syn_filt(Ap2, 0, xn, 0, xn, 0, 40, this.mem_w0, 0, 0);
            int t0 = Pitch.pitch_fr3(this.old_exc_array, this.exc + i_subfr, xn, 0, h1, 0, 40, t0_min.value, t0_max.value, i_subfr, t0_frac);
            int index = Pitch.enc_lag3(t0, t0_frac.value, t0_min, t0_max, 20, 143, i_subfr);
            ana_array[ana++] = index;
            if (i_subfr == 0) {
                ana_array[ana++] = PParity.parity_pitch(index);
            }

            PredLt.pred_lt_3(this.old_exc_array, this.exc + i_subfr, t0, t0_frac.value, 40);
            Filter.convolve(this.old_exc_array, this.exc + i_subfr, h1, 0, y1, 0, 40);
            float gain_pit = Pitch.g_pitch(xn, 0, y1, 0, g_coeff, 0, 40);
            int taming = this.tamingFunc.test_err(t0, t0_frac.value);
            if (taming == 1 && gain_pit > 0.95F) {
                gain_pit = 0.95F;
            }

            for(i = 0; i < 40; ++i) {
                xn2[i] = xn[i] - y1[i] * gain_pit;
            }

            IntegerPointer tmpi = new IntegerPointer(i);
            index = this.acelp.ACELP_codebook(xn2, h1, t0, this.sharp, i_subfr, code, y2, tmpi);
            i = tmpi.value;
            ana_array[ana++] = index;
            ana_array[ana++] = i;
            CorFunc.corr_xy2(xn, y1, y2, g_coeff);
            FloatPointer tmpgain_pit = new FloatPointer(gain_pit);
            FloatPointer tmpgain_code = new FloatPointer(gain_code);
            ana_array[ana++] = this.quaGain.qua_gain(code, g_coeff, 40, tmpgain_pit, tmpgain_code, taming);
            gain_pit = tmpgain_pit.value;
            gain_code = tmpgain_code.value;
            this.sharp = gain_pit;
            if (this.sharp > 0.7945F) {
                this.sharp = 0.7945F;
            }

            if (this.sharp < 0.2F) {
                this.sharp = 0.2F;
            }

            for(i = 0; i < 40; ++i) {
                this.old_exc_array[this.exc + i + i_subfr] = gain_pit * this.old_exc_array[this.exc + i + i_subfr] + gain_code * code[i];
            }

            this.tamingFunc.update_exc_err(gain_pit, t0);
            Filter.syn_filt(Aq_t, Aq, this.old_exc_array, this.exc + i_subfr, synth, i_subfr, 40, this.mem_syn, 0, 1);
            i = 30;

            for(int j = 0; i < 40; ++j) {
                this.mem_err_array[this.mem_err + j] = this.old_speech_array[this.speech + i_subfr + i] - synth[i_subfr + i];
                this.mem_w0[j] = xn[i] - gain_pit * y1[i] - gain_code * y2[i];
                ++i;
            }

            A += 11;
            Aq += 11;
        }

        Util.copy(this.old_speech_array, this.old_speech + 80, this.old_speech_array, this.old_speech, 160);
        Util.copy(this.old_wsp_array, this.old_wsp + 80, this.old_wsp_array, this.old_wsp, 143);
        Util.copy(this.old_exc_array, this.old_exc + 80, this.old_exc_array, this.old_exc, 154);
    }
}
