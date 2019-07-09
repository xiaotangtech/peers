package net.sourceforge.peers.G729.codec;

public class Bits {
    public Bits() {
    }

    public static void prm2bits_ld8k_b(int[] anau, byte[] dst) {
        dst[0] = (byte)(anau[0] & 255);
        dst[1] = (byte)((anau[1] & 1023) >> 2);
        dst[2] = (byte)((anau[1] & 3) << 6 | anau[2] >> 2 & 63);
        dst[3] = (byte)((anau[2] & 3) << 6 | (anau[3] & 1) << 5 | (anau[4] & 8191) >> 8);
        dst[4] = (byte)(anau[4] & 255);
        dst[5] = (byte)((anau[5] & 15) << 4 | (anau[6] & 127) >> 3);
        dst[6] = (byte)((anau[6] & 7) << 5 | anau[7] & 31);
        dst[7] = (byte)((anau[8] & 8191) >> 5);
        dst[8] = (byte)((anau[8] & 31) << 3 | (anau[9] & 15) >> 1);
        dst[9] = (byte)((anau[9] & 1) << 7 | anau[10] & 127);
    }

    public static void prm2bits_ld8k(int[] prm, short[] bits) {
        int bitsp = 0;
        bitsp = bitsp + 1;
        bits[bitsp] = 27425;
        bits[bitsp++] = 80;

        for(short i = 0; i < 11; ++i) {
            int2bin(prm[i], TabLD8k.bitsno[i], bits, bitsp);
            bitsp += TabLD8k.bitsno[i];
        }

    }

    public static byte[] toRealBits(short[] fakebits) {
        byte[] real = new byte[10];

        for(int q = 0; q < 80; ++q) {
            if (fakebits[q + 2] == 129) {
                int tmp = real[q / 8];
                int onebit = 1 << 7 - q % 8;
                tmp = tmp | onebit;
                real[q / 8] = (byte)(255 & tmp);
            }
        }

        return real;
    }

    public static short[] fromRealBits(byte[] real) {
        short[] fake = new short[82];
        fake[0] = 27425;
        fake[1] = 80;

        for(int q = 0; q < 80; ++q) {
            if ((real[q / 8] & 1 << 7 - q % 8) != 0) {
                fake[q + 2] = 129;
            } else {
                fake[q + 2] = 127;
            }
        }

        return fake;
    }

    static void int2bin(int value, int no_of_bits, short[] bitstream, int bitstreams) {
        int pt_bitstream = bitstreams + no_of_bits;

        for(short i = 0; i < no_of_bits; ++i) {
            short bit = (short)(value & 1);
            if (bit == 0) {
                --pt_bitstream;
                bitstream[pt_bitstream] = 127;
            } else {
                --pt_bitstream;
                bitstream[pt_bitstream] = 129;
            }

            value >>= 1;
        }

    }

    public static void bits2prm_ld8k(short[] bits, int bitss, int[] prm, int ps) {
        for(short i = 0; i < 11; ++i) {
            prm[i + ps] = bin2int(TabLD8k.bitsno[i], bits, bitss);
            bitss += TabLD8k.bitsno[i];
        }

    }

    static short bin2int(int no_of_bits, short[] bitstream, int bitstreams) {
        short value = 0;

        for(short i = 0; i < no_of_bits; ++i) {
            value = (short)(value << 1);
            short bit = bitstream[bitstreams++];
            if (bit == 129) {
                ++value;
            }
        }

        return value;
    }
}