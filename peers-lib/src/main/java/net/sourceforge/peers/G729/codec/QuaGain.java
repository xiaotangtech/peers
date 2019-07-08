package net.sourceforge.peers.G729.codec;

public class QuaGain {
    float[] past_qua_en = new float[]{-14.0F, -14.0F, -14.0F, -14.0F};

    public QuaGain() {
    }

    int qua_gain(float[] code, float[] g_coeff, int l_subfr, FloatPointer gain_pit, FloatPointer gain_code, int tameflag) {
        int index1 = 0;
        int index2 = 0;
        IntegerPointer cand1 = new IntegerPointer(0);
        IntegerPointer cand2 = new IntegerPointer(0);
        FloatPointer gcode0 = new FloatPointer(0.0F);
        float dist = 0.0F;
        float dist_min = 0.0F;
        float g_pitch = 0.0F;
        float g_code = 0.0F;
        float[] best_gain = new float[2];
        GainPred.gain_predict(this.past_qua_en, code, l_subfr, gcode0);
        float tmp = -1.0F / (4.0F * g_coeff[0] * g_coeff[2] - g_coeff[4] * g_coeff[4]);
        best_gain[0] = (2.0F * g_coeff[2] * g_coeff[1] - g_coeff[3] * g_coeff[4]) * tmp;
        best_gain[1] = (2.0F * g_coeff[0] * g_coeff[3] - g_coeff[1] * g_coeff[4]) * tmp;
        if (tameflag == 1 && best_gain[0] > 0.94F) {
            best_gain[0] = 0.94F;
        }

        gbk_presel(best_gain, cand1, cand2, gcode0.value);
        dist_min = 1.0E38F;
        int i;
        int j;
        if (tameflag == 1) {
            for(i = 0; (float)i < 4.0F; ++i) {
                for(j = 0; (float)j < 8.0F; ++j) {
                    g_pitch = TabLD8k.gbk1[cand1.value + i][0] + TabLD8k.gbk2[cand2.value + j][0];
                    if (g_pitch < 0.9999F) {
                        g_code = gcode0.value * (TabLD8k.gbk1[cand1.value + i][1] + TabLD8k.gbk2[cand2.value + j][1]);
                        dist = g_pitch * g_pitch * g_coeff[0] + g_pitch * g_coeff[1] + g_code * g_code * g_coeff[2] + g_code * g_coeff[3] + g_pitch * g_code * g_coeff[4];
                        if (dist < dist_min) {
                            dist_min = dist;
                            index1 = cand1.value + i;
                            index2 = cand2.value + j;
                        }
                    }
                }
            }
        } else {
            for(i = 0; (float)i < 4.0F; ++i) {
                for(j = 0; (float)j < 8.0F; ++j) {
                    g_pitch = TabLD8k.gbk1[cand1.value + i][0] + TabLD8k.gbk2[cand2.value + j][0];
                    g_code = gcode0.value * (TabLD8k.gbk1[cand1.value + i][1] + TabLD8k.gbk2[cand2.value + j][1]);
                    dist = g_pitch * g_pitch * g_coeff[0] + g_pitch * g_coeff[1] + g_code * g_code * g_coeff[2] + g_code * g_coeff[3] + g_pitch * g_code * g_coeff[4];
                    if (dist < dist_min) {
                        dist_min = dist;
                        index1 = cand1.value + i;
                        index2 = cand2.value + j;
                    }
                }
            }
        }

        gain_pit.value = TabLD8k.gbk1[index1][0] + TabLD8k.gbk2[index2][0];
        g_code = TabLD8k.gbk1[index1][1] + TabLD8k.gbk2[index2][1];
        gain_code.value = g_code * gcode0.value;
        GainPred.gain_update(this.past_qua_en, g_code);
        return TabLD8k.map1[index1] * 16 + TabLD8k.map2[index2];
    }

    public static void gbk_presel(float[] best_gain, IntegerPointer cand1, IntegerPointer cand2, float gcode0) {
        float x = (best_gain[1] - (TabLD8k.coef[0][0] * best_gain[0] + TabLD8k.coef[1][1]) * gcode0) * -0.032623F;
        float y = (TabLD8k.coef[1][0] * (-TabLD8k.coef[0][1] + best_gain[0] * TabLD8k.coef[0][0]) * gcode0 - TabLD8k.coef[0][0] * best_gain[1]) * -0.032623F;
        if (gcode0 > 0.0F) {
            cand1.value = 0;

            while(y > TabLD8k.thr1[cand1.value] * gcode0) {
                cand1.value = cand1.value + 1;
                if ((float)cand1.value >= 4.0F) {
                    break;
                }
            }

            cand2.value = 0;

            while(x > TabLD8k.thr2[cand2.value] * gcode0) {
                cand2.value = cand2.value + 1;
                if ((float)cand2.value >= 8.0F) {
                    break;
                }
            }
        } else {
            cand1.value = 0;

            Integer var7;
            Integer var8;
            while(y < TabLD8k.thr1[cand1.value] * gcode0) {
                var7 = cand1.value;
                var8 = cand1.value = cand1.value + 1;
                if ((float)cand1.value >= 4.0F) {
                    break;
                }
            }

            cand2.value = 0;

            while(x < TabLD8k.thr2[cand2.value] * gcode0) {
                var7 = cand2.value;
                var8 = cand2.value = cand2.value + 1;
                if ((float)cand2.value >= 8.0F) {
                    break;
                }
            }
        }

    }
}
