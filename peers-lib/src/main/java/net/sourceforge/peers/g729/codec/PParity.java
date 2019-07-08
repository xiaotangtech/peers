package net.sourceforge.peers.g729.codec;

public class PParity {
    public PParity() {
    }

    public static int parity_pitch(int pitch_index) {
        int temp = pitch_index >> 1;
        int sum = 1;

        for(int i = 0; i <= 5; ++i) {
            temp >>= 1;
            int bit = temp & 1;
            sum += bit;
        }

        sum &= 1;
        return sum;
    }

    public static int check_parity_pitch(int pitch_index, int parity) {
        int temp = pitch_index >> 1;
        int sum = 1;

        for(int i = 0; i <= 5; ++i) {
            temp >>= 1;
            int bit = temp & 1;
            sum += bit;
        }

        sum += parity;
        sum &= 1;
        return sum;
    }
}
