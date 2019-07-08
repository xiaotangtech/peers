package net.sourceforge.peers.g729.codec;

public class QuaLsp {
    float[][] freq_prev = new float[4][10];
    float[] freq_prev_reset = new float[]{0.285599F, 0.571199F, 0.856798F, 1.142397F, 1.427997F, 1.713596F, 1.999195F, 2.284795F, 2.570394F, 2.855993F};

    public QuaLsp() {
    }

    public void qua_lsp(float[] lsp, float[] lsp_q, int[] ana) {
        float[] lsf = new float[10];
        float[] lsf_q = new float[10];

        int i;
        for(i = 0; i < 10; ++i) {
            lsf[i] = (float)Math.acos((double)lsp[i]);
        }

        this.lsp_qua_cs(lsf, lsf_q, ana);

        for(i = 0; i < 10; ++i) {
            lsp_q[i] = (float)Math.cos((double)lsf_q[i]);
        }

    }

    void lsp_encw_reset() {
        for(int i = 0; i < 4; ++i) {
            Util.copy(this.freq_prev_reset, this.freq_prev[i], 10);
        }

    }

    public void lsp_qua_cs(float[] flsp_in, float[] lspq_out, int[] code) {
        float[] wegt = new float[10];
        get_wegt(flsp_in, wegt);
        relspwed(flsp_in, wegt, lspq_out, TabLD8k.lspcb1, TabLD8k.lspcb2, TabLD8k.fg, this.freq_prev, TabLD8k.fg_sum, TabLD8k.fg_sum_inv, code);
    }

    static void relspwed(float[] lsp, float[] wegt, float[] lspq, float[][] lspcb1, float[][] lspcb2, float[][][] fg, float[][] freq_prev, float[][] fg_sum, float[][] fg_sum_inv, int[] code_ana) {
        IntegerPointer index = new IntegerPointer();
        IntegerPointer mode_index = new IntegerPointer();
        IntegerPointer cand_cur = new IntegerPointer();
        int[] cand = new int[2];
        int[] tindex1 = new int[2];
        int[] tindex2 = new int[2];
        float[] tdist = new float[2];
        float[] rbuf = new float[10];
        float[] buf = new float[10];

        for(int mode = 0; mode < 2; ++mode) {
            LspGetq.lsp_prev_extract(lsp, rbuf, fg[mode], freq_prev, fg_sum_inv[mode]);
            lsp_pre_select(rbuf, lspcb1, cand_cur);
            cand[mode] = cand_cur.value;
            lsp_select_1(rbuf, lspcb1[cand_cur.value], wegt, lspcb2, index);
            tindex1[mode] = index.value;

            int j;
            for(j = 0; j < 5; ++j) {
                buf[j] = lspcb1[cand_cur.value][j] + lspcb2[index.value][j];
            }

            LspGetq.lsp_expand_1(buf, 0.0012F);
            lsp_select_2(rbuf, lspcb1[cand_cur.value], wegt, lspcb2, index);
            tindex2[mode] = index.value;

            for(j = 5; j < 10; ++j) {
                buf[j] = lspcb1[cand_cur.value][j] + lspcb2[index.value][j];
            }

            LspGetq.lsp_expand_2(buf, 0.0012F);
            LspGetq.lsp_expand_1_2(buf, 6.0E-4F);
            FloatPointer tmp = new FloatPointer(tdist[mode]);
            lsp_get_tdist(wegt, buf, tmp, rbuf, fg_sum[mode]);
            tdist[mode] = tmp.value;
        }

        lsp_last_select(tdist, mode_index);
        code_ana[0] = mode_index.value << 7 | cand[mode_index.value];
        code_ana[1] = tindex1[mode_index.value] << 5 | tindex2[mode_index.value];
        LspGetq.lsp_get_quant(lspcb1, lspcb2, cand[mode_index.value], tindex1[mode_index.value], tindex2[mode_index.value], fg[mode_index.value], freq_prev, lspq, fg_sum[mode_index.value]);
    }

    static void lsp_pre_select(float[] rbuf, float[][] lspcb1, IntegerPointer cand) {
        cand.value = 0;
        float dmin = 1.0E38F;

        for(int i = 0; i < 128; ++i) {
            float dist = 0.0F;

            for(int j = 0; j < 10; ++j) {
                float temp = rbuf[j] - lspcb1[i][j];
                dist += temp * temp;
            }

            if (dist < dmin) {
                dmin = dist;
                cand.value = i;
            }
        }

    }

    static void lsp_select_1(float[] rbuf, float[] lspcb1, float[] wegt, float[][] lspcb2, IntegerPointer index) {
        float[] buf = new float[10];

        int j;
        for(j = 0; j < 5; ++j) {
            buf[j] = rbuf[j] - lspcb1[j];
        }

        index.value = 0;
        float dmin = 1.0E38F;

        for(int k1 = 0; k1 < 32; ++k1) {
            float dist = 0.0F;

            for(j = 0; j < 5; ++j) {
                float tmp = buf[j] - lspcb2[k1][j];
                dist += wegt[j] * tmp * tmp;
            }

            if (dist < dmin) {
                dmin = dist;
                index.value = k1;
            }
        }

    }

    static void lsp_select_2(float[] rbuf, float[] lspcb1, float[] wegt, float[][] lspcb2, IntegerPointer index) {
        float[] buf = new float[10];

        int j;
        for(j = 5; j < 10; ++j) {
            buf[j] = rbuf[j] - lspcb1[j];
        }

        index.value = 0;
        float dmin = 1.0E38F;

        for(int k1 = 0; k1 < 32; ++k1) {
            float dist = 0.0F;

            for(j = 5; j < 10; ++j) {
                float tmp = buf[j] - lspcb2[k1][j];
                dist += wegt[j] * tmp * tmp;
            }

            if (dist < dmin) {
                dmin = dist;
                index.value = k1;
            }
        }

    }

    static void lsp_get_tdist(float[] wegt, float[] buf, FloatPointer tdist, float[] rbuf, float[] fg_sum) {
        tdist.value = 0.0F;

        for(int j = 0; j < 10; ++j) {
            float tmp = (buf[j] - rbuf[j]) * fg_sum[j];
            tdist.value = tdist.value + wegt[j] * tmp * tmp;
        }

    }

    static void lsp_last_select(float[] tdist, IntegerPointer mode_index) {
        mode_index.value = 0;
        if (tdist[1] < tdist[0]) {
            mode_index.value = 1;
        }

    }

    static void get_wegt(float[] flsp, float[] wegt) {
        float tmp = flsp[1] - 0.12566371F - 1.0F;
        if (tmp > 0.0F) {
            wegt[0] = 1.0F;
        } else {
            wegt[0] = tmp * tmp * 10.0F + 1.0F;
        }

        for(int i = 1; i < 9; ++i) {
            tmp = flsp[i + 1] - flsp[i - 1] - 1.0F;
            if (tmp > 0.0F) {
                wegt[i] = 1.0F;
            } else {
                wegt[i] = tmp * tmp * 10.0F + 1.0F;
            }
        }

        tmp = 2.8902655F - flsp[8] - 1.0F;
        if (tmp > 0.0F) {
            wegt[9] = 1.0F;
        } else {
            wegt[9] = tmp * tmp * 10.0F + 1.0F;
        }

        wegt[4] *= 1.2F;
        wegt[5] *= 1.2F;
    }
}
