package net.sourceforge.peers.G729.codec;

import net.sourceforge.peers.G729.spi.dsp.Codec;
import net.sourceforge.peers.G729.spi.format.Format;
import net.sourceforge.peers.G729.spi.format.FormatFactory;
import net.sourceforge.peers.G729.spi.memory.Frame;
import net.sourceforge.peers.G729.spi.memory.Memory;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class G729AEncoder implements Codec {
    private static final Format g729 = FormatFactory.createAudioFormat("g729", 8000);
    private static final Format linear = FormatFactory.createAudioFormat("linear", 8000, 16, 1);
    int frame = 0;
    CodLD8K encoder = new CodLD8K();
    PreProc preProc = new PreProc();
    CircularBuffer circularBuffer = new CircularBuffer(32000);
    int[] prm = new int[11];
    short[] serial = new short[82];
    FileInputStream testData = null;
    FileOutputStream outdbg = null;

    public G729AEncoder() {
        this.preProc.init_pre_process();
        this.encoder.init_coder_ld8k();
    }

    public Format getSupportedInputFormat() {
        return linear;
    }

    public Format getSupportedOutputFormat() {
        return g729;
    }

    public Frame process(Frame frame) {
        Frame res = null;
        byte[] data = frame.getData();
        this.circularBuffer.addData(data);
        int frameSize = 160;
        byte[] speechWindow = this.circularBuffer.getData(2 * frameSize);
        byte[] resultingBytes = null;
        if (speechWindow == null) {
            byte[] var11 = new byte[0];
        } else {
            byte[] one = new byte[frameSize];
            byte[] two = new byte[frameSize];

            for(int q = 0; q < frameSize; ++q) {
                one[q] = speechWindow[q];
                two[q] = speechWindow[q + frameSize];
            }

            one = this.process(one);
            two = this.process(two);
            if (one.length != two.length) {
                throw new RuntimeException("The two frames are not equal in size!");
            }

            res = Memory.allocate(one.length + two.length);
            res.setLength(one.length + two.length);
            byte[] resultBytes = res.getData();

            for(int q = 0; q < one.length; ++q) {
                resultBytes[q] = one[q];
                resultBytes[q + one.length] = two[q];
            }
        }

        res.setOffset(0);
        res.setTimestamp(frame.getTimestamp());
        res.setDuration(frame.getDuration());
        res.setSequenceNumber(frame.getSequenceNumber());
        res.setEOM(frame.isEOM());
        res.setFormat(g729);
        return res;
    }

    public byte[] process(byte[] media) {
        ++this.frame;
        float[] new_speech = new float[media.length];
        short[] shortMedia = Util.byteArrayToShortArray(media);

        for(int i = 0; i < 80; ++i) {
            new_speech[i] = (float)shortMedia[i];
        }

        this.preProc.pre_process(new_speech, 80);
        this.encoder.loadSpeech(new_speech);
        this.encoder.coder_ld8k(this.prm, 0);
        Bits.prm2bits_ld8k(this.prm, this.serial);
        return Bits.toRealBits(this.serial);
    }

    public void processTestDecoderWithFileITUEncoded(Frame buffer) {
    }

    public void processTestFileWithoutDecoding(Frame buffer) {
        Object var2 = null;

        try {
            byte[] data = new byte[20];
            byte[] tmp = new byte[164];
            this.testData.read(tmp);
            short[] sdata = Util.byteArrayToShortArray(tmp);
            byte[] bits = Bits.toRealBits(sdata);
            System.arraycopy(bits, 0, data, 0, 10);
            this.testData.read(tmp);
            sdata = Util.byteArrayToShortArray(tmp);
            bits = Bits.toRealBits(sdata);
            System.arraycopy(bits, 0, data, 10, 10);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }
}
