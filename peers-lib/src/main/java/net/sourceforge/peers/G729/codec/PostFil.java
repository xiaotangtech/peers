package net.sourceforge.peers.g729.codec;

public class PostFil {
    float[] apond2 = new float[20];
    float[] mem_stp = new float[10];
    float[] mem_zero = new float[10];
    float[] res2 = new float[192];
    int res2_ptr;
    int ptr_mem_stp;
    FloatPointer gain_prec = new FloatPointer(0.0F);

    public PostFil() {
    }

    public void init_post_filter() {
        int i;
        for(i = 0; i < 152; ++i) {
            this.res2[i] = 0.0F;
        }

        this.res2_ptr = 152;

        for(i = 0; i < 10; ++i) {
            this.mem_stp[i] = 0.0F;
        }

        this.ptr_mem_stp = 9;

        for(i = 11; i < 20; ++i) {
            this.apond2[i] = 0.0F;
        }

        for(i = 0; i < 10; ++i) {
            this.mem_zero[i] = 0.0F;
        }

        this.gain_prec.value = 1.0F;
    }

    public void post(int t0, float[] signal_ptr, int signals, float[] coeff, int coeffs, float[] sig_out, int outs, IntegerPointer vo) {
        float[] apond1 = new float[11];
        float[] sig_ltp = new float[41];
        FloatPointer parcor0 = new FloatPointer();
        LpcFunc.weight_az(coeff, coeffs, 0.7F, 10, apond1, 0);
        LpcFunc.weight_az(coeff, coeffs, 0.55F, 10, this.apond2, 0);
        Filter.residu(this.apond2, 0, signal_ptr, signals, this.res2, this.res2_ptr, 40);
        int sig_ltp_ptr = 1;
        this.pst_ltp(t0, this.res2, this.res2_ptr, sig_ltp, sig_ltp_ptr, vo);
        sig_ltp[0] = this.mem_stp[this.ptr_mem_stp];
        this.calc_st_filt(this.apond2, 0, apond1, 0, parcor0, sig_ltp, sig_ltp_ptr);
        Filter.syn_filt(apond1, 0, sig_ltp, sig_ltp_ptr, sig_ltp, sig_ltp_ptr, 40, this.mem_stp, 0, 1);
        filt_mu(sig_ltp, 0, sig_out, outs, parcor0.value);
        this.scale_st(signal_ptr, signals, sig_out, outs, this.gain_prec);
        Util.copy(this.res2, 40, this.res2, 0, 152);
    }

    public void pst_ltp(int t0, float[] ptr_sig_in, int ins, float[] ptr_sig_pst0, int psts, IntegerPointer vo) {
        IntegerPointer ltpdel = new IntegerPointer(0);
        IntegerPointer phase = new IntegerPointer(0);
        FloatPointer num_gltp = new FloatPointer(0.0F);
        FloatPointer den_gltp = new FloatPointer(0.0F);
        FloatPointer num2_gltp = new FloatPointer(0.0F);
        FloatPointer den2_gltp = new FloatPointer(0.0F);
        float[] y_up = new float[287];
        IntegerPointer off_yup = new IntegerPointer();
        search_del(t0, ptr_sig_in, ins, ltpdel, phase, num_gltp, den_gltp, y_up, off_yup);
        vo.value = ltpdel.value;
        if (num_gltp.value == 0.0F) {
            Util.copy(ptr_sig_in, ins, ptr_sig_pst0, psts, 40);
        } else {
            int ptr_y_up;
            float[] ptr_y_up_array;
            if (phase.value == 0) {
                ptr_y_up = ins - ltpdel.value;
                ptr_y_up_array = ptr_sig_in;
            } else {
                compute_ltp_l(ptr_sig_in, ins, ltpdel.value, phase.value, ptr_sig_pst0, psts, num2_gltp, den2_gltp);
                if (select_ltp(num_gltp.value, den_gltp.value, num2_gltp.value, den2_gltp.value) == 1) {
                    ptr_y_up = 0 + (phase.value - 1) * 41 + off_yup.value;
                    ptr_y_up_array = y_up;
                } else {
                    num_gltp = num2_gltp;
                    den_gltp = den2_gltp;
                    ptr_y_up = psts;
                    ptr_y_up_array = ptr_sig_pst0;
                }
            }

            float gain_plt;
            if (num_gltp.value > den_gltp.value) {
                gain_plt = 0.6666667F;
            } else {
                gain_plt = den_gltp.value / (den_gltp.value + 0.5F * num_gltp.value);
            }

            this.filt_plt(ptr_sig_in, ins, ptr_y_up_array, ptr_y_up, ptr_sig_pst0, psts, gain_plt);
        }

    }

    static void search_del(int t0, float[] ptr_sig_in, int ins, IntegerPointer ltpdel, IntegerPointer phase, FloatPointer num_gltp, FloatPointer den_gltp, float[] y_up, IntegerPointer off_yup) {
        float[] tab_den0 = new float[7];
        float[] tab_den1 = new float[7];
        float ener = 0.0F;

        int i;
        for(i = 0; i < 40; ++i) {
            ener += ptr_sig_in[i + ins] * ptr_sig_in[i + ins];
        }

        if (ener < 0.1F) {
            num_gltp.value = 0.0F;
            den_gltp.value = 1.0F;
            ltpdel.value = 0;
            phase.value = 0;
        } else {
            int lambda = t0 - 1;
            int ptr_sig_past = 0 - lambda;
            float num_int = -1.0E30F;
            int i_max = 0;

            int n;
            float num;
            for(i = 0; i < 3; ++i) {
                num = 0.0F;

                for(n = 0; n < 40; ++n) {
                    num += ptr_sig_in[ins + n] * ptr_sig_in[ins + ptr_sig_past + n];
                }

                if (num > num_int) {
                    i_max = i;
                    num_int = num;
                }

                --ptr_sig_past;
            }

            if (num_int <= 0.0F) {
                num_gltp.value = 0.0F;
                den_gltp.value = 1.0F;
                ltpdel.value = 0;
                phase.value = 0;
            } else {
                lambda += i_max;
                ptr_sig_past = 0 - lambda;
                float den_int = 0.0F;

                for(n = 0; n < 40; ++n) {
                    den_int += ptr_sig_in[ins + ptr_sig_past + n] * ptr_sig_in[ins + ptr_sig_past + n];
                }

                if (den_int < 0.1F) {
                    num_gltp.value = 0.0F;
                    den_gltp.value = 1.0F;
                    ltpdel.value = 0;
                    phase.value = 0;
                } else {
                    int ptr_y_up = 0;
                    float den_max = 0.0F;
                    int ptr_den0 = 0;
                    int ptr_den1 = 0;
                    int ptr_h = 0;
                    int ptr_sig_past0 = 1 - lambda;

                    float den0;
                    float den1;
                    int phi;
                    float temp0;
                    for(phi = 1; phi < 8; ++phi) {
                        ptr_sig_past = ptr_sig_past0;

                        for(n = 0; n <= 40; ++n) {
                            int ptr1 = ptr_sig_past++;
                            temp0 = 0.0F;

                            for(i = 0; i < 4; ++i) {
                                temp0 += TabLD8k.tab_hup_s[ptr_h + i] * ptr_sig_in[ins + ptr1 - i];
                            }

                            y_up[ptr_y_up + n] = temp0;
                        }

                        temp0 = 0.0F;

                        for(n = 1; n < 40; ++n) {
                            temp0 += y_up[ptr_y_up + n] * y_up[ptr_y_up + n];
                        }

                        den0 = temp0 + y_up[ptr_y_up + 0] * y_up[ptr_y_up + 0];
                        tab_den0[ptr_den0++] = den0;
                        den1 = temp0 + y_up[ptr_y_up + 40] * y_up[ptr_y_up + 40];
                        tab_den1[ptr_den1++] = den1;
                        if (Math.abs(y_up[ptr_y_up + 0]) > Math.abs(y_up[ptr_y_up + 40])) {
                            if (den0 > den_max) {
                                den_max = den0;
                            }
                        } else if (den1 > den_max) {
                            den_max = den1;
                        }

                        ptr_y_up += 41;
                        ptr_h += 4;
                    }

                    if (den_max < 0.1F) {
                        num_gltp.value = 0.0F;
                        den_gltp.value = 1.0F;
                        ltpdel.value = 0;
                        phase.value = 0;
                    } else {
                        float num_max = num_int;
                        den_max = den_int;
                        float numsq_max = num_int * num_int;
                        int phi_max = 0;
                        int ioff = 1;
                        ptr_den0 = 0;
                        ptr_den1 = 0;
                        ptr_y_up = 0;

                        float temp1;
                        for(phi = 1; phi < 8; ++phi) {
                            num = 0.0F;

                            for(n = 0; n < 40; ++n) {
                                num += ptr_sig_in[ins + n] * y_up[ptr_y_up + n];
                            }

                            if (num < 0.0F) {
                                num = 0.0F;
                            }

                            float numsq = num * num;
                            den0 = tab_den0[ptr_den0++];
                            temp0 = numsq * den_max;
                            temp1 = numsq_max * den0;
                            if (temp0 > temp1) {
                                num_max = num;
                                numsq_max = numsq;
                                den_max = den0;
                                ioff = 0;
                                phi_max = phi;
                            }

                            ++ptr_y_up;
                            num = 0.0F;

                            for(n = 0; n < 40; ++n) {
                                num += ptr_sig_in[ins + n] * y_up[ptr_y_up + n];
                            }

                            if (num < 0.0F) {
                                num = 0.0F;
                            }

                            numsq = num * num;
                            den1 = tab_den1[ptr_den1++];
                            temp0 = numsq * den_max;
                            temp1 = numsq_max * den1;
                            if (temp0 > temp1) {
                                num_max = num;
                                numsq_max = numsq;
                                den_max = den1;
                                ioff = 1;
                                phi_max = phi;
                            }

                            ptr_y_up += 40;
                        }

                        if (num_max != 0.0F && den_max > 0.1F) {
                            temp1 = den_max * ener * 0.5F;
                            if (numsq_max >= temp1) {
                                ltpdel.value = lambda + 1 - ioff;
                                off_yup.value = Integer.valueOf(ioff);
                                phase.value = phi_max;
                                num_gltp.value = num_max;
                                den_gltp.value = den_max;
                            } else {
                                num_gltp.value = 0.0F;
                                den_gltp.value = 1.0F;
                                ltpdel.value = 0;
                                phase.value = 0;
                            }

                        } else {
                            num_gltp.value = 0.0F;
                            den_gltp.value = 1.0F;
                            ltpdel.value = 0;
                            phase.value = 0;
                        }
                    }
                }
            }
        }
    }

    void filt_plt(float[] s_in, int ins, float[] s_ltp, int ltps, float[] s_out, int outs, float gain_plt) {
        float gain_plt_1 = 1.0F - gain_plt;

        for(int n = 0; n < 40; ++n) {
            float temp = gain_plt * s_in[n + ins];
            temp += gain_plt_1 * s_ltp[n + ltps];
            s_out[n + outs] = temp;
        }

    }

    static void compute_ltp_l(float[] s_in, int ins, int ltpdel, int phase, float[] y_up, int ys, FloatPointer num, FloatPointer den) {
        int ptr_h = (phase - 1) * 16;
        int ptr2 = 0 - ltpdel + 8;

        int n;
        for(n = 0; n < 40; ++n) {
            float temp = 0.0F;

            for(int i = 0; i < 16; ++i) {
                temp += TabLD8k.tab_hup_l[ptr_h + i] * s_in[ins + ptr2--];
            }

            y_up[ys + n] = temp;
            ptr2 += 17;
        }

        num.value = 0.0F;

        for(n = 0; n < 40; ++n) {
            num.value = num.value + y_up[ys + n] * s_in[ins + n];
        }

        if (num.value < 0.0F) {
            num.value = 0.0F;
        }

        den.value = 0.0F;

        for(n = 0; n < 40; ++n) {
            den.value = den.value + y_up[ys + n] * y_up[ys + n];
        }

    }

    static int select_ltp(float num1, float den1, float num2, float den2) {
        if (den2 == 0.0F) {
            return 1;
        } else {
            return num2 * num2 * den1 > num1 * num1 * den2 ? 2 : 1;
        }
    }

    void calc_st_filt(float[] apond2, int apond2s, float[] apond1, int apond1s, FloatPointer parcor0, float[] sig_ltp_ptr, int sigs) {
        float[] h = new float[20];
        Filter.syn_filt(apond1, apond1s, apond2, apond2s, h, 0, 20, this.mem_zero, 0, 0);
        calc_rc0_h(h, 0, parcor0);
        float g0 = 0.0F;

        int i;
        for(i = 0; i < 20; ++i) {
            g0 += Math.abs(h[i]);
        }

        if (g0 > 1.0F) {
            float temp = 1.0F / g0;

            for(i = 0; i < 40; ++i) {
                sig_ltp_ptr[i + sigs] *= temp;
            }
        }

    }

    static void calc_rc0_h(float[] h, int hs, FloatPointer rc0) {
        float temp = 0.0F;

        int i;
        for(i = 0; i < 20; ++i) {
            temp += h[hs + i] * h[hs + i];
        }

        float acf0 = temp;
        temp = 0.0F;
        int ptrs = 0;

        for(i = 0; i < 19; ++i) {
            float temp2 = h[hs + ptrs++];
            temp += temp2 * h[hs + ptrs];
        }

        if (acf0 == 0.0F) {
            rc0.value = 0.0F;
        } else if (acf0 < Math.abs(temp)) {
            rc0.value = 0.0F;
        } else {
            rc0.value = -temp / acf0;
        }
    }

    static void filt_mu(float[] sig_in, int ins, float[] sig_out, int outs, float parcor0) {
        float mu;
        if (parcor0 > 0.0F) {
            mu = parcor0 * 0.2F;
        } else {
            mu = parcor0 * 0.9F;
        }

        float ga = 1.0F / (1.0F - Math.abs(mu));
        int ptrs = ins;

        for(int n = 0; n < 40; ++n) {
            float temp = mu * sig_in[ptrs++];
            temp += sig_in[ptrs];
            sig_out[outs + n] = ga * temp;
        }

    }

    void scale_st(float[] sig_in, int ins, float[] sig_out, int outs, FloatPointer gain_prec) {
        float gain_in = 0.0F;

        int i;
        for(i = 0; i < 40; ++i) {
            gain_in += Math.abs(sig_in[ins + i]);
        }

        float g0;
        if (gain_in == 0.0F) {
            g0 = 0.0F;
        } else {
            float gain_out = 0.0F;

            for(i = 0; i < 40; ++i) {
                gain_out += Math.abs(sig_out[outs + i]);
            }

            if (gain_out == 0.0F) {
                gain_prec.value = 0.0F;
                return;
            }

            g0 = gain_in / gain_out;
            g0 *= 0.012499988F;
        }

        float gain = gain_prec.value;

        for(i = 0; i < 40; ++i) {
            gain *= 0.9875F;
            gain += g0;
            sig_out[outs + i] *= gain;
        }

        gain_prec.value = gain;
    }
}
