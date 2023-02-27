package org.JE.JE2.Rendering.Shaders.Debugging;

import org.JE.JE2.Annotations.PerformanceWarning;
import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.Renderer;
import org.JE.JE2.Rendering.Shaders.ShaderProgram;
import org.JE.JE2.Scene.Scene;
import org.joml.Vector2f;

public class ShaderDebugger {

    @PerformanceWarning(Severity = 3, Reason = "Will stop current thread until GL Thread processes shader")
    public static ShaderDebugInfo VerifyShader(ShaderProgram shader){
        boolean allGood = true;
        ShaderDebugInfo sdi = new ShaderDebugInfo();

        // shaders compile on a different thread, so we wait for the GL thread to compile before testing. Otherwise, it will dismiss it as invalid
        // Will delay max 1 frame (in theory).
        while (!shader.attemptedCompile && !shader.valid());

        if(!shader.vertexCompileStatus){
            allGood = false;
            sdi.info.append("Vertex Shader failed to compile\n");
        }
        if(!shader.fragmentCompileStatus){
            allGood = false;
            sdi.info.append("Fragment Shader failed to compile\n");
        }

        checkErrors(shader.vertex.split("\n"),true,sdi);
        checkErrors(shader.fragment.split("\n"),false,sdi);

        sdi.isGood = allGood;
        return sdi;
    }

    private static void checkErrors(String[] lines, boolean isVertex, ShaderDebugInfo sdi){
        boolean inComment = false;
        int bracketCount = 0;

        for(int i = 0; i < lines.length; i++){
            String line = lines[i];
            // if line only contains spaces, skip
            if(line.trim().length() == 0) continue;
            if(line.contains("/*")){
                inComment = true;
            }
            if(line.contains("*/")){
                inComment = false;
            }
            if(inComment){
                continue;
            }
            if(line.contains("{")){
                bracketCount++;
            }
            if(line.contains("}")){
                bracketCount--;
            }

            // check if lines don't have a semicolon
            if(!line.contains(";")){
                // if it doesn't require a semicolon, then it's fine
                if(line.contains("void main") || line.contains("#define") || line.contains("#version") ||
                        line.contains("attribute") || line.contains("uniform") || line.contains("varying")
                        || line.contains("{") || line.contains("}") ||  line.startsWith("//")){
                    continue;
                }

                sdi.info.append("Line ").append(i + 1).append(" is missing a semicolon\n");

                sdi.isGood = false;
            }

            if(line.contains("attribute") && !isVertex){
                sdi.info.append("Line ").append(i + 1).append(" has an attribute in the fragment shader\n");
                sdi.isGood = false;
            }
        }
        if(bracketCount != 0){
            sdi.info.append("Brackets are not balanced\n");
            sdi.isGood = false;
        }
    }

    public static Scene shaderTestScene(ShaderProgram shader, ShaderTestSceneCustomData data){
        Scene scene = new Scene();

        // Lighting
        scene.addLight(data.sceneLights);

        // Test Object
        GameObject testObject = new GameObject();
        testObject.getTransform().setPosition(new Vector2f(-0.5f, -0.5f));
        scene.add(testObject);
        scene.add(data.otherObjects);

        // Configure Renderer
        Renderer r = new Renderer();
        r.getVAO().setShaderProgram(shader);
        r.baseColor = data.baseColor;
        testObject.addScript(r);

        // Setup Camera
        GameObject cameraRig = new GameObject();
        cameraRig.addScript(new Camera());
        scene.setCamera(cameraRig.getScript(Camera.class));
        scene.add(cameraRig);

        return scene;
    }

    public static void TestShader(ShaderProgram shaderProgram){
        Manager.setScene(shaderTestScene(shaderProgram, new ShaderTestSceneCustomData()));
    }
    public static void TestShader(ShaderProgram shaderProgram, ShaderTestSceneCustomData data){
        Manager.setScene(shaderTestScene(shaderProgram, data));
    }
}
