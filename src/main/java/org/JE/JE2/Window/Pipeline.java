package org.JE.JE2.Window;

import org.JE.JE2.Annotations.GLThread;
import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.Manager;
import org.JE.JE2.Utility.GarbageCollection;

public abstract class Pipeline {

    int gcThreshold = 500;

    @GLThread
    protected abstract void run(); // will be called every frame

    @GLThread
    public final void onStart(){
        run();
        onEnd();
    }

    @GLThread
    protected void onEnd(){
        if(Window.getFrameCount() % gcThreshold == 0)
        {
            GarbageCollection.takeOutDaTrash();
        }
    }
    @GLThread
    public abstract void renderObjects(); // GameObjects
    @GLThread
    public abstract void postProcess(); // GameObjects
    @GLThread
    public abstract void renderUI(); // UI
    @GLThread
    public abstract void runQueuedEvents(); // GL thread events
    @GLThread
    public abstract void updateScene(); // run GameObject update methods

    @GLThread
    public abstract void checkWatchers(); // run scene's watchers to watch for event changes

    @GLThread public abstract void init();

}
