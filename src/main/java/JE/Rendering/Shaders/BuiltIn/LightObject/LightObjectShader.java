package JE.Rendering.Shaders.BuiltIn.LightObject;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

public class LightObjectShader extends ShaderProgram {
    public LightObjectShader(){
        CreateShader(new File("shaders/LightObject/lightObject.vert"),
                new File("shaders/LightObject/lightObject.frag"));
        supportsLighting = true;
    }
}
