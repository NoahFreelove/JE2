package JE.Resources;

import org.joml.Vector2i;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class ResourceBundle implements Serializable {

    // General
    public String filepath;


    // Texture
    public Vector2i imageSize;
    public ByteBuffer imageData;

    // Sound
    public ShortBuffer soundData;
    public int format;
    public int sampleRate;
    public int channels;
    public boolean isPlaying = false;

}
