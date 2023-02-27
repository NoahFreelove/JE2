package org.JE.JE2.Rendering.Shaders.Debugging;

public class ShaderDebugInfo {
    public StringBuilder info = new StringBuilder();
    public boolean isGood;

    @Override
    public String toString() {
        return (isGood? "Succeeded verification" : "Failed verification: " + info);
    }
}
