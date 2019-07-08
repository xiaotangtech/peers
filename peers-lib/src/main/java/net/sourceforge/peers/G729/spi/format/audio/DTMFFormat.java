package net.sourceforge.peers.g729.spi.format.audio;

import net.sourceforge.peers.g729.spi.format.AudioFormat;
import net.sourceforge.peers.g729.spi.format.EncodingName;
import net.sourceforge.peers.g729.spi.utils.Text;

public class DTMFFormat extends AudioFormat {
    public DTMFFormat() {
        super(new EncodingName("telephone-event"));
        this.setSampleRate(8000);
        this.setOptions(new Text("0-15"));
    }
}
