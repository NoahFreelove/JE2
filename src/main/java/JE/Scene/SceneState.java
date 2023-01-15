package JE.Scene;

import JE.Logging.Logger;
import JE.Objects.Base.GameObject;
import JE.Objects.Gizmos.Gizmo;
import JE.Objects.Lights.Light;
import JE.Objects.Lights.PointLight;

import java.io.*;

public class SceneState {

    private final byte[] objectState;
    private final byte[] lightState;
    private final byte[] gizmoState;

    private SceneState(byte[] objectState, byte[] lightState, byte[] gizmoState){
        this.objectState = objectState;
        this.lightState = lightState;
        this.gizmoState = gizmoState;
    }
    public static SceneState saveState(Scene scene){
        return saveState(scene, new SceneStateSaveMethod());
    }

    public static SceneState saveState(Scene scene, SceneStateSaveMethod saveType) {
        return saveState(scene, saveType, new GameObject[]{}, new PointLight[]{}, new Gizmo[]{});
    }

    public static SceneState saveState(Scene scene, SceneStateSaveMethod saveType, GameObject[] ignore, PointLight[] ignoreLight, Gizmo[] ignoreGizmo) {
        byte[] bytes = saveObjects(scene, ignore);
        byte[] lightBytes = saveLights(scene, ignoreLight);
        byte[] gizmoBytes = saveGizmo(scene, ignoreGizmo);

        if(saveType.saveToFile){
            try {
                FileOutputStream fos = new FileOutputStream(saveType.filePath);
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new SceneState(bytes, lightBytes, gizmoBytes);
    }

    private static byte[] saveObjects(Scene scene, GameObject[] ignore) {
        byte[] bytes = new byte[0];
        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);

            for (GameObject go : scene.world.gameObjects) {
                boolean ignoreThis = false;
                for (GameObject ig : ignore) {
                    if (go == ig) {
                        ignoreThis = true;
                        break;
                    }
                }
                if (ignoreThis)
                    continue;
                Logger.log("Saving " + go.getClass().getSimpleName());
                oos.writeObject(go);
            }
            bao.close();
            oos.close();
            bytes = bao.toByteArray();

        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        return bytes;
    }
    private static byte[] saveLights(Scene scene, Light[] ignore) {
        byte[] bytes = new byte[0];
        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);

            for (Light go : scene.world.lights) {
                boolean ignoreThis = false;
                for (Light ig : ignore) {
                    if (go == ig) {
                        ignoreThis = true;
                        break;
                    }
                }
                if (ignoreThis)
                    continue;
                Logger.log("Saving " + go.getClass().getSimpleName());
                oos.writeObject(go);
            }
            bao.close();
            oos.close();
            bytes = bao.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static byte[] saveGizmo(Scene scene, Gizmo[] ignore) {
        byte[] bytes = new byte[0];
        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);

            for (Gizmo go : scene.world.gizmos) {
                boolean ignoreThis = false;
                for (Gizmo ig : ignore) {
                    if (go == ig) {
                        ignoreThis = true;
                        break;
                    }
                }
                if (ignoreThis)
                    continue;
                Logger.log("Saving " + go.getClass().getSimpleName());
                oos.writeObject(go);
            }
            bao.close();
            oos.close();
            bytes = bao.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /*public static boolean loadState(Scene scene, String filepath){
        byte[] bytes = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(filepath);
            bytes = fis.readAllBytes();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadState(scene, new SceneState(bytes));
    }*/

    public static boolean loadState(Scene scene, SceneState state){
        scene.clear();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state.objectState);
            ObjectInputStream ois = new ObjectInputStream(bis);
            while (true) {
                try {
                    GameObject go = (GameObject) ois.readObject();
                    scene.add(go);
                } catch (EOFException e) {
                    break;
                }
            }
            bis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state.lightState);
            ObjectInputStream ois = new ObjectInputStream(bis);
            while (true) {
                try {
                    PointLight go = (PointLight) ois.readObject();
                    scene.addLight(go);
                } catch (EOFException e) {
                    break;
                }
            }
            bis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state.gizmoState);
            ObjectInputStream ois = new ObjectInputStream(bis);
            while (true) {
                try {
                    Gizmo go = (Gizmo) ois.readObject();
                    scene.addGizmo(go);
                } catch (EOFException e) {
                    break;
                }
            }
            bis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public byte[] getBytes()
    {
        return objectState;
    }

    public static SceneState makeState(byte[] objectState, byte[] lightState, byte[] gizmoState){
        return new SceneState(objectState,lightState,gizmoState);
    }
}
