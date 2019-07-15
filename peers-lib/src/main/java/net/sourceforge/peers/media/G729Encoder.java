package net.sourceforge.peers.media;

import net.sourceforge.peers.Logger;
import org.restcomm.media.spi.memory.Frame;
import org.restcomm.media.spi.memory.Memory;

import java.io.ByteArrayOutputStream;

/**
 * Created by luxuedong
 * 2019/6/28 9:44 AM
 */
public class G729Encoder extends Encoder{


    private Logger logger;

    private org.restcomm.media.codec.g729.Encoder encoder;

    public G729Encoder(Logger logger) {
        encoder = new org.restcomm.media.codec.g729.Encoder();
        this.logger=logger;
    }

    @Override
    public byte[] process(byte[] media) {
        byte[] bytes = encodePcm2G729(media);
        logger.debug("G729 Encode after size: "+bytes.length);
        return bytes;
    }


    public byte[] encodePcm2G729(byte[] src){
         byte[] result = new byte[src.length / 16];
         for(int i = 0;i<src.length/160;i++){
             byte[] temp = new byte[160];
             System.arraycopy(src, i*160, temp, 0, 160);
             byte[] process = encoder.process(temp);//10
             System.arraycopy(process, 0,result , i*10, 10);
         }
         return result;
    }

    public byte[] pcm2g729(byte[] data) {
        ByteArrayOutputStream dstBuffer = new ByteArrayOutputStream();
        try {
            Frame buffer = Memory.allocate(320);
            byte[] src = buffer.getData();
            int readLen = 0;
            while (readLen < data.length) {
                int remainLen = data.length - readLen;
                int onceLen = 320 < remainLen ? 320 : remainLen;
                System.arraycopy(data, readLen, src, 0, onceLen);
                readLen += onceLen;
                buffer.setLength(onceLen);
                byte[] encodeBytes = encoder.process(buffer).getData();
                dstBuffer.write(encodeBytes);
            }
        } catch (Exception e) {

        }
        return dstBuffer.toByteArray();
    }
}
