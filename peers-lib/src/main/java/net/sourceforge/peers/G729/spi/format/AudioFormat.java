package net.sourceforge.peers.G729.spi.format;

public class AudioFormat extends Format implements Cloneable {
    private int sampleRate;
    private int sampleSize = -1;
    private int channels = 1;

    protected AudioFormat(EncodingName name) {
        super(name);
    }

    private AudioFormat(EncodingName name, int sampleRate, int sampleSize, int channels) {
        super(name);
        this.sampleRate = sampleRate;
        this.sampleSize = sampleSize;
        this.channels = channels;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public int getChannels() {
        return this.channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public AudioFormat clone() {
        AudioFormat f = new AudioFormat(this.getName().clone(), this.sampleRate, this.sampleSize, this.channels);
        f.setOptions(this.getOptions());
        return f;
    }

    public boolean matches(Format other) {
        if (!super.matches(other)) {
            return false;
        } else {
            AudioFormat f = (AudioFormat)other;
            if (f.sampleRate != this.sampleRate) {
                return false;
            } else {
                return f.channels == this.channels;
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AudioFormat[");
        builder.append(this.getName().toString());
        builder.append(",");
        builder.append(this.sampleRate);
        if (this.sampleSize > 0) {
            builder.append(",");
            builder.append(this.sampleSize);
        }

        if (this.channels == 1) {
            builder.append(",mono");
        } else if (this.channels == 2) {
            builder.append(",stereo");
        } else {
            builder.append(",");
            builder.append(this.channels);
        }

        builder.append("]");
        return builder.toString();
    }
}