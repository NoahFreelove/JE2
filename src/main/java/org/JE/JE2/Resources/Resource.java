package org.JE.JE2.Resources;

import org.JE.JE2.IO.FileInput.ImageProcessor;
import org.JE.JE2.IO.FileInput.SoundProcessor;
import org.JE.JE2.IO.Logging.Errors.ResourceError;
import org.JE.JE2.IO.Logging.Logger;
import org.joml.Vector2i;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class Resource implements Serializable {
    public transient ResourceBundle bundle;

    public final String name;
    public final ResourceType type;

    public boolean hasBeenLoaded = false;

    public Resource(String name, byte[] data, ResourceType type) {
        this.name = name;
        this.type = type;
        bundle = new ResourceBundle();
        switch (type) {
            case TEXTURE -> {
                bundle = ImageProcessor.processImage(data, true);
                ResourceManager.textures.add(this);
            }
            case SOUND -> {
                bundle = SoundProcessor.ProcessSound(data);
                ResourceManager.sounds.add(this);
            }
        }
        hasBeenLoaded = true;
    }

    public Resource(String name, String path, ResourceType type) {
        this.name = name;
        this.type = type;
        bundle = new ResourceBundle();
        switch (type) {
            case TEXTURE -> {
                bundle = ImageProcessor.processImage(path);
                ResourceManager.textures.add(this);
            }
            case SOUND -> {
                bundle = SoundProcessor.ProcessSound(path);
                ResourceManager.sounds.add(this);
            }
        }
        hasBeenLoaded = true;
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
                this.bundle = new ResourceBundle();
                this.bundle.imageData = buffer;
                this.bundle.imageSize = size;
                ResourceManager.textures.add(this);
            }
        }
        hasBeenLoaded = true;
    }

    public boolean free(){
        if(hasBeenLoaded)
        {
            switch (type) {
                case TEXTURE -> {
                    bundle.imageData.clear();
                    bundle.imageData = null;
                    bundle.imageSize = null;
                    bundle.filepath = null;
                    ResourceManager.textures.remove(this);
                }
                case SOUND -> {
                    bundle.soundData.clear();
                    bundle.soundData = null;
                    bundle.filepath = null;
                    bundle.format = 0;
                    bundle.sampleRate = 0;
                    bundle.channels = 0;
                    ResourceManager.sounds.remove(this);
                }
            }
            hasBeenLoaded = false;
            return true;
        }
        Logger.log(new ResourceError("Resource has not been loaded yet!"));

        return false;
    }
}