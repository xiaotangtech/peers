package net.sourceforge.peers.g729.codec;

public class LspGetq {
    public LspGetq() {
    }

    public static void lsp_get_quant(float[][] lspcb1, float[][] lspcb2, int code0, int code1, int code2, float[][] fg, float[][] freq_prev, float[] lspq, float[] fg_sum) {
        float[] buf = new float[10];

        int j;
        for(j = 0; j < 5; ++j) {
            buf[j] = lspcb1[code0][j] + lspcb2[code1][j];
        }

        for(j = 5; j < 10; ++j) {
            buf[j] = lspcb1[code0][j] + lspcb2[code2][j];
        }

        lsp_expand_1_2(buf, 0.0012F);
        lsp_expand_1_2(buf, 6.0E-4F);
        lsp_prev_compose(buf, lspq, fg, freq_prev, fg_sum);
        lsp_prev_update(buf, freq_prev);
        lsp_stability(lspq);
    }

    public static void lsp_expand_1(float[] buf, float gap) {
        for(int j = 1; j < 5; ++j) {
            float diff = buf[j - 1] - buf[j];
            float tmp = (diff + gap) * 0.5F;
            if (tmp > 0.0F) {
                buf[j - 1] -= tmp;
                buf[j] += tmp;
            }
        }

    }

    public static void lsp_expand_2(float[] buf, float gap) {
        for(int j = 5; j < 10; ++j) {
            float diff = buf[j - 1] - buf[j];
            float tmp = (diff + gap) * 0.5F;
            if (tmp > 0.0F) {
                buf[j - 1] -= tmp;
                buf[j] += tmp;
            }
        }

    }

    public static void lsp_expand_1_2(float[] buf, float gap) {
        for(int j = 1; j < 10; ++j) {
            float diff = buf[j - 1] - buf[j];
            float tmp = (diff + gap) * 0.5F;
            if (tmp > 0.0F) {
                buf[j - 1] -= tmp;
                buf[j] += tmp;
            }
        }

    }

    public static void lsp_prev_compose(float[] lsp_ele, float[] lsp, float[][] fg, float[][] freq_prev, float[] fg_sum) {
        for(int j = 0; j < 10; ++j) {
            lsp[j] = lsp_ele[j] * fg_sum[j];

            for(int k = 0; k < 4; ++k) {
                lsp[j] += freq_prev[k][j] * fg[k][j];
            }
        }

    }

    public static void lsp_prev_extract(float[] lsp, float[] lsp_ele, float[][] fg, float[][] freq_prev, float[] fg_sum_inv) {
        for(int j = 0; j < 10; ++j) {
            lsp_ele[j] = lsp[j];

            for(int k = 0; k < 4; ++k) {
                lsp_ele[j] -= freq_prev[k][j] * fg[k][j];
            }

            lsp_ele[j] *= fg_sum_inv[j];
        }

    }

    public static void lsp_prev_update(float[] lsp_ele, float[][] freq_prev) {
        for(int k = 3; k > 0; --k) {
            Util.copy(freq_prev[k - 1], freq_prev[k], 10);
        }

        Util.copy(lsp_ele, freq_prev[0], 10);
    }

    public static void lsp_stability(float[] buf) {
        int j;
        float diff;
        for(j = 0; j < 9; ++j) {
            diff = buf[j + 1] - buf[j];
            if (diff < 0.0F) {
                float tmp = buf[j + 1];
                buf[j + 1] = buf[j];
                buf[j] = tmp;
            }
        }

        if (buf[0] < 0.005F) {
            buf[0] = 0.005F;
            System.out.println("warning LSP Low \n");
        }

        for(j = 0; j < 9; ++j) {
            diff = buf[j + 1] - buf[j];
            if (diff < 0.0392F) {
                buf[j + 1] = buf[j] + 0.0392F;
            }
        }

        if (buf[9] > 3.135F) {
            buf[9] = 3.135F;
            System.out.println("warning LSP High \n");
        }

    }
}
