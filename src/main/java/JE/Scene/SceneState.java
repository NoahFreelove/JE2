package JE.Scene;

import JE.Objects.GameObject;

import java.io.*;

public class SceneState {

    private byte[] state;

    private SceneState(byte[] state){
        this.state = state;
    }

    public static SceneState saveState(Scene scene, SceneStateSaveMethod saveType) {
        return saveState(scene, saveType, new GameObject());
    }

    public static SceneState saveState(Scene scene, SceneStateSaveMethod saveType, GameObject... ignore) {
        byte[] bytes = new byte[0];
        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);

            for (GameObject go : scene.gameObjects) {
                boolean ignoreThis = false;
                for (GameObject ig : ignore) {
                    if (go == ig) {
                        ignoreThis = true;
                        break;
                    }
                }
                if (ignoreThis)
                    continue;
                oos.writeObject(go);
            }
            bao.close();
            oos.close();
            bytes = bao.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(saveType.saveToFile){
            try {
                FileOutputStream fos = new FileOutputStream(saveType.filePath);
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new SceneState(bytes);
    }

    public static boolean loadState(Scene scene, String filepath){
        byte[] bytes = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(filepath);
            bytes = fis.readAllBytes();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadState(scene, new SceneState(bytes));
    }

    public static boolean loadState(Scene scene, SceneState state){
        scene.clear();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state.state);
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
        return true;
    }

    public byte[] getBytes()
    {
        return state;
    }

    public static SceneState makeState(byte[] bytes){
        return new SceneState(bytes);
    }
}
