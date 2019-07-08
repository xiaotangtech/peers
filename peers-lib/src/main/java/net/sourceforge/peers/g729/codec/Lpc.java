package net.sourceforge.peers.g729.codec;

public class Lpc {
    public Lpc() {
    }

    void autocorr(float[] x, int m, float[] r) {
        float[] y = new float[240];

        int i;
        for(i = 0; i < 240; ++i) {
            y[i] = x[i] * TabLD8k.hamwindow[i];
        }

        for(i = 0; i <= m; ++i) {
            float sum = 0.0F;

            for(int j = 0; j < 240 - i; ++j) {
                sum += y[j] * y[j + i];
            }

            r[i] = sum;
        }

        if (r[0] < 1.0F) {
            r[0] = 1.0F;
        }

    }

    void lag_window(int m, float[] r) {
        for(int i = 1; i <= m; ++i) {
            r[i] *= TabLD8k.lwindow[i - 1];
        }

    }

    float levinson(float[] r, float[] a, float[] rc) {
        rc[0] = -r[1] / r[0];
        a[0] = 1.0F;
        a[1] = rc[0];
        float err = r[0] + r[1] * rc[0];

        for(int i = 2; i <= 10; ++i) {
            float s = 0.0F;

            int j;
            for(j = 0; j < i; ++j) {
                s += r[i - j] * a[j];
            }

            rc[i - 1] = -s / err;

            for(j = 1; j <= i / 2; ++j) {
                int l = i - j;
                float at = a[j] + rc[i - 1] * a[l];
                a[l] += rc[i - 1] * a[j];
                a[j] = at;
            }

            a[i] = rc[i - 1];
            err += rc[i - 1] * s;
            if (err <= 0.0F) {
                err = 0.001F;
            }
        }

        return err;
    }

    void az_lsp(float[] a, float[] lsp, float[] old_lsp) {
        float[] f1 = new float[6];
        float[] f2 = new float[6];
        f1[0] = 1.0F;
        f2[0] = 1.0F;
        int i = 1;

        int j;
        for(j = 10; i <= 5; --j) {
            f1[i] = a[i] + a[j] - f1[i - 1];
            f2[i] = a[i] - a[j] + f2[i - 1];
            ++i;
        }

        int nf = 0;
        int ip = 0;
        float[] coef = f1;
        float xlow = TabLD8k.grid[0];
        float ylow = chebyshev(xlow, f1, 5);
        j = 0;

        while(nf < 10 && j < 60) {
            ++j;
            float xhigh = xlow;
            float yhigh = ylow;
            xlow = TabLD8k.grid[j];
            ylow = chebyshev(xlow, coef, 5);
            if (ylow * yhigh <= 0.0F) {
                --j;

                for(i = 0; i < 4; ++i) {
                    float xmid = 0.5F * (xlow + xhigh);
                    float ymid = chebyshev(xmid, coef, 5);
                    if (ylow * ymid <= 0.0F) {
                        yhigh = ymid;
                        xhigh = xmid;
                    } else {
                        ylow = ymid;
                        xlow = xmid;
                    }
                }

                float xint = xlow - ylow * (xhigh - xlow) / (yhigh - ylow);
                lsp[nf] = xint;
                ++nf;
                ip = 1 - ip;
                coef = ip != 0 ? f2 : f1;
                xlow = xint;
                ylow = chebyshev(xint, coef, 5);
            }
        }

        if (nf < 10) {
            for(i = 0; i < 10; ++i) {
                lsp[i] = old_lsp[i];
            }
        }

    }

    static float chebyshev(float x, float[] f, int n) {
        float x2 = 2.0F * x;
        float b2 = 1.0F;
        float b1 = x2 + f[1];

        for(int i = 2; i < n; ++i) {
            float b0 = x2 * b1 - b2 + f[i];
            b2 = b1;
            b1 = b0;
        }

        return x * b1 - b2 + 0.5F * f[n];
    }
}
