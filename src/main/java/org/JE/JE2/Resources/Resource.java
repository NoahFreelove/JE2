package org.JE.JE2.Resources;

import org.JE.JE2.IO.FileInput.ImageProcessor;
import org.JE.JE2.IO.FileInput.AudioProcessor;
import org.JE.JE2.IO.Logging.Errors.ResourceError;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Resources.Bundles.AudioBundle;
import org.JE.JE2.Resources.Bundles.DefaultBundle;
import org.JE.JE2.Resources.Bundles.ResourceBundle;
import org.JE.JE2.Resources.Bundles.TextureBundle;
import org.joml.Vector2i;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class Resource implements Serializable {
    private transient ResourceBundle bundle;

    public final String name;
    public final ResourceType type;

    public Resource(String name, byte[] data, ResourceType type) {
        this.name = name;
        this.type = type;
        bundle = new DefaultBundle();
        switch (type) {
            case TEXTURE -> {
                bundle = ImageProcessor.processImage(data, true);
                ResourceManager.textures.add(this);
            }
            case SOUND -> {
                bundle = AudioProcessor.ProcessSound(data);
                ResourceManager.sounds.add(this);
            }
        }
    }

    public Resource(String name, String path, ResourceType type) {
        this.name = name;
        this.type = type;
        bundle = new DefaultBundle();
        switch (type) {
            case TEXTURE -> {
                bundle = ImageProcessor.processImage(path);
                ResourceManager.textures.add(this);
            }
            case SOUND -> {
                bundle = AudioProcessor.ProcessSound(path);
                ResourceManager.sounds.add(this);
            }
        }
    }

    public Resource(String name, ResourceBundle rb, ResourceType type){
        this.name = name;
        this.type = type;
        bundle = rb;
        switch (type) {
            case TEXTURE -> ResourceManager.textures.add(this);
            case SOUND -> ResourceManager.sounds.add(this);
        }
    }

    public Resource(String name, ByteBuffer buffer, Vector2i size, ResourceType type)
    {
        this.name = name;
        this.type = type;
        switch (type)
        {
            case TEXTURE -> {
                this.bundle = new TextureBundle(size,buffer);
                ResourceManager.textures.add(this);
            }
        }
    }
    public TextureBundle getTextureBundle(){
        if(type == ResourceType.TEXTURE)
            return (TextureBundle) bundle;
        Logger.log(new ResourceError("Resource is not a texture!"));
        return null;
    }
    public AudioBundle getAudioBundle(){
        if(type == ResourceType.SOUND)
            return (AudioBundle) bundle;
        Logger.log(new ResourceError("Resource is not a sound!"));
        return null;
    }
    public ResourceBundle getBundle(){
        return bundle;
    }

    public boolean free(){
        if(bundle.isLoaded())
        {
            bundle.free();
            return true;
        }
        Logger.log(new ResourceError("Resource has not been loaded yet!"));
        return false;
    }
}
