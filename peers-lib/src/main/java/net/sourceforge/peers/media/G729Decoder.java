package net.sourceforge.peers.media;

import net.sourceforge.peers.Logger;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by luxuedong
 * 2019/6/27 5:24 PM
 */
public class G729Decoder extends Decoder {

    private Logger logger;

    private org.restcomm.media.codec.g729.Decoder decoder;


    public G729Decoder(Logger logger) {
        decoder = new org.restcomm.media.codec.g729.Decoder();
        this.logger = logger;
    }

    @Override
    public byte[] process(byte[] media) {
        logger.debug("-----------------G729 Decoder To PCM Before length:" + media.length);
        byte[] g7292pcm = g7292pcm(media);
        logger.debug("-----------------G729 Decoder To PCM After length:" + g7292pcm.length);
        return g7292pcm;
    }

    public byte[] g7292pcm(byte[] data) {

        ByteArrayOutputStream dstBuffer = new ByteArrayOutputStream();
        try {
            org.restcomm.media.spi.memory.Frame buffer = org.restcomm.media.spi.memory.Memory.allocate(20);
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
}
