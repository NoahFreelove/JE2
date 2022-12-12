package JE.Utility;

// Be careful with using watchers. They're run every frame and can get expensive with long operations
public interface Watcher {
    void invoke();
}
