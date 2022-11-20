package JE.Rendering.Shaders.BuiltIn.LightSprite;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

public class LightSpriteShader extends ShaderProgram {
    public LightSpriteShader(){
        CreateShader(new File("src/main/java/JE/Rendering/Shaders/BuiltIn/LightSprite/lightSprite.vert"),
                new File("src/main/java/JE/Rendering/Shaders/BuiltIn/LightSprite/lightSprite.frag"));
    }
}
