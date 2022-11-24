package JE.Rendering.Shaders.BuiltIn.LightObject;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

public class LightObjectShader extends ShaderProgram {
    public LightObjectShader(){
        CreateShader(new File("src/main/java/JE/Rendering/Shaders/BuiltIn/LightObject/lightObject.vert"),
                new File("src/main/java/JE/Rendering/Shaders/BuiltIn/LightObject/lightObject.frag"));
        supportsLighting = true;
    }
}
