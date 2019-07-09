package net.sourceforge.peers.G729.codec;

public class DecLag {
    public DecLag() {
    }

    public static void dec_lag3(int index, int pit_min, int pit_max, int i_subfr, IntegerPointer T0, IntegerPointer T0_frac) {
        if (i_subfr == 0) {
            if (index < 197) {
                T0.value = (index + 2) / 3 + 19;
                T0_frac.value = index - T0.value * 3 + 58;
            } else {
                T0.value = index - 112;
                T0_frac.value = 0;
            }
        } else {
            int T0_min = T0.value - 5;
            if (T0_min < pit_min) {
                T0_min = pit_min;
            }

            int T0_max = T0_min + 9;
            if (T0_max > pit_max) {
                T0_min = pit_max - 9;
            }

            int i = (index + 2) / 3 - 1;
            T0.value = i + T0_min;
            T0_frac.value = index - 2 - i * 3;
        }

    }
}
