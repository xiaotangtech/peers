package net.sourceforge.peers.g729.spi.format;

public class VideoFormat extends Format implements Cloneable {
    private int frameRate;

    protected VideoFormat(EncodingName name) {
        super(name);
    }

    protected VideoFormat(EncodingName name, int frameRate) {
        super(name);
        this.frameRate = frameRate;
    }

    protected VideoFormat(String name) {
        super(new EncodingName(name));
    }

    protected VideoFormat(String name, int frameRate) {
        super(new EncodingName(name));
        this.frameRate = frameRate;
    }

    public int getFrameRate() {
        return this.frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public VideoFormat clone() {
        return new VideoFormat(this.getName().clone(), this.frameRate);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AudioFormat[");
        builder.append(this.getName().toString());
        builder.append(",");
        builder.append(this.frameRate);
        builder.append("]");
        return builder.toString();
    }
}