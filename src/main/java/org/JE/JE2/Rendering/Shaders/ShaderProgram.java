package org.JE.JE2.Rendering.Shaders;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.Logging.Errors.ShaderError;
import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.Shaders.Uniforms.ShaderUniform;
import org.JE.JE2.Utility.Loadable;
import org.JE.JE2.Window.Window;
import org.JE.JE2.Window.WindowPreferences;
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
    private ShaderUniform[] uniforms = new ShaderUniform[0];
    public int presetIndex = 0;
    public transient boolean vertexCompileStatus;
    public transient boolean fragmentCompileStatus;
    private transient int vertexShaderID;
    private transient int fragmentShaderID;
    public boolean logCommonErrors = true;

    public static boolean logShaderSourceUponError = true;

    private boolean hasQueuedCompile = false;

    private ShaderProgram(){}

    public ShaderProgram(String vertex, String fragment){
        createShader(vertex,fragment);
    }

    public ShaderProgram(String vertexShader, String fragmentShader, boolean supportsTextures, boolean supportsLighting){
        createShader(vertexShader, fragmentShader);
        this.supportsTextures = supportsTextures;
        this.supportsLighting = supportsLighting;
    }

    public ShaderProgram(File vertexShader, File fragmentShader, boolean supportsTextures, boolean supportsLighting){
        createShader(vertexShader, fragmentShader);
        this.supportsLighting = supportsLighting;
        this.supportsTextures = supportsTextures;
    }

    public ShaderProgram(ShaderModule sm){
        createShader(sm.vertex(),sm.fragment());
        this.supportsLighting = sm.supportsLighting();
        this.supportsTextures = sm.supportsTextures();
    }

    public static ShaderProgram invalidShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.attemptedCompile = true;
        sp.attemptedCompile = true;
        sp.programID = -1;
        sp.fragmentCompileStatus = false;
        sp.vertexCompileStatus = false;
        return sp;
    }

    public static ShaderProgram defaultShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.createShader(ShaderRegistry.DEFAULT_VERTEX, ShaderRegistry.DEFAULT_FRAGMENT);
        return sp;
    }

    public static ShaderProgram spriteShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.createShader(ShaderRegistry.SPRITE_VERTEX, ShaderRegistry.SPRITE_FRAGMENT);
        sp.supportsTextures = true;
        return sp;
    }

    public static ShaderProgram lightSpriteShader(){
        ShaderProgram sp = new ShaderProgram();
        sp.createShader(ShaderRegistry.LIGHTSPRITE_VERTEX, ShaderRegistry.LIGHTSPRITE_FRAGMENT);
        sp.supportsLighting = true;
        sp.supportsTextures = true;
        return sp;
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
    public void setUniform2f(String name, float x, float y){
        int location = glGetUniformLocation(programID, name);
        glUniform2f(location, x, y);
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
        hasQueuedCompile = true;
    }

    @GLThread
    public void createShaderNow(String vertex, String fragment){
        this.vertex = vertex;
        this.fragment = fragment;
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexShaderID, vertex);
        glShaderSource(fragmentShaderID, fragment);
        glCompileShader(vertexShaderID);
        glCompileShader(fragmentShaderID);
        this.attemptedCompile = true;

        vertexCompileStatus = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == 1;
        fragmentCompileStatus = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == 1;

        if(logCommonErrors){
            if(!vertexCompileStatus){
                Logger.log(new ShaderError("Vertex shader did not compile...", vertex, fragment));
            }

            if(!fragmentCompileStatus)
            {
                Logger.log(new ShaderError("Fragment shader did not compile...", vertex, fragment));
            }
        }

        if(!vertexCompileStatus || !fragmentCompileStatus){
            return;
        }

        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
        hasQueuedCompile = false;
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
            if(logCommonErrors){
                //System.out.println("Has Queued Compile: " + hasQueuedCompile);
                if(!hasQueuedCompile){
                    System.out.println("shader ID invalid, recompiling...");
                    createShader(vertex,fragment);
                }
                else
                    return false;
                if(logShaderSourceUponError){
                    Logger.log(new ShaderError("Shader program ID is not valid", vertex, fragment));
                }
                else {
                    Logger.log(ShaderError.invalidProgramIDError);
                }
            }
        }
        else {
            for (ShaderUniform uniform : uniforms) {
                uniform.set(this);
            }
            glUseProgram(programID);
        }
        return valid();
    }
    public boolean valid(){
        return (programID >0);
    }

    @Override
    public void load() {

    }

    public void addUniform(ShaderUniform shaderUniform){
        // increase the size of the array by 1
        ShaderUniform[] newArray = new ShaderUniform[uniforms.length + 1];
        // copy the old array into the new one
        System.arraycopy(uniforms, 0, newArray, 0, uniforms.length);
        // add the new uniform to the end of the array
        newArray[newArray.length - 1] = shaderUniform;
        // set the old array to the new array
        uniforms = newArray;
    }

    public void nullifyUniform(int index){
        if (index<uniforms.length && index>=0){
            uniforms[index] = ShaderUniform.EMPTY;
        }
    }
}
