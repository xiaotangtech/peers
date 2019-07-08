package net.sourceforge.peers.G729.codec;

public class PreProc {
    float x0;
    float x1;
    float y1;
    float y2;

    public PreProc() {
    }

    public void init_pre_process() {
        this.x0 = this.x1 = 0.0F;
        this.y2 = this.y1 = 0.0F;
    }

    public void pre_process(float[] signal, int lg) {
        for(int i = 0; i < lg; ++i) {
            float x2 = this.x1;
            this.x1 = this.x0;
            this.x0 = signal[i];
            float y0 = this.y1 * TabLD8k.a140[1] + this.y2 * TabLD8k.a140[2] + this.x0 * TabLD8k.b140[0] + this.x1 * TabLD8k.b140[1] + x2 * TabLD8k.b140[2];
            signal[i] = y0;
            this.y2 = this.y1;
            this.y1 = y0;
        }

    }
}
