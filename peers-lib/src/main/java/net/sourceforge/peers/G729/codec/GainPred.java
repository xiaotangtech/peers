package net.sourceforge.peers.g729.codec;

public class GainPred {
    public GainPred() {
    }

    public static void gain_predict(float[] past_qua_en, float[] code, int l_subfr, FloatPointer gcode0) {
        float pred_code = 36.0F;
        float ener_code = 0.01F;

        int i;
        for(i = 0; i < l_subfr; ++i) {
            ener_code += code[i] * code[i];
        }

        ener_code = 10.0F * (float)Math.log10((double)(ener_code / (float)l_subfr));
        pred_code -= ener_code;

        for(i = 0; i < 4; ++i) {
            pred_code += TabLD8k.pred[i] * past_qua_en[i];
        }

        gcode0.value = pred_code;
        gcode0.value = (float)Math.pow(10.0D, (double)gcode0.value / 20.0D);
    }

    public static void gain_update(float[] past_qua_en, float g_code) {
        for(int i = 3; i > 0; --i) {
            past_qua_en[i] = past_qua_en[i - 1];
        }

        past_qua_en[0] = 20.0F * (float)Math.log10((double)g_code);
    }

    public static void gain_update_erasure(float[] past_qua_en) {
        float av_pred_en = 0.0F;

        int i;
        for(i = 0; i < 4; ++i) {
            av_pred_en += past_qua_en[i];
        }

        av_pred_en = av_pred_en * 0.25F - 4.0F;
        if (av_pred_en < -14.0F) {
            av_pred_en = -14.0F;
        }

        for(i = 3; i > 0; --i) {
            past_qua_en[i] = past_qua_en[i - 1];
        }

        past_qua_en[0] = av_pred_en;
    }
}
