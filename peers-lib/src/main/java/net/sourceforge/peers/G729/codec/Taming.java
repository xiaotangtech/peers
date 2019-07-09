package net.sourceforge.peers.G729.codec;

public class Taming {
    float[] exc_err = new float[4];

    public Taming() {
    }

    void init_exc_err() {
        for(int i = 0; i < 4; ++i) {
            this.exc_err[i] = 1.0F;
        }

    }

    int test_err(int t0, int t0_frac) {
        int t1 = t0_frac > 0 ? t0 + 1 : t0;
        int i = t1 - 40 - 10;
        if (i < 0) {
            i = 0;
        }

        int zone1 = (int)((float)i * 0.025F);
        i = t1 + 10 - 2;
        int zone2 = (int)((float)i * 0.025F);
        float maxloc = -1.0F;
        int flag = 0;

        for(i = zone2; i >= zone1; --i) {
            if (this.exc_err[i] > maxloc) {
                maxloc = this.exc_err[i];
            }
        }

        if (maxloc > 60000.0F) {
            flag = 1;
        }

        return flag;
    }

    void update_exc_err(float gain_pit, int t0) {
        float worst = -1.0F;
        int n = t0 - 40;
        int i;
        float temp;
        if (n < 0) {
            temp = 1.0F + gain_pit * this.exc_err[0];
            if (temp > worst) {
                worst = temp;
            }

            temp = 1.0F + gain_pit * temp;
            if (temp > worst) {
                worst = temp;
            }
        } else {
            int zone1 = (int)((float)n * 0.025F);
            i = t0 - 1;
            int zone2 = (int)((float)i * 0.025F);

            for(i = zone1; i <= zone2; ++i) {
                temp = 1.0F + gain_pit * this.exc_err[i];
                if (temp > worst) {
                    worst = temp;
                }
            }
        }

        for(i = 3; i >= 1; --i) {
            this.exc_err[i] = this.exc_err[i - 1];
        }

        this.exc_err[0] = worst;
    }
}