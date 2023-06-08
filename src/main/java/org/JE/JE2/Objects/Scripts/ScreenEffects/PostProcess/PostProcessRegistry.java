package org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess;

import org.JE.JE2.Rendering.Shaders.ShaderRegistry;

public class PostProcessRegistry {
    // you should always be using this for full-screen vertex post processing unless you have your own.
    public static final String POST_PROCESS_VERTEX = ShaderRegistry.QUAD_VERTEX;
    public static final String POST_PROCESS_FRAGMENT = ShaderRegistry.QUAD_FRAGMENT;

    public static final String INVERT_FRAG = "#version 330 core\n" +
            "\n" +
            "in vec2 UV;\n" +
            "out vec4 FragColor;\n" +
            "uniform sampler2D JE_Texture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 color = texture(JE_Texture, UV);\n" +
            "    vec3 invertedColor = vec3(1.0) - color.rgb; // Invert the RGB values\n" +
            "    FragColor = vec4(invertedColor, color.a);\n" +
            "}";

    public static final String BLUR_FRAG = "#version 330 core\n" +
            "\n" +
            "in vec2 UV;\n" +
            "out vec4 FragColor;\n" +
            "uniform sampler2D JE_Texture;\n" +
            "uniform vec2 JE_TextureSize;\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 color = vec4(0.0);\n" +
            "    vec2 texelSize = vec2(1.0) / JE_TextureSize;\n" +
            "\n" +
            "    for (float x = -1.0; x <= 1.0; x++) {\n" +
            "        for (float y = -1.0; y <= 1.0; y++) {\n" +
            "            vec2 offset = vec2(x, y) * texelSize;\n" +
            "            color += texture(JE_Texture, UV + offset);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    FragColor = color / 9.0; // Divide by the number of samples (9 in this case)\n" +
            "}";

    public static final String BLOOM_FRAG = "#version 330\n" +
            "\n" +
            "uniform sampler2D JE_Texture;\n" +
            "in vec2 UV;\n" +
            "out vec4 fragColor;\n" +
            "\n" +
            "const int kernelSize = 15;\n" +
            "const float threshold = 0.7;\n" +
            "const float intensity = 2.0;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 color = texture(JE_Texture, UV);\n" +
            "    vec3 hdrColor = color.rgb;\n" +
            "\n" +
            "    vec3 brightPass = vec3(0.0);\n" +
            "    for (int i = 0; i < kernelSize; i++)\n" +
            "    {\n" +
            "        vec2 offset = vec2(float(i) - float(kernelSize - 1) / 2.0);\n" +
            "        vec4 sampleColor = texture(JE_Texture, UV + offset / textureSize(JE_Texture, 0));\n" +
            "        float luminance = dot(sampleColor.rgb, vec3(0.2126, 0.7152, 0.0722));\n" +
            "        brightPass += max(luminance - threshold, 0.0);\n" +
            "    }\n" +
            "    brightPass /= float(kernelSize);\n" +
            "\n" +
            "    fragColor = vec4(hdrColor + brightPass * intensity, color.a);\n" +
            "}";
}
