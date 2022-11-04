package JE.Objects.Components;

import JE.IO.ImageProcessor;
import JE.Manager;
import JE.Rendering.Renderer;
import JE.Rendering.VAO;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL30.glTexParameterIi;

public class SpriteRenderer extends Renderer {

    public int generatedTexture = -1;
    protected ByteBuffer texture;

    public SpriteRenderer(){
    }

    public SpriteRenderer(VAO vao){
        super(vao);
    }


    @Override
    public void update() {

    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }

    @Override
    public void Render(Transform t){
        if(generatedTexture <=-1)
            return;
        int textureID = glGetUniformLocation(vao.shaderProgram.programID, "JE_Texture");
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, generatedTexture);
        glUniform1i(textureID, 0);

        super.Render(t);

    }

    public void setTexture(String texturePath) {
        setTexture(ImageProcessor.ProcessImage(texturePath));
    }

        public void setTexture(ByteBuffer texture){
        this.texture = texture;

        Runnable r = () -> {
            this.generatedTexture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, generatedTexture);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, 64, 64, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glGenerateMipmap(GL_TEXTURE_2D);
            System.out.println("Texture ID: " + generatedTexture);
        };
        Manager.QueueGLFunction(r);
    }

    public void CreateTexture(){}

}
