package net.sourceforge.peers.g729.codec;

public class DecGain {
    float[] past_qua_en = new float[]{-14.0F, -14.0F, -14.0F, -14.0F};

    public DecGain() {
    }

    public void dec_gain(int index, float[] code, int l_subfr, int bfi, FloatPointer gain_pit, FloatPointer gain_code) {
        FloatPointer gcode0 = new FloatPointer();
        if (bfi != 0) {
            gain_pit.value = gain_pit.value * 0.9F;
            if (gain_pit.value > 0.9F) {
                gain_pit.value = 0.9F;
            }

            gain_code.value = gain_code.value * 0.98F;
            GainPred.gain_update_erasure(this.past_qua_en);
        } else {
            int index1 = TabLD8k.imap1[index / 16];
            int index2 = TabLD8k.imap2[index % 16];
            gain_pit.value = TabLD8k.gbk1[index1][0] + TabLD8k.gbk2[index2][0];
            GainPred.gain_predict(this.past_qua_en, code, l_subfr, gcode0);
            float g_code = TabLD8k.gbk1[index1][1] + TabLD8k.gbk2[index2][1];
            gain_code.value = g_code * gcode0.value;
            GainPred.gain_update(this.past_qua_en, g_code);
        }
    }
}
