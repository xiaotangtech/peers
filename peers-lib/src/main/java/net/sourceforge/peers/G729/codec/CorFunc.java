package net.sourceforge.peers.g729.codec;

public class CorFunc {
    public CorFunc() {
    }

    public static void corr_xy2(float[] xn, float[] y1, float[] y2, float[] g_coeff) {
        float y2y2 = 0.01F;

        int i;
        for(i = 0; i < 40; ++i) {
            y2y2 += y2[i] * y2[i];
        }

        g_coeff[2] = y2y2;
        float xny2 = 0.01F;

        for(i = 0; i < 40; ++i) {
            xny2 += xn[i] * y2[i];
        }

        g_coeff[3] = -2.0F * xny2;
        float y1y2 = 0.01F;

        for(i = 0; i < 40; ++i) {
            y1y2 += y1[i] * y2[i];
        }

        g_coeff[4] = 2.0F * y1y2;
    }

    public static void cor_h_x(float[] h, float[] x, float[] d) {
        for(int i = 0; i < 40; ++i) {
            float s = 0.0F;

            for(int j = i; j < 40; ++j) {
                s += x[j] * h[j - i];
            }

            d[i] = s;
        }

    }
}
