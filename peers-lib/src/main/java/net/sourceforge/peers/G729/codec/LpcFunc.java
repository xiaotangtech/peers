package net.sourceforge.peers.G729.codec;

public class LpcFunc {
    public LpcFunc() {
    }

    public static void lsp_az(float[] lsp, int lsps, float[] a, int as) {
        float[] f1 = new float[6];
        float[] f2 = new float[6];
        get_lsp_pol(lsp, lsps, f1, 0);
        get_lsp_pol(lsp, lsps + 1, f2, 0);

        int i;
        for(i = 5; i > 0; --i) {
            f1[i] += f1[i - 1];
            f2[i] -= f2[i - 1];
        }

        a[as + 0] = 1.0F;
        i = 1;

        for(int j = 10; i <= 5; --j) {
            a[as + i] = 0.5F * (f1[i] + f2[i]);
            a[as + j] = 0.5F * (f1[i] - f2[i]);
            ++i;
        }

    }

    public static void get_lsp_pol(float[] lsp, int lsps, float[] f, int fs) {
        f[fs + 0] = 1.0F;
        float b = -2.0F * lsp[lsps + 0];
        f[fs + 1] = b;

        for(int i = 2; i <= 5; ++i) {
            b = -2.0F * lsp[lsps + 2 * i - 2];
            f[i] = b * f[fs + i - 1] + 2.0F * f[fs + i - 2];

            for(int j = i - 1; j > 1; --j) {
                f[fs + j] += b * f[fs + j - 1] + f[fs + j - 2];
            }

            f[fs + 1] += b;
        }

    }

    public static void lsf_lsp(float[] lsf, float[] lsp, int m) {
        for(int i = 0; i < m; ++i) {
            lsp[i] = (float)Math.cos((double)lsf[i]);
        }

    }

    public static void lsp_lsf(float[] lsp, float[] lsf, int m) {
        for(int i = 0; i < m; ++i) {
            lsf[i] = (float)Math.acos((double)lsp[i]);
        }

    }

    public static void weight_az(float[] a, int as, float gamma, int m, float[] ap, int aps) {
        ap[aps] = a[as];
        float fac = gamma;

        for(int i = 1; i < m; ++i) {
            ap[aps + i] = fac * a[as + i];
            fac *= gamma;
        }

        ap[aps + m] = fac * a[as + m];
    }

    public static void int_qlpc(float[] lsp_old, float[] lsp_new, float[] az) {
        float[] lsp = new float[10];

        for(int i = 0; i < 10; ++i) {
            lsp[i] = lsp_old[i] * 0.5F + lsp_new[i] * 0.5F;
        }

        lsp_az(lsp, 0, az, 0);
        lsp_az(lsp_new, 0, az, 11);
    }

    public static void int_lpc(float[] lsp_old, float[] lsp_new, float[] lsf_int, float[] lsf_new, float[] az) {
        float[] lsp = new float[10];

        for(int i = 0; i < 10; ++i) {
            lsp[i] = lsp_old[i] * 0.5F + lsp_new[i] * 0.5F;
        }

        lsp_az(lsp, 0, az, 0);
        lsp_lsf(lsp, lsf_int, 10);
        lsp_lsf(lsp_new, lsf_new, 10);
    }
}
