package JE.Objects.Components;

import JE.IO.ImageProcessor;
import JE.Manager;
import JE.Rendering.Renderer;
import JE.Rendering.VAO;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL30.glTexParameterIi;

public class SpriteRenderer extends Renderer {

    public int generatedTexture = -1;
    private int textCoordBufferId;
    private FloatBuffer textCoordBuffer;

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
        if(generatedTexture <=-1) {
            super.Render(t, 0);
            return;
        }
        int textureID = glGetUniformLocation(vao.shaderProgram.programID, "JE_Texture");
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, generatedTexture);
        glUniform1i(textureID, 0);

        glUseProgram(vao.shaderProgram.programID);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL20.GL_ARRAY_BUFFER, textCoordBufferId);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        super.Render(t, textCoordBuffer.limit());
        glDisableVertexAttribArray(1);
    }

    public void setTexture(String texturePath) {
        setTexture(ImageProcessor.ProcessImage(texturePath), new Vector2i(64,64));
    }

    public void setTexture(ByteBuffer texture, Vector2i size){
        setTexture(texture, vao.vertices, size);
    }

    public void setTexture(ByteBuffer texture, Vector2f[] textCoords, Vector2i size) {
        this.texture = texture;

        Runnable r = () -> {
            this.generatedTexture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, generatedTexture);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, size.x(), size.y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

            textCoordBuffer = BufferUtils.createFloatBuffer(textCoords.length * 2);

            for (int i = 0; i < vao.vertices.length; i++) {
                textCoordBuffer.put(vao.vertices[i].x);
                textCoordBuffer.put(vao.vertices[i].y);
            }

            textCoordBuffer.flip();
            textCoordBufferId = glGenBuffers();

            glBindBuffer(GL_ARRAY_BUFFER, textCoordBufferId);
            glBufferData(GL_ARRAY_BUFFER, textCoordBuffer, GL_STATIC_DRAW);
            glGenerateMipmap(GL_TEXTURE_2D);
        };
        Manager.QueueGLFunction(r);
    }
}
