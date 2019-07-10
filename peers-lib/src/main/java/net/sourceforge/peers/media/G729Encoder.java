package net.sourceforge.peers.media;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.G729.codec.G729AEncoder;
import net.sourceforge.peers.G729.spi.memory.Frame;
import net.sourceforge.peers.G729.spi.memory.Memory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by luxuedong
 * 2019/6/28 9:44 AM
 */
public class G729Encoder extends Encoder{


    private Logger logger;

    private org.restcomm.media.core.codec.g729.Encoder encoderJ;

    private G729AEncoder encoder;

//    PipedInputStream rawData, PipedOutputStream encodedData, boolean mediaDebug, Logger logger, String peersHome, CountDownLatch latch
    public G729Encoder(Logger logger) {
        super();
//        super(rawData, encodedData, mediaDebug, logger, peersHome, latch);
        encoderJ = new org.restcomm.media.core.codec.g729.Encoder();
        encoder = new G729AEncoder();
        this.logger=logger;
    }

    @Override
    public byte[] process(byte[] media) {
        logger.debug("+++++++++++++++++++PCM Encoder 2 G729 Before length:" + media.length);
        byte[] bytes = pcm2g729J(media);
        logger.debug("+++++++++++++++++++PCM Encoder 2 G729 after length:" + bytes.length);
        return bytes;
    }

    public byte[] pcm2g729J(byte[] data) {
        ByteArrayOutputStream dstBuffer = new ByteArrayOutputStream();
        try {
            org.restcomm.media.core.spi.memory.Frame buffer = org.restcomm.media.core.spi.memory.Memory.allocate(320);
            byte[] src = buffer.getData();
            int readLen = 0;
            while (readLen < data.length) {
                int remainLen = data.length - readLen;
                int onceLen = 320 < remainLen ? 320 : remainLen;
                System.arraycopy(data, readLen, src, 0, onceLen);
                readLen += onceLen;
                buffer.setLength(onceLen);
                byte[] encodeBytes = encoderJ.process(buffer).getData();
                dstBuffer.write(encodeBytes);
            }
        } catch (Exception e) {

        }
        return dstBuffer.toByteArray();
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

    public byte[] encodeByte(byte[] bytes) {
        byte[] bb = new byte[bytes.length / 16];
        ArrayList<Byte> list = new ArrayList<>();//没有解压的集合
        ArrayList<Byte> list2 = new ArrayList<>();//解压完的集合
        for (int i = 0; i < bytes.length; i++) {
            list.add(bytes[i]);//为集合添加数据
        }
        for (int i = 0; i < list.size() / 160; i++) {
            byte[] b = new byte[160];//创建160为基准的小byte【】
            for (int j = 0; j < 160; j++) {
                b[j] = list.get(i * 160 + j);
            }
            byte[] process = encoder.process(b);
            for (int j = 0; j < process.length; j++) {
                list2.add(process[j]);
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            bb[i] = list2.get(i);
        }
        return bb;
    }
}
