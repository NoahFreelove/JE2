package JE.Scene;

public class SceneStateSaveMethod {
    public boolean saveToFile = false;
    public String filePath = "";

    public SceneStateSaveMethod(boolean saveToFile, String filePath) {
        this.saveToFile = saveToFile;
        this.filePath = filePath;
    }
    public SceneStateSaveMethod(){}
}
