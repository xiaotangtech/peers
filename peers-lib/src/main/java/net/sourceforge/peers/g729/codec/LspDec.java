package net.sourceforge.peers.g729.codec;

public class LspDec {
    float[][] freq_prev = new float[4][10];
    float[] freq_prev_reset = new float[]{0.285599F, 0.571199F, 0.856798F, 1.142397F, 1.427997F, 1.713596F, 1.999195F, 2.284795F, 2.570394F, 2.855993F};
    static int prev_ma;
    static float[] prev_lsp = new float[10];

    public LspDec() {
    }

    void lsp_decw_reset() {
        for(int i = 0; i < 4; ++i) {
            Util.copy(this.freq_prev_reset, this.freq_prev[i], 10);
        }

        prev_ma = 0;
        Util.copy(this.freq_prev_reset, prev_lsp, 10);
    }

    public void lsp_iqua_cs(int[] prm, int prms, float[] lsp_q, int erase) {
        float[] buf = new float[10];
        if (erase == 0) {
            int mode_index = prm[prms + 0] >> 7 & 1;
            int code0 = prm[prms + 0] & 127;
            int code1 = prm[prms + 1] >> 5 & 31;
            int code2 = prm[prms + 1] & 31;
            LspGetq.lsp_get_quant(TabLD8k.lspcb1, TabLD8k.lspcb2, code0, code1, code2, TabLD8k.fg[mode_index], this.freq_prev, lsp_q, TabLD8k.fg_sum[mode_index]);
            Util.copy(lsp_q, prev_lsp, 10);
            prev_ma = mode_index;
        } else {
            Util.copy(prev_lsp, lsp_q, 10);
            LspGetq.lsp_prev_extract(prev_lsp, buf, TabLD8k.fg[prev_ma], this.freq_prev, TabLD8k.fg_sum_inv[prev_ma]);
            LspGetq.lsp_prev_update(buf, this.freq_prev);
        }

    }

    public void d_lsp(int[] index, int is, float[] lsp_q, int bfi) {
        this.lsp_iqua_cs(index, is, lsp_q, bfi);

        for(int i = 0; i < 10; ++i) {
            lsp_q[i] = (float)Math.cos((double)lsp_q[i]);
        }

    }
}
