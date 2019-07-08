package net.sourceforge.peers.G729.codec;

import net.sourceforge.peers.G729.spi.dsp.Codec;
import net.sourceforge.peers.G729.spi.format.Format;
import net.sourceforge.peers.G729.spi.format.FormatFactory;
import net.sourceforge.peers.G729.spi.memory.Frame;
import net.sourceforge.peers.G729.spi.memory.Memory;

public class G729ADecoder implements Codec {
    private static final Format g729 = FormatFactory.createAudioFormat("g729", 8000);
    private static final Format linear = FormatFactory.createAudioFormat("linear", 8000, 16, 1);
    int frame = 0;
    CircularBuffer circular = new CircularBuffer(16000);
    float[] synth_buf = new float[90];
    int synth;
    int[] parm = new int[12];
    short[] serial = new short[82];
    float[] Az_dec = new float[22];
    int ptr_Az;
    IntegerPointer t0_first = new IntegerPointer();
    float[] pst_out = new float[80];
    int voicing;
    IntegerPointer sf_voic = new IntegerPointer(0);
    DecLD8K decLD = new DecLD8K();
    PostFil postFil = new PostFil();
    PostPro postPro = new PostPro();
    private byte[][] subFrames = new byte[10][10];
    private int framesCount;

    public G729ADecoder() {
        for(int i = 0; i < 10; ++i) {
            this.synth_buf[i] = 0.0F;
        }

        this.synth = 10;
        this.decLD.init_decod_ld8k();
        this.postFil.init_post_filter();
        this.postPro.init_post_process();
        this.voicing = 60;
    }

    public Format getSupportedInputFormat() {
        return g729;
    }

    public Format getSupportedOutputFormat() {
        return linear;
    }

    public Frame process(Frame frame) {
        Frame res = null;
        byte[] data = frame.getData();
        if (data.length != 0 && data.length <= 100 && data.length % 10 == 0) {
            this.circular.addData(data);
            byte[] speechWindow = this.circular.getData(data.length);
            byte[] resultBytes = null;
            byte[] transcodedBytes = null;
            if (speechWindow != null) {
                int q;
                for(q = 0; q < speechWindow.length; ++q) {
                    this.subFrames[q / 10][q % 10] = speechWindow[q];
                }

                res = Memory.allocate(speechWindow.length * 16);
                res.setLength(speechWindow.length * 16);
                resultBytes = res.getData();
                this.framesCount = speechWindow.length / 10;

                for(q = 0; q < this.framesCount; ++q) {
                    transcodedBytes = this.process(this.subFrames[q]);

                    for(int k = 0; k < transcodedBytes.length; ++k) {
                        resultBytes[q * 160 + k] = transcodedBytes[k];
                    }
                }
            } else {
                res = Memory.allocate(frame.getLength());
                res.setLength(0);
                resultBytes = new byte[0];
            }

            res.setOffset(0);
            res.setTimestamp(frame.getTimestamp());
            res.setDuration(frame.getDuration());
            res.setSequenceNumber(frame.getSequenceNumber());
            res.setEOM(frame.isEOM());
            res.setFormat(linear);
            return res;
        } else {
            throw new RuntimeException("Invalid frame size!");
        }
    }

    public byte[] process(byte[] media) {
        this.serial = Bits.fromRealBits(media);
        ++this.frame;
        Bits.bits2prm_ld8k(this.serial, 2, this.parm, 1);
        this.parm[0] = 0;

        int i;
        for(i = 2; i < 82; ++i) {
            if (this.serial[i] == 0) {
                this.parm[0] = 1;
            }
        }

        this.parm[4] = PParity.check_parity_pitch(this.parm[3], this.parm[4]);
        this.decLD.decod_ld8k(this.parm, 0, this.voicing, this.synth_buf, this.synth, this.Az_dec, this.t0_first);
        this.voicing = 0;
        this.ptr_Az = 0;

        for(i = 0; i < 80; i += 40) {
            this.postFil.post(this.t0_first.value, this.synth_buf, this.synth + i, this.Az_dec, this.ptr_Az, this.pst_out, i, this.sf_voic);
            if (this.sf_voic.value != 0) {
                this.voicing = this.sf_voic.value;
            }

            this.ptr_Az += 11;
        }

        Util.copy(this.synth_buf, 80, this.synth_buf, 0, 10);
        this.postPro.post_process(this.pst_out, 80);
        return Util.floatArrayToByteArray(this.pst_out, 80);
    }
}
