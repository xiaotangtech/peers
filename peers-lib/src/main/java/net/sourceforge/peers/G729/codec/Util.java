package net.sourceforge.peers.G729.codec;

import java.io.IOException;
import java.io.OutputStream;

public class Util {
    public Util() {
    }

    public static void set_zero(float[] x, int L) {
        for(int i = 0; i < L; ++i) {
            x[i] = 0.0F;
        }

    }

    public static void copy(float[] x, float[] y, int L) {
        for(int i = 0; i < L; ++i) {
            y[i] = x[i];
        }

    }

    public static void copy(float[] x, int xs, float[] y, int ys, int L) {
        for(int i = 0; i < L; ++i) {
            y[ys + i] = x[xs + i];
        }

    }

    public static short random_g729() {
        return (short)((int)(65535L & System.currentTimeMillis()));
    }

    public static byte[] floatArrayToByteArray(float[] data, int length) {
        short[] sp16 = new short[80];
        if (length > 80) {
            throw new RuntimeException("error in fwrite16\n");
        } else {
            for(int i = 0; i < length; ++i) {
                float temp = data[i];
                if (temp >= 0.0F) {
                    temp += 0.5F;
                } else {
                    temp -= 0.5F;
                }

                if (temp > 32767.0F) {
                    temp = 32767.0F;
                }

                if (temp < -32768.0F) {
                    temp = -32768.0F;
                }

                sp16[i] = (short)((int)temp);
            }

            byte[] ret = shortArrayToByteArray(sp16);
            return ret;
        }
    }

    public static void fwrite16(float[] data, int length, OutputStream fp) {
        short[] sp16 = new short[80];
        if (length > 80) {
            throw new RuntimeException("error in fwrite16\n");
        } else {
            for(int i = 0; i < length; ++i) {
                float temp = data[i];
                if (temp >= 0.0F) {
                    temp += 0.5F;
                } else {
                    temp -= 0.5F;
                }

                if (temp > 32767.0F) {
                    temp = 32767.0F;
                }

                if (temp < -32768.0F) {
                    temp = -32768.0F;
                }

                sp16[i] = (short)((int)temp);

                try {
                    fp.write(shortToBytes(sp16[i]));
                } catch (IOException var7) {
                    var7.printStackTrace();
                }
            }

        }
    }

    public static byte[] shortToBytes(int myInt) {
        byte[] bytes = new byte[2];
        int hexBase = 255;
        bytes[0] = (byte)(hexBase & myInt);
        bytes[1] = (byte)((hexBase << 8 & myInt) >> 8);
        return bytes;
    }

    public static short bytesToShort(byte[] bytes) {
        return (short)('\uffff' & (bytes[0] | bytes[1] << 8));
    }

    public static short bytesToShort(byte byte1, byte byte2) {
        return (short)('\uffff' & (255 & byte1 | (255 & byte2) << 8));
    }

    public static short[] byteArrayToShortArray(byte[] bytes) {
        short[] s = new short[bytes.length / 2];

        for(int q = 0; q < s.length; ++q) {
            s[q] = bytesToShort(bytes[2 * q], bytes[2 * q + 1]);
        }

        return s;
    }

    public static byte[] shortArrayToByteArray(short[] values) {
        byte[] s = new byte[values.length * 2];

        for(int q = 0; q < values.length; ++q) {
            byte[] bytes = shortToBytes(values[q]);
            s[2 * q] = bytes[0];
            s[2 * q + 1] = bytes[1];
        }

        return s;
    }
}
