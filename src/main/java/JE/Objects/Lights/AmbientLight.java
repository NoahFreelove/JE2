package JE.Objects.Lights;

import JE.Rendering.Shaders.ShaderProgram;
import JE.UI.UIElements.Style.Color;

public class AmbientLight extends Light{
    public AmbientLight() {
        super(Color.WHITE,1,0);
        this.type = 0;
    }

    @Override
    protected void setLightSpecific(ShaderProgram shaderProgram, int index) {}
}
