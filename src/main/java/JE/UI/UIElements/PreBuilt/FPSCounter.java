package JE.UI.UIElements.PreBuilt;

import JE.Manager;
import JE.UI.UIElements.Label;
import JE.Window.UIHandler;

import static org.lwjgl.nuklear.Nuklear.nk_label_colored;

public class FPSCounter extends Label {

    int fps = 0;
    public long delayMs = 500;
    private Thread delayThread;

    public FPSCounter(){
        start();
    }
    public FPSCounter(String prefix){
        this.text = prefix;
        start();
    }

    @Override
    protected void render() {
        nk_label_colored(UIHandler.ctx,text + fps,alignment, textColor.nkColor());
    }

    public void start(){
        if(delayThread !=null)
            if(delayThread.isAlive())
                return;
        delayThread = new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(delayMs);
                    fps = Manager.getFPS();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        delayThread.start();
    }
    public void stop(){
        delayThread.interrupt();
    }
}
