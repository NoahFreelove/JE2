package JE.Resources;

import org.joml.Vector2i;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class ResourceBundle implements Serializable {

    // General
    public transient String filepath;

    // Texture
    public transient Vector2i imageSize;
    public transient ByteBuffer imageData;

    // Sound
    public transient ShortBuffer soundData;
    public transient int format;
    public transient int sampleRate;
    public transient int channels;
    public transient boolean isPlaying = false;

}
