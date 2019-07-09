package net.sourceforge.peers.G729.codec;

public class Pitch {
    public Pitch() {
    }

    public static int pitch_ol(float[] signal, int signals, int pit_min, int pit_max, int l_frame) {
        FloatPointer max1 = new FloatPointer();
        FloatPointer max2 = new FloatPointer();
        FloatPointer max3 = new FloatPointer();
        int p_max1 = lag_max(signal, signals, l_frame, pit_max, 80, max1);
        int p_max2 = lag_max(signal, signals, l_frame, 79, 40, max2);
        int p_max3 = lag_max(signal, signals, l_frame, 39, pit_min, max3);
        if (max1.value * 0.85F < max2.value) {
            max1.value = max2.value;
            p_max1 = p_max2;
        }

        if (max1.value * 0.85F < max3.value) {
            p_max1 = p_max3;
        }

        return p_max1;
    }

    public static int lag_max(float[] signal, int signals, int l_frame, int lagmax, int lagmin, FloatPointer cor_max) {
        int p = 0;
        int p_max = 0;
        float max = -1.0E38F;

        int i;
        float t0;
        for(i = lagmax; i >= lagmin; --i) {
            p = 0;
            int p1 = -i;
            t0 = 0.0F;

            for(int j = 0; j < l_frame; ++j) {
                t0 += signal[signals + p++] * signal[signals + p1++];
            }

            if (t0 >= max) {
                max = t0;
                p_max = i;
            }
        }

        t0 = 0.01F;
        p = -p_max;

        for(i = 0; i < l_frame; ++p) {
            t0 += signal[signals + p] * signal[signals + p];
            ++i;
        }

        t0 = inv_sqrt(t0);
        cor_max.value = max * t0;
        return p_max;
    }

    public static int pitch_fr3(float[] exc, int excs, float[] xn, int xns, float[] h, int hs, int l_subfr, int t0_min, int t0_max, int i_subfr, IntegerPointer pit_frac) {
        float[] corr_v = new float[18];
        int t_min = t0_min - 4;
        int t_max = t0_max + 4;
        int corr = -t_min;
        norm_corr(exc, excs, xn, xns, h, hs, l_subfr, t_min, t_max, corr_v, corr);
        float max = corr_v[corr + t0_min];
        int lag = t0_min;

        int i;
        for(i = t0_min + 1; i <= t0_max; ++i) {
            if (corr_v[corr + i] >= max) {
                max = corr_v[corr + i];
                lag = i;
            }
        }

        if (i_subfr == 0 && lag > 84) {
            pit_frac.value = 0;
            return lag;
        } else {
            max = interpol_3(corr_v, corr + lag, -2);
            int frac = -2;

            for(i = -1; i <= 2; ++i) {
                float corr_int = interpol_3(corr_v, corr + lag, i);
                if (corr_int > max) {
                    max = corr_int;
                    frac = i;
                }
            }

            if (frac == -2) {
                frac = 1;
                --lag;
            }

            if (frac == 2) {
                frac = -1;
                ++lag;
            }

            pit_frac.value = frac;
            return lag;
        }
    }

    public static void norm_corr(float[] exc, int excs, float[] xn, int xns, float[] h, int hs, int l_subfr, int t_min, int t_max, float[] corr_norm, int cs) {
        float[] excf = new float[40];
        int k = -t_min;
        Filter.convolve(exc, excs + k, h, 0, excf, 0, l_subfr);

        for(int i = t_min; i <= t_max; ++i) {
            float alp = 0.01F;

            int j;
            for(j = 0; j < l_subfr; ++j) {
                alp += excf[j] * excf[j];
            }

            float norm = inv_sqrt(alp);
            float s = 0.0F;

            for(j = 0; j < l_subfr; ++j) {
                s += xn[xns + j] * excf[j];
            }

            corr_norm[cs + i] = s * norm;
            if (i != t_max) {
                --k;

                for(j = l_subfr - 1; j > 0; --j) {
                    excf[j] = excf[j - 1] + exc[excs + k] * h[j];
                }

                excf[0] = exc[excs + k];
            }
        }

    }

    public static float g_pitch(float[] xn, int xns, float[] y1, int y1s, float[] g_coeff, int gs, int l_subfr) {
        float xy = 0.0F;

        int i;
        for(i = 0; i < l_subfr; ++i) {
            xy += xn[xns + i] * y1[y1s + i];
        }

        float yy = 0.01F;

        for(i = 0; i < l_subfr; ++i) {
            yy += y1[y1s + i] * y1[y1s + i];
        }

        g_coeff[gs + 0] = yy;
        g_coeff[gs + 1] = -2.0F * xy + 0.01F;
        float gain = xy / yy;
        if (gain < 0.0F) {
            gain = 0.0F;
        }

        if (gain > 1.2F) {
            gain = 1.2F;
        }

        return gain;
    }

    public static int enc_lag3(int T0, int T0_frac, IntegerPointer T0_min, IntegerPointer T0_max, int pit_min, int pit_max, int pit_flag) {
        int index;
        if (pit_flag == 0) {
            if (T0 <= 85) {
                index = T0 * 3 - 58 + T0_frac;
            } else {
                index = T0 + 112;
            }

            T0_min.value = T0 - 5;
            if (T0_min.value < pit_min) {
                T0_min.value = pit_min;
            }

            T0_max.value = T0_min.value + 9;
            if (T0_max.value > pit_max) {
                T0_max.value = pit_max;
                T0_min.value = T0_max.value - 9;
            }
        } else {
            index = T0 - T0_min.value;
            index = index * 3 + 2 + T0_frac;
        }

        return index;
    }

    public static float interpol_3(float[] x, int xs, int frac) {
        if (frac < 0) {
            frac += 3;
            --xs;
        }

        int x1 = 0;
        int x2 = 1;
        int c1 = frac;
        int c2 = 3 - frac;
        float s = 0.0F;

        for(int i = 0; i < 4; c2 += 3) {
            s += x[xs + x1--] * TabLD8k.inter_3[c1] + x[xs + x2++] * TabLD8k.inter_3[c2];
            ++i;
            c1 += 3;
        }

        return s;
    }

    static float inv_sqrt(float x) {
        return 1.0F / (float)Math.sqrt((double)x);
    }
}
