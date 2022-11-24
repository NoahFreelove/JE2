package JE.Rendering.Shaders.Debugging;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Base.Identity;
import JE.Objects.CameraRig;
import JE.Rendering.RenderTypes.Renderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.VertexBuffers.VAO2f;
import JE.Scene.Scene;
import org.joml.Vector2f;

public class ShaderDebugger {
    public static ShaderDebugInfo VerifyShader(ShaderProgram shader){
        boolean allGood = true;
        ShaderDebugInfo sdi = new ShaderDebugInfo();
        if(!shader.vertexCompileStatus){
            allGood = false;
            sdi.info += "Vertex Shader failed to compile";
        }
        if(!shader.fragmentCompileStatus){
            allGood = false;
            sdi.info += "Fragment Shader failed to compile";
        }

        checkErrors(shader.vertex.split("\r"),true,sdi);
        checkErrors(shader.fragment.split("\r"),false,sdi);

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
                if(line.contains("void main") || line.contains("#define") || line.contains("#version") || line.contains("attribute") || line.contains("uniform") || line.contains("varying") || line.contains("gl_Position") || line.contains("gl_FragColor") || line.contains("{") || line.contains("}")){
                    continue;
                }

                sdi.info += "Line " + (i+1) + " is missing a semicolon\n";
                sdi.isGood = false;
            }

            if(line.contains("attribute") && !isVertex){
                sdi.info += "Line " + (i+1) + " has an attribute in the fragment shader\n";
                sdi.isGood = false;
            }
        }
        if(bracketCount != 0){
            sdi.info += "Brackets are not balanced\n";
            sdi.isGood = false;
        }
    }

    public static Scene shaderTestScene(ShaderProgram shader, ShaderTestSceneCustomData data){
        Scene scene = new Scene();

        // Lighting
        scene.addLight(data.sceneLights);

        // Test Object
        GameObject testObject = new GameObject(new Identity("Test Object", "GameObject"));
        testObject.getTransform().position = new Vector2f(-0.5f, -0.5f);
        scene.add(testObject);
        scene.add(data.otherObjects);

        // Configure Renderer
        Renderer r = new Renderer(new VAO2f(data.vertices, shader));
        r.baseColor = data.baseColor;
        testObject.addComponent(r);

        // Setup Camera
        CameraRig cameraRig = new CameraRig();
        scene.add(cameraRig);
        scene.activeCamera = cameraRig.camera;

        return scene;
    }

    public static void TestShader(ShaderProgram shaderProgram){
        Manager.SetScene(shaderTestScene(shaderProgram, new ShaderTestSceneCustomData()));
    }
    public static void TestShader(ShaderProgram shaderProgram, ShaderTestSceneCustomData data){
        Manager.SetScene(shaderTestScene(shaderProgram, data));
    }
}
