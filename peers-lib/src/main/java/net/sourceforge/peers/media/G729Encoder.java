package net.sourceforge.peers.media;

import net.sourceforge.peers.g729.codec.G729AEncoder;
import net.sourceforge.peers.Logger;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by luxuedong
 * 2019/6/28 9:44 AM
 */
public class G729Encoder extends Encoder{


    private Logger logger;

    public G729Encoder(PipedInputStream rawData, PipedOutputStream encodedData, boolean mediaDebug, Logger logger, String peersHome, CountDownLatch latch) {
        super(rawData, encodedData, mediaDebug, logger, peersHome, latch);
        this.logger=logger;
    }

    @Override
    public byte[] process(byte[] media) {
        return encodeByte(media);
    }

    public byte[] encodeByte(byte[] bytes) {
        logger.debug("+++++++++++++++++++G729Encoder before length:" + bytes.length);
        G729AEncoder encoder = new G729AEncoder();
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
        logger.debug("-------------------G729Encoder after length:" + bb.length);
        return bb;
    }
}
