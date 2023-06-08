package org.JE.JE2.Objects.Scripts.ScreenEffects.PostProcess;

import org.JE.JE2.Rendering.Shaders.ShaderRegistry;

public class PostProcessRegistry {
    // you should always be using this for full-screen vertex post-processing unless you have your own.
    public static final String POST_PROCESS_VERTEX = ShaderRegistry.QUAD_VERTEX;
    public static final String POST_PROCESS_FRAGMENT = ShaderRegistry.QUAD_FRAGMENT;

    public static final String INVERT_FRAG = """
            #version 330 core

            in vec2 UV;
            out vec4 FragColor;
            uniform sampler2D JE_Texture;

            void main()
            {
                vec4 color = texture(JE_Texture, UV);
                vec3 invertedColor = vec3(1.0) - color.rgb; // Invert the RGB values
                FragColor = vec4(invertedColor, color.a);
            }""";

    public static final String BLUR_FRAG = """
            #version 330 core
                        
            uniform sampler2D JE_Texture;
            uniform vec2 JE_TextureSize;
            in vec2 UV;
            
            const int kernelSize = 5;
                        
            void main()
            {
                vec2 texelSize = 1.0 / JE_TextureSize;
               \s
                vec3 blurColor = vec3(0.0);
                float weightTotal = 0.0;
                        
                for (int i = -kernelSize; i <= kernelSize; i++)
                {
                    for (int j = -kernelSize; j <= kernelSize; j++)
                    {
                        vec2 offset = vec2(float(i), float(j)) * texelSize;
                        blurColor += texture(JE_Texture, UV + offset).rgb;
                        weightTotal += 1.0;
                    }
                }
               \s
                vec3 finalColor = blurColor / weightTotal;
                gl_FragColor = vec4(finalColor, 1.0);
            }""";

    public static final String BLOOM_FRAG = """
            #version 330

            uniform sampler2D JE_Texture;
            in vec2 UV;
            out vec4 fragColor;

            const int kernelSize = 50;
            const float threshold = 0.4;
            const float intensity = 2.0;

            void main()
            {
                vec4 color = texture(JE_Texture, UV);
                vec3 hdrColor = color.rgb;

                vec3 brightPass = vec3(0.0);
                for (int i = 0; i < kernelSize; i++)
                {
                    vec2 offset = vec2(float(i) - float(kernelSize - 1) / 2.0);
                    vec4 sampleColor = texture(JE_Texture, UV + offset / textureSize(JE_Texture, 0));
                    float luminance = dot(sampleColor.rgb, vec3(0.2126, 0.7152, 0.0722));
                    brightPass += max(luminance - threshold, 0.0);
                }
                brightPass /= float(kernelSize);

                fragColor = vec4(hdrColor + brightPass * intensity, color.a);
            }""";
}
