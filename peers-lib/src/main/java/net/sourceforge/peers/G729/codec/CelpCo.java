package net.sourceforge.peers.g729.codec;

public class CelpCo {
    int extra;

    public CelpCo() {
    }

    int ACELP_codebook(float[] x, float[] h, int t0, float pitch_sharp, int i_subfr, float[] code, float[] y, IntegerPointer sign) {
        float[] dn = new float[40];
        float[] rr = new float[616];
        int i;
        if (t0 < 40) {
            for(i = t0; i < 40; ++i) {
                h[i] += pitch_sharp * h[i - t0];
            }
        }

        cor_h(h, rr);
        CorFunc.cor_h_x(h, x, dn);
        int index = this.d4i40_17(dn, rr, h, code, y, sign, i_subfr);
        if (t0 < 40) {
            for(i = t0; i < 40; ++i) {
                code[i] += pitch_sharp * code[i - t0];
            }
        }

        return index;
    }

    static void cor_h(float[] h, float[] rr) {
        int rri0i0 = 0;
        int rri1i1 = rri0i0 + 8;
        int rri2i2 = rri1i1 + 8;
        int rri3i3 = rri2i2 + 8;
        int rri4i4 = rri3i3 + 8;
        int rri0i1 = rri4i4 + 8;
        int rri0i2 = rri0i1 + 64;
        int rri0i3 = rri0i2 + 64;
        int rri0i4 = rri0i3 + 64;
        int rri1i2 = rri0i4 + 64;
        int rri1i3 = rri1i2 + 64;
        int rri1i4 = rri1i3 + 64;
        int rri2i3 = rri1i4 + 64;
        int rri2i4 = rri2i3 + 64;
        int p0 = rri0i0 + 8 - 1;
        int p1 = rri1i1 + 8 - 1;
        int p2 = rri2i2 + 8 - 1;
        int p3 = rri3i3 + 8 - 1;
        int p4 = rri4i4 + 8 - 1;
        int ptr_h1 = 0;
        float cor = 0.0F;

        int i;
        for(i = 0; i < 8; ++i) {
            cor += h[ptr_h1] * h[ptr_h1];
            ++ptr_h1;
            rr[p4--] = cor;
            cor += h[ptr_h1] * h[ptr_h1];
            ++ptr_h1;
            rr[p3--] = cor;
            cor += h[ptr_h1] * h[ptr_h1];
            ++ptr_h1;
            rr[p2--] = cor;
            cor += h[ptr_h1] * h[ptr_h1];
            ++ptr_h1;
            rr[p1--] = cor;
            cor += h[ptr_h1] * h[ptr_h1];
            ++ptr_h1;
            rr[p0--] = cor;
        }

        int l_fin_sup = 63;
        int l_fin_inf = l_fin_sup - 1;
        int ldec = 9;
        int ptr_hd = 0;
        int ptr_hf = ptr_hd + 1;

        int ptr_h2;
        int k;
        for(k = 0; k < 8; ++k) {
            p3 = rri2i3 + l_fin_sup;
            p2 = rri1i2 + l_fin_sup;
            p1 = rri0i1 + l_fin_sup;
            p0 = rri0i4 + l_fin_inf;
            cor = 0.0F;
            ptr_h1 = ptr_hd;
            ptr_h2 = ptr_hf;

            for(i = k + 1; i < 8; ++i) {
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p3] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p2] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p1] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p0] = cor;
                p3 -= ldec;
                p2 -= ldec;
                p1 -= ldec;
                p0 -= ldec;
            }

            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p3] = cor;
            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p2] = cor;
            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p1] = cor;
            l_fin_sup -= 8;
            --l_fin_inf;
            ptr_hf += 5;
        }

        ptr_hd = 0;
        ptr_hf = ptr_hd + 2;
        l_fin_sup = 63;
        l_fin_inf = l_fin_sup - 1;

        for(k = 0; k < 8; ++k) {
            p4 = rri2i4 + l_fin_sup;
            p3 = rri1i3 + l_fin_sup;
            p2 = rri0i2 + l_fin_sup;
            p1 = rri1i4 + l_fin_inf;
            p0 = rri0i3 + l_fin_inf;
            cor = 0.0F;
            ptr_h1 = ptr_hd;
            ptr_h2 = ptr_hf;

            for(i = k + 1; i < 8; ++i) {
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p4] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p3] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p2] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p1] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p0] = cor;
                p4 -= ldec;
                p3 -= ldec;
                p2 -= ldec;
                p1 -= ldec;
                p0 -= ldec;
            }

            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p4] = cor;
            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p3] = cor;
            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p2] = cor;
            l_fin_sup -= 8;
            --l_fin_inf;
            ptr_hf += 5;
        }

        ptr_hd = 0;
        ptr_hf = ptr_hd + 3;
        l_fin_sup = 63;
        l_fin_inf = l_fin_sup - 1;

        for(k = 0; k < 8; ++k) {
            p4 = rri1i4 + l_fin_sup;
            p3 = rri0i3 + l_fin_sup;
            p2 = rri2i4 + l_fin_inf;
            p1 = rri1i3 + l_fin_inf;
            p0 = rri0i2 + l_fin_inf;
            ptr_h1 = ptr_hd;
            ptr_h2 = ptr_hf;
            cor = 0.0F;

            for(i = k + 1; i < 8; ++i) {
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p4] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p3] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p2] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p1] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p0] = cor;
                p4 -= ldec;
                p3 -= ldec;
                p2 -= ldec;
                p1 -= ldec;
                p0 -= ldec;
            }

            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p4] = cor;
            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p3] = cor;
            l_fin_sup -= 8;
            --l_fin_inf;
            ptr_hf += 5;
        }

        ptr_hd = 0;
        ptr_hf = ptr_hd + 4;
        l_fin_sup = 63;
        l_fin_inf = l_fin_sup - 1;

        for(k = 0; k < 8; ++k) {
            p3 = rri0i4 + l_fin_sup;
            p2 = rri2i3 + l_fin_inf;
            p1 = rri1i2 + l_fin_inf;
            p0 = rri0i1 + l_fin_inf;
            ptr_h1 = ptr_hd;
            ptr_h2 = ptr_hf;
            cor = 0.0F;

            for(i = k + 1; i < 8; ++i) {
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p3] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p2] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p1] = cor;
                cor += h[ptr_h1] * h[ptr_h2];
                ++ptr_h1;
                ++ptr_h2;
                rr[p0] = cor;
                p3 -= ldec;
                p2 -= ldec;
                p1 -= ldec;
                p0 -= ldec;
            }

            cor += h[ptr_h1] * h[ptr_h2];
            ++ptr_h1;
            ++ptr_h2;
            rr[p3] = cor;
            l_fin_sup -= 8;
            --l_fin_inf;
            ptr_hf += 5;
        }

    }

    int d4i40_17(float[] dn, float[] rr, float[] h, float[] cod, float[] y, IntegerPointer signs, int i_subfr) {
        float[] p_sign = new float[40];
        int rri0i0 = 0;
        int rri1i1 = rri0i0 + 8;
        int rri2i2 = rri1i1 + 8;
        int rri3i3 = rri2i2 + 8;
        int rri4i4 = rri3i3 + 8;
        int rri0i1 = rri4i4 + 8;
        int rri0i2 = rri0i1 + 64;
        int rri0i3 = rri0i2 + 64;
        int rri0i4 = rri0i3 + 64;
        int rri1i2 = rri0i4 + 64;
        int rri1i3 = rri1i2 + 64;
        int rri1i4 = rri1i3 + 64;
        int rri2i3 = rri1i4 + 64;
        int rri2i4 = rri2i3 + 64;
        if (i_subfr == 0) {
            this.extra = 30;
        }

        int i;
        for(i = 0; i < 40; ++i) {
            if (dn[i] >= 0.0F) {
                p_sign[i] = 1.0F;
            } else {
                p_sign[i] = -1.0F;
                dn[i] = -dn[i];
            }
        }

        float average = dn[0] + dn[1] + dn[2];
        float max0 = dn[0];
        float max1 = dn[1];
        float max2 = dn[2];

        for(i = 5; i < 40; i += 5) {
            average += dn[i] + dn[i + 1] + dn[i + 2];
            if (dn[i] > max0) {
                max0 = dn[i];
            }

            if (dn[i + 1] > max1) {
                max1 = dn[i + 1];
            }

            if (dn[i + 2] > max2) {
                max2 = dn[i + 2];
            }
        }

        max0 += max1 + max2;
        average *= 0.125F;
        float thres = average + (max0 - average) * 0.4F;
        int ptr_ri0i1 = rri0i1;
        int ptr_ri0i2 = rri0i2;
        int ptr_ri0i3 = rri0i3;
        int ptr_ri0i4 = rri0i4;

        int i0;
        int i1;
        for(i0 = 0; i0 < 40; i0 += 5) {
            for(i1 = 1; i1 < 40; i1 += 5) {
                rr[ptr_ri0i1] *= p_sign[i0] * p_sign[i1];
                ++ptr_ri0i1;
                rr[ptr_ri0i2] *= p_sign[i0] * p_sign[i1 + 1];
                ++ptr_ri0i2;
                rr[ptr_ri0i3] *= p_sign[i0] * p_sign[i1 + 2];
                ++ptr_ri0i3;
                rr[ptr_ri0i4] *= p_sign[i0] * p_sign[i1 + 3];
                ++ptr_ri0i4;
            }
        }

        int ptr_ri1i2 = rri1i2;
        int ptr_ri1i3 = rri1i3;
        int ptr_ri1i4 = rri1i4;

        int i2;
        for(i1 = 1; i1 < 40; i1 += 5) {
            for(i2 = 2; i2 < 40; i2 += 5) {
                rr[ptr_ri1i2] *= p_sign[i1] * p_sign[i2];
                ++ptr_ri1i2;
                rr[ptr_ri1i3] *= p_sign[i1] * p_sign[i2 + 1];
                ++ptr_ri1i3;
                rr[ptr_ri1i4] *= p_sign[i1] * p_sign[i2 + 2];
                ++ptr_ri1i4;
            }
        }

        int ptr_ri2i3 = rri2i3;
        int ptr_ri2i4 = rri2i4;

        int i3;
        for(i2 = 2; i2 < 40; i2 += 5) {
            for(i3 = 3; i3 < 40; i3 += 5) {
                rr[ptr_ri2i3] *= p_sign[i2] * p_sign[i3];
                ++ptr_ri2i3;
                rr[ptr_ri2i4] *= p_sign[i2] * p_sign[i3 + 1];
                ++ptr_ri2i4;
            }
        }

        int ip0 = 0;
        int ip1 = 1;
        int ip2 = 2;
        int ip3 = 3;
        float psc = 0.0F;
        float alpha = 1000000.0F;
        int time = 75 + this.extra;
        int ptr_ri0i0 = rri0i0;
        ptr_ri0i1 = rri0i1;
        ptr_ri0i2 = rri0i2;
        ptr_ri0i3 = rri0i3;
        ptr_ri0i4 = rri0i4;

        label217:
        for(i0 = 0; i0 < 40; i0 += 5) {
            float ps0 = dn[i0];
            float alp0 = rr[ptr_ri0i0++];
            int ptr_ri1i1 = rri1i1;
            ptr_ri1i2 = rri1i2;
            ptr_ri1i3 = rri1i3;
            ptr_ri1i4 = rri1i4;

            for(i1 = 1; i1 < 40; i1 += 5) {
                float ps1 = ps0 + dn[i1];
                float alp1 = alp0 + rr[ptr_ri1i1++] + 2.0F * rr[ptr_ri0i1++];
                int ptr_ri2i2 = rri2i2;
                ptr_ri2i3 = rri2i3;
                ptr_ri2i4 = rri2i4;

                for(i2 = 2; i2 < 40; i2 += 5) {
                    float ps2 = ps1 + dn[i2];
                    float alp2 = alp1 + rr[ptr_ri2i2++] + 2.0F * (rr[ptr_ri0i2++] + rr[ptr_ri1i2++]);
                    if (ps2 <= thres) {
                        ptr_ri2i3 += 8;
                        ptr_ri2i4 += 8;
                    } else {
                        int ptr_ri3i3 = rri3i3;

                        float ps3;
                        float alp3;
                        float ps3c;
                        for(i3 = 3; i3 < 40; i3 += 5) {
                            ps3 = ps2 + dn[i3];
                            alp3 = alp2 + rr[ptr_ri3i3++] + 2.0F * (rr[ptr_ri1i3++] + rr[ptr_ri0i3++] + rr[ptr_ri2i3++]);
                            ps3c = ps3 * ps3;
                            if (ps3c * alpha > psc * alp3) {
                                psc = ps3c;
                                alpha = alp3;
                                ip0 = i0;
                                ip1 = i1;
                                ip2 = i2;
                                ip3 = i3;
                            }
                        }

                        ptr_ri0i3 -= 8;
                        ptr_ri1i3 -= 8;
                        int ptr_ri4i4 = rri4i4;

                        for(i3 = 4; i3 < 40; i3 += 5) {
                            ps3 = ps2 + dn[i3];
                            alp3 = alp2 + rr[ptr_ri4i4++] + 2.0F * (rr[ptr_ri1i4++] + rr[ptr_ri0i4++] + rr[ptr_ri2i4++]);
                            ps3c = ps3 * ps3;
                            if (ps3c * alpha > psc * alp3) {
                                psc = ps3c;
                                alpha = alp3;
                                ip0 = i0;
                                ip1 = i1;
                                ip2 = i2;
                                ip3 = i3;
                            }
                        }

                        ptr_ri0i4 -= 8;
                        ptr_ri1i4 -= 8;
                        --time;
                        if (time <= 0) {
                            break label217;
                        }
                    }
                }

                ptr_ri0i2 -= 8;
                ptr_ri1i3 += 8;
                ptr_ri1i4 += 8;
            }

            ptr_ri0i2 += 8;
            ptr_ri0i3 += 8;
            ptr_ri0i4 += 8;
        }

        this.extra = time;

        for(i = 0; i < 40; ++i) {
            cod[i] = 0.0F;
        }

        cod[ip0] = p_sign[ip0];
        cod[ip1] = p_sign[ip1];
        cod[ip2] = p_sign[ip2];
        cod[ip3] = p_sign[ip3];

        for(i = 0; i < 40; ++i) {
            y[i] = 0.0F;
        }

        int j;
        if (p_sign[ip0] > 0.0F) {
            i = ip0;

            for(j = 0; i < 40; ++j) {
                y[i] = h[j];
                ++i;
            }
        } else {
            i = ip0;

            for(j = 0; i < 40; ++j) {
                y[i] = -h[j];
                ++i;
            }
        }

        if (p_sign[ip1] > 0.0F) {
            i = ip1;

            for(j = 0; i < 40; ++j) {
                y[i] += h[j];
                ++i;
            }
        } else {
            i = ip1;

            for(j = 0; i < 40; ++j) {
                y[i] -= h[j];
                ++i;
            }
        }

        if (p_sign[ip2] > 0.0F) {
            i = ip2;

            for(j = 0; i < 40; ++j) {
                y[i] += h[j];
                ++i;
            }
        } else {
            i = ip2;

            for(j = 0; i < 40; ++j) {
                y[i] -= h[j];
                ++i;
            }
        }

        if (p_sign[ip3] > 0.0F) {
            i = ip3;

            for(j = 0; i < 40; ++j) {
                y[i] += h[j];
                ++i;
            }
        } else {
            i = ip3;

            for(j = 0; i < 40; ++j) {
                y[i] -= h[j];
                ++i;
            }
        }

        i = 0;
        if (p_sign[ip0] > 0.0F) {
            ++i;
        }

        if (p_sign[ip1] > 0.0F) {
            i += 2;
        }

        if (p_sign[ip2] > 0.0F) {
            i += 4;
        }

        if (p_sign[ip3] > 0.0F) {
            i += 8;
        }

        signs.value = i;
        ip0 /= 5;
        ip1 /= 5;
        ip2 /= 5;
        j = ip3 % 5 - 3;
        ip3 = (ip3 / 5 << 1) + j;
        i = ip0 + (ip1 << 3) + (ip2 << 6) + (ip3 << 9);
        return i;
    }
}