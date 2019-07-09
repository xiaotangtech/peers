package net.sourceforge.peers.G729.codec;

public class PredLt {
    public PredLt() {
    }

    public static void pred_lt_3(float[] exc, int excs, int t0, int frac, int l_subfr) {
        int x0 = -t0;
        frac = -frac;
        if (frac < 0) {
            frac += 3;
            --x0;
        }

        for(int j = 0; j < l_subfr; ++j) {
            int x1 = x0++;
            int x2 = x0;
            int c1 = frac;
            int c2 = 3 - frac;
            float s = 0.0F;
            int i = 0;

            for(int k = 0; i < 10; k += 3) {
                s += exc[excs + x1 - i] * TabLD8k.inter_3l[c1 + k] + exc[excs + x2 + i] * TabLD8k.inter_3l[c2 + k];
                ++i;
            }

            exc[excs + j] = s;
        }

    }
}
