package net.sourceforge.peers.media;

//import net.sourceforge.peers.g729.codec.G729ADecoder;

import java.util.ArrayList;

/**
 * Created by luxuedong
 * 2019/6/27 5:24 PM
 */
public class G729Decoder extends Decoder {
    @Override
    public byte[] process(byte[] media) {
        return decodeByte(media);
    }


    public static byte[] decodeByte(byte[] bytes) {
//        G729ADecoder decoder = new G729ADecoder();
//        byte[] bb = new byte[bytes.length * 16];
//        ArrayList<Byte> list = new ArrayList<>();
//        for (int i = 0; i < bytes.length / 10; i++) {
//            byte[] b = {bytes[i * 10], bytes[1 + i * 10], bytes[2 + i * 10], bytes[3 + i * 10], bytes[4 + i * 10], bytes[5 + i * 10], bytes[6 + i * 10], bytes[7 + i * 10], bytes[8 + i * 10], bytes[9 + i * 10]};
//            byte[] process = decoder.process(b);
//            ArrayList<Byte> arr = new ArrayList<>();
//            for (int j = 0; j < process.length; j++) {
//                arr.add(process[j]);
//            }
//            list.addAll(arr);
//        }
//        for (int i = 0; i < list.size(); i++) {
//            bb[i] = list.get(i);
//        }
//        return bb;
        return null;
    }
}
