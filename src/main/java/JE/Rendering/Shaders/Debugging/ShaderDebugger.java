package JE.Rendering.Shaders.Debugging;

import JE.Manager;
import JE.Objects.Base.GameObject;
import JE.Objects.Base.Identity;
import JE.Objects.Base.Sprite;
import JE.Objects.CameraRig;
import JE.Rendering.RenderTypes.Renderer;
import JE.Rendering.Shaders.ShaderProgram;
import JE.Rendering.VertexBuffers.VAO2f;
import JE.Scene.Scene;
import org.joml.Vector2f;

public class ShaderDebugger {
    public ShaderDebugInfo VerifyShader(ShaderProgram shader){
        boolean allGood = true;
        ShaderDebugInfo sdi = new ShaderDebugInfo();
        if(!shader.vertexCompileStatus){
            allGood = false;
            sdi.info = "Vertex Shader failed to compile";
        }
        if(!shader.fragmentCompileStatus){
            allGood = false;
            sdi.info = "Fragment Shader failed to compile";
        }

        sdi.isGood = allGood;
        return sdi;
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
