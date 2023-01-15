package JE.Rendering.Shaders.BuiltIn.LightObject;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

@Deprecated(forRemoval = true) // lightSprite will replace. Use ShaderProgram for non-lit solid color objects
public class LightObjectShader extends ShaderProgram {
    public LightObjectShader(){
        CreateShader(new File("shaders/LightObject/lightObject.vert"),
                new File("shaders/LightObject/lightObject.frag"));
        supportsLighting = true;
    }
}
