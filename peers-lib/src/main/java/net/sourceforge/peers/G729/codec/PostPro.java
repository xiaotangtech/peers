package net.sourceforge.peers.G729.codec;

public class PostPro {
    float x0;
    float x1;
    float y1;
    float y2;

    public PostPro() {
    }

    public void init_post_process() {
        this.x0 = this.x1 = 0.0F;
        this.y2 = this.y1 = 0.0F;
    }

    public void post_process(float[] signal, int lg) {
        for(int i = 0; i < lg; ++i) {
            float x2 = this.x1;
            this.x1 = this.x0;
            this.x0 = signal[i];
            float y0 = this.y1 * TabLD8k.a100[1] + this.y2 * TabLD8k.a100[2] + this.x0 * TabLD8k.b100[0] + this.x1 * TabLD8k.b100[1] + x2 * TabLD8k.b100[2];
            signal[i] = y0;
            this.y2 = this.y1;
            this.y1 = y0;
        }

    }
}
