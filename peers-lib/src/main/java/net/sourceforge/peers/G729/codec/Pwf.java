package net.sourceforge.peers.G729.codec;

public class Pwf {
    int smooth = 1;
    float[] lar_old = new float[]{0.0F, 0.0F};

    public Pwf() {
    }

    void perc_var(float[] gamma1, float[] gamma2, float[] lsfint, float[] lsfnew, float[] r_c) {
        float[] lar = new float[4];
        int lar_new = 2;

        int i;
        for(i = 0; i < 2; ++i) {
            lar[lar_new + i] = (float)Math.log10((double)((1.0F + r_c[i]) / (1.0F - r_c[i])));
        }

        for(i = 0; i < 2; ++i) {
            lar[i] = 0.5F * (lar[lar_new + i] + this.lar_old[i]);
            this.lar_old[i] = lar[lar_new + i];
        }

        for(int k = 0; k < 2; ++k) {
            float critlar0 = lar[2 * k];
            float critlar1 = lar[2 * k + 1];
            if (this.smooth != 0) {
                if (critlar0 < -1.74F && critlar1 > 0.65F) {
                    this.smooth = 0;
                }
            } else if (critlar0 > -1.52F || critlar1 < 0.43F) {
                this.smooth = 1;
            }

            if (this.smooth != 0) {
                gamma1[k] = 0.94F;
                gamma2[k] = 0.6F;
            } else {
                gamma1[k] = 0.98F;
                float[] lsf;
                if (k == 0) {
                    lsf = lsfint;
                } else {
                    lsf = lsfnew;
                }

                float d_min = lsf[1] - lsf[0];

                for(i = 1; i < 9; ++i) {
                    float temp = lsf[i + 1] - lsf[i];
                    if (temp < d_min) {
                        d_min = temp;
                    }
                }

                gamma2[k] = -6.0F * d_min + 1.0F;
                if (gamma2[k] > 0.7F) {
                    gamma2[k] = 0.7F;
                }

                if (gamma2[k] < 0.4F) {
                    gamma2[k] = 0.4F;
                }
            }
        }

    }
}
