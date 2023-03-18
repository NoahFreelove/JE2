package org.JE.JE2.Rendering.Shaders;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.Logging.Errors.ShaderError;
import org.JE.JE2.Manager;
import org.JE.JE2.Utility.Loadable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram implements Serializable, Loadable {
    public transient int programID = -1;

    public boolean supportsLighting = false;
    public boolean supportsTextures = false;
    public transient volatile boolean attemptedCompile = false;

    public String vertex;
    public String fragment;
    public int presetIndex = 0;
    public transient boolean vertexCompileStatus;
    public transient boolean fragmentCompileStatus;
    private transient int vertexShaderID;
    private transient int fragmentShaderID;

    // TODO: future testing to see if multiple objects can shader the same shader
    /*public static final ShaderProgram defaultShader = defaultShader();
    public static final ShaderProgram spriteShader = spriteShader();
    public static final ShaderProgram lightSpriteShader = lightSpriteShader();*/

    private ShaderProgram(){}

    public static ShaderProgram invalidShader(){
        return new ShaderProgram();
    }

    public static ShaderProgram defaultShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.setDefaultShader();
        return sp;
    }
    private void setDefaultShader(){
        createShader(ShaderRegistry.DEFAULT_VERTEX, ShaderRegistry.DEFAULT_FRAGMENT);
    }
    public static ShaderProgram spriteShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.setSpriteShader();
        return sp;
    }
    private void setSpriteShader(){
        createShader(ShaderRegistry.SPRITE_VERTEX, ShaderRegistry.SPRITE_FRAGMENT);
        supportsTextures = true;
    }
    public static ShaderProgram lightSpriteShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.setLightSpriteShader();
        return sp;
    }
    private void setLightSpriteShader(){
        createShader(ShaderRegistry.LIGHTSPRITE_VERTEX, ShaderRegistry.LIGHTSPRITE_FRAGMENT);
        supportsLighting = true;
        supportsTextures = true;
    }
    public ShaderProgram(String vertexShader, String fragmentShader, boolean supportsLighting){
        createShader(vertexShader, fragmentShader);
        this.supportsLighting = supportsLighting;
    }
    public ShaderProgram(File vertexShader, File fragmentShader, boolean supportsLighting){
        createShader(vertexShader, fragmentShader);
        this.supportsLighting = supportsLighting;
    }

    @GLThread
    public static ShaderProgram ShaderProgramNow(String vert, String frag, boolean supportsLighting){
        ShaderProgram sp = new ShaderProgram();
        sp.createShaderNow(vert,frag);
        sp.supportsLighting = supportsLighting;
        return sp;
    }

    public static ShaderProgram getProgramFromIndex(int i){
        return switch (i){
            case 1 -> spriteShader();
            case 2 -> lightSpriteShader();
            default -> defaultShader();
        };
    }

    @GLThread
    public void setUniformMatrix4f(String name, FloatBuffer matrix){
        int location = glGetUniformLocation(programID, name);
        glUniformMatrix4fv(location, false, matrix);
    }

    @GLThread
    public void setUniform1f(String name, float value){
        int location = glGetUniformLocation(programID, name);
        glUniform1f(location, value);
    }

    @GLThread
    public void setUniform1i(String name, int value){
        int location = glGetUniformLocation(programID, name);
        glUniform1i(location, value);
    }

    @GLThread
    public void setUniform2f(String name, Vector2f value){
        int location = glGetUniformLocation(programID, name);
        glUniform2f(location, value.x, value.y);
    }
    @GLThread
    public void setUniform4f(String name, Vector4f value){
        int location = glGetUniformLocation(programID, name);
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    @GLThread
    public void setUniform3f(String name, Vector3f value){
        int location = glGetUniformLocation(programID, name);
        glUniform3f(location, value.x, value.y, value.z);
    }

    public void createShader(File vertex, File fragment){
        StringBuilder vertexShader = new StringBuilder();
        StringBuilder fragmentShader = new StringBuilder();

        FileInputStream vertexStream = null;
        FileInputStream fragmentStream = null;

        try {
            vertexStream = new FileInputStream(vertex);
            fragmentStream = new FileInputStream(fragment);

            int data = vertexStream.read();
            while(data != -1){
                vertexShader.append((char) data);
                data = vertexStream.read();
            }

            data = fragmentStream.read();
            while(data != -1){
                fragmentShader.append((char) data);
                data = fragmentStream.read();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(vertexStream !=null)
                    vertexStream.close();
                if(fragmentStream !=null)
                    fragmentStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        createShader(vertexShader.toString(), fragmentShader.toString());
    }

    public void createShader(String vertex, String fragment) {
        this.vertex = vertex;
        this.fragment = fragment;
        Runnable r = () -> createShaderNow(vertex,fragment);
        Manager.queueGLFunction(r);
    }

    @GLThread
    public void createShaderNow(String vertex, String fragment){
        this.vertex = vertex;
        this.fragment = fragment;
        this.attemptedCompile = true;
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexShaderID, vertex);
        glShaderSource(fragmentShaderID, fragment);
        glCompileShader(vertexShaderID);
        glCompileShader(fragmentShaderID);
        vertexCompileStatus = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == 1;
        fragmentCompileStatus = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == 1;

        if(!vertexCompileStatus){
            Logger.log(new ShaderError(vertex, true));
        }

        if(!fragmentCompileStatus)
        {
            Logger.log(new ShaderError(fragment, false));
        }

        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
    }

    public void destroy(){
        Runnable r = () -> {
            glDetachShader(programID, vertexShaderID);
            glDetachShader(programID, fragmentShaderID);
            glDeleteShader(vertexShaderID);
            glDeleteShader(fragmentShaderID);
            glDeleteProgram(programID);
        };
        Manager.queueGLFunction(r);
    }

    /**
     * activate the shader program. very important to do before drawing the object
     * @return true if program was successfully activated
     */
    @GLThread
    public boolean use(){
        if(!valid()){
            Logger.log(ShaderError.invalidProgramIDError, true);
        }
        else glUseProgram(programID);
        return valid();
    }
    public boolean valid(){
        return (programID >0);
    }

    @Override
    public void load() {
        /*if(vertex == null || fragment == null)
        {
            System.out.println("Preset:" + presetIndex);
            switch (presetIndex)
            {
                case 0 -> setDefaultShader();
                case 1 -> setSpriteShader();
                case 2 -> {
                    System.out.println("setting 2");
                    setLightSpriteShader();
                }
            }
        }
        else
            createShader(vertex, fragment);*/
        switch (presetIndex)
        {
            case 0 -> setDefaultShader();
            case 1 -> setSpriteShader();
            case 2 -> {
                setLightSpriteShader();
            }
        }
    }
}
