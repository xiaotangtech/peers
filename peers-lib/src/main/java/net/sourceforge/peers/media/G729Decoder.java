package net.sourceforge.peers.media;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.g729.codec.G729ADecoder;
import net.sourceforge.peers.g729.spi.memory.Frame;
import net.sourceforge.peers.g729.spi.memory.Memory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by luxuedong
 * 2019/6/27 5:24 PM
 */
public class G729Decoder extends Decoder {

    private Logger logger;

    private G729ADecoder decoder;

    public G729Decoder(Logger logger) {
        decoder = new G729ADecoder();
        this.logger = logger;
    }

    @Override
    public byte[] process(byte[] media) {
        logger.debug("-----------------G729 Dncoder 2 PCM Before length:" + media.length);
        byte[] g7292pcm = g7292pcm(media);
        logger.debug("-----------------G729 Dncoder 2 PCM After length:" + g7292pcm.length);
        return g7292pcm;
    }

    public byte[] g7292pcm(byte[] data) {

        ByteArrayOutputStream dstBuffer = new ByteArrayOutputStream();
        try {
            Frame buffer = Memory.allocate(20);
            byte[] src = buffer.getData();
            int readLen = 0;
            while (readLen < data.length) {
                int remainLen = data.length - readLen;
                int onceLen = 20 < remainLen ? 20 : remainLen;
                System.arraycopy(data, readLen, src, 0, onceLen);
                readLen += onceLen;
                buffer.setLength(onceLen);
                byte[] encodeBytes = decoder.process(buffer).getData();
                dstBuffer.write(encodeBytes);
            }
        } catch (Exception e) {

        }
        return dstBuffer.toByteArray();
    }

    public byte[] decodeByte(byte[] bytes) {
        logger.debug("-----------------G729Decoder before length:" + bytes.length);
        G729ADecoder decoder = new G729ADecoder();
        byte[] bb = new byte[bytes.length * 16];
        ArrayList<Byte> list = new ArrayList<>();
        for (int i = 0; i < bytes.length / 10; i++) {
            byte[] b = {bytes[i * 10], bytes[1 + i * 10], bytes[2 + i * 10], bytes[3 + i * 10], bytes[4 + i * 10], bytes[5 + i * 10], bytes[6 + i * 10], bytes[7 + i * 10], bytes[8 + i * 10], bytes[9 + i * 10]};
            byte[] process = decoder.process(b);
            ArrayList<Byte> arr = new ArrayList<>();
            for (int j = 0; j < process.length; j++) {
                arr.add(process[j]);
            }
            list.addAll(arr);
        }
        for (int i = 0; i < list.size(); i++) {
            bb[i] = list.get(i);
        }
        logger.debug("-----------------G729Decoder after length:" + bb.length);
        return bb;
    }
}
