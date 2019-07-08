package net.sourceforge.peers.G729.codec;

public class DecAcelp {
    public DecAcelp() {
    }

    public static void decod_ACELP(int sign, int index, float[] cod) {
        int[] pos = new int[4];
        int i = index & 7;
        pos[0] = i * 5;
        index >>= 3;
        i = index & 7;
        pos[1] = i * 5 + 1;
        index >>= 3;
        i = index & 7;
        pos[2] = i * 5 + 2;
        index >>= 3;
        int j = index & 1;
        index >>= 1;
        i = index & 7;
        pos[3] = i * 5 + 3 + j;

        for(i = 0; i < 40; ++i) {
            cod[i] = 0.0F;
        }

        for(j = 0; j < 4; ++j) {
            i = sign & 1;
            sign >>= 1;
            if (i != 0) {
                cod[pos[j]] = 1.0F;
            } else {
                cod[pos[j]] = -1.0F;
            }
        }

    }
}
