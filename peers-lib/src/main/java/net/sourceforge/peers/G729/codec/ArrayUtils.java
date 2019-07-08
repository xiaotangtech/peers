package net.sourceforge.peers.G729.codec;

public class ArrayUtils {
    public ArrayUtils() {
    }

    public static float[] subArray(float[] array, int start) {
        int nl = array.length - start;
        float[] ret = new float[nl];

        for(int q = start; q < array.length; ++q) {
            ret[q - start] = array[q];
        }

        return ret;
    }

    public static void replace(float[] array, int start, float[] tmp) {
        for(int q = start; q < array.length; ++q) {
            array[q] = tmp[q - start];
        }

    }
}