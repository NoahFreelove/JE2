package JE.Resources;

import org.joml.Vector2i;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class ResourceBundle implements Serializable {

    // Texture
    public Vector2i imageSize;
    public ByteBuffer imageData;

    // Sound
    public ShortBuffer soundData;
    public String filepath;
    public int format;
    public int sampleRate;
    public int channels;
    public boolean isPlaying = false;

}
