package net.sourceforge.peers.G729.codec;

public class Filter {
    public Filter() {
    }

    public static void convolve(float[] x, int xs, float[] h, int hs, float[] y, int ys, int l) {
        for(int n = 0; n < l; ++n) {
            float temp = 0.0F;

            for(int i = 0; i <= n; ++i) {
                temp += x[xs + i] * h[hs + n - i];
            }

            y[ys + n] = temp;
        }

    }

    public static void syn_filt(float[] a, int as, float[] x, int xs, float[] y, int ys, int l, float[] mem, int mems, int update) {
        float[] yy_b = new float[50];
        int yy = 0;

        int i;
        for(i = 0; i < 10; ++i) {
            yy_b[yy++] = mem[mems++];
        }

        for(i = 0; i < l; ++i) {
            int py = yy;
            int pa = 0;
            double s = (double)x[xs++];

            for(int j = 0; j < 10; ++j) {
                ++pa;
                double var18 = (double)a[as + pa];
                --py;
                s -= var18 * (double)yy_b[py];
            }

            yy_b[yy++] = (float)s;
            y[ys++] = (float)s;
        }

        if (update != 0) {
            for(i = 0; i < 10; ++i) {
                --mems;
                --yy;
                mem[mems] = yy_b[yy];
            }
        }

    }

    public static void residu(float[] a, int as, float[] x, int xs, float[] y, int ys, int l) {
        int ya = 0;

        for(int i = 0; i < l; ++i) {
            double s = (double)x[xs + i];

            for(int j = 1; j <= 10; ++j) {
                s += (double)a[as + j] * (double)x[xs + i - j];
            }

            y[ys + ya++] = (float)s;
        }

    }
}
