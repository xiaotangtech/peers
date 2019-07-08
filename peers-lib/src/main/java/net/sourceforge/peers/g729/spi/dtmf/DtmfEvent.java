package net.sourceforge.peers.g729.spi.dtmf;

import net.sourceforge.peers.g729.spi.listener.Event;

public interface DtmfEvent extends Event<DtmfDetector> {
    int DTMF_0 = 0;
    int DTMF_1 = 1;
    int DTMF_2 = 2;
    int DTMF_3 = 3;
    int DTMF_4 = 4;
    int DTMF_5 = 5;
    int DTMF_6 = 6;
    int DTMF_7 = 7;
    int DTMF_8 = 8;
    int DTMF_9 = 9;
    int DTMF_A = 10;
    int DTMF_B = 11;
    int DTMF_C = 12;
    int DTMF_D = 13;
    int DTMF_HASH = 14;
    int DTMF_STAR = 15;

    int getVolume();

    String getTone();
}