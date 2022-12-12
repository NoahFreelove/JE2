package JE.Rendering.Shaders.BuiltIn.LightSprite;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

public class LightSpriteShader extends ShaderProgram {
    public LightSpriteShader(){
        CreateShader(new File("shaders/LightSprite/lightSprite.vert"),
                new File("shaders/LightSprite/lightSprite.frag"));
        supportsLighting = true;
    }
}
