package net.sourceforge.peers.g729.spi.format;

import net.sourceforge.peers.g729.spi.format.audio.DTMFFormat;
import net.sourceforge.peers.g729.spi.utils.Text;

public class FormatFactory {
    private static Text DTMF = new Text("telephone-event");

    public FormatFactory() {
    }

    public static AudioFormat createAudioFormat(EncodingName name) {
        return (AudioFormat)(name.equals(DTMF) ? new DTMFFormat() : new AudioFormat(name));
    }

    public static AudioFormat createAudioFormat(EncodingName name, int sampleRate, int sampleSize, int channels) {
        AudioFormat fmt = createAudioFormat(name);
        fmt.setSampleRate(sampleRate);
        fmt.setSampleSize(sampleSize);
        fmt.setChannels(channels);
        return fmt;
    }

    public static AudioFormat createAudioFormat(String name, int sampleRate, int sampleSize, int channels) {
        AudioFormat fmt = createAudioFormat(new EncodingName(name));
        fmt.setSampleRate(sampleRate);
        fmt.setSampleSize(sampleSize);
        fmt.setChannels(channels);
        return fmt;
    }

    public static AudioFormat createAudioFormat(String name, int sampleRate) {
        AudioFormat fmt = createAudioFormat(new EncodingName(name));
        fmt.setSampleRate(sampleRate);
        return fmt;
    }

    public static VideoFormat createVideoFormat(EncodingName name, int frameRate) {
        return new VideoFormat(name, frameRate);
    }

    public static VideoFormat createVideoFormat(EncodingName name) {
        return new VideoFormat(name);
    }

    public static VideoFormat createVideoFormat(String name) {
        return new VideoFormat(name);
    }

    public static VideoFormat createVideoFormat(String name, int frameRate) {
        return new VideoFormat(name, frameRate);
    }

    public static ApplicationFormat createApplicationFormat(EncodingName name) {
        return new ApplicationFormat(name);
    }

    public static ApplicationFormat createApplicationFormat(String name) {
        return new ApplicationFormat(name);
    }
}