package JE.Rendering.Shaders.BuiltIn.UI.ScreenUI;

import JE.Rendering.Shaders.ShaderProgram;

import java.io.File;

public class ScreenUIShader extends ShaderProgram {
    public ScreenUIShader(){
        CreateShader(new File("src/main/java/JE/Rendering/Shaders/BuiltIn/UI/ScreenUI/screenUI.vert"),
                new File("src/main/java/JE/Rendering/Shaders/BuiltIn/UI/ScreenUI/screenUI.frag"));
        supportsLighting = false;
    }
}
