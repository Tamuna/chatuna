package ge.edu.freeuni.chatuna;

public class WifiP2pSingleton {
    private static final WifiP2pSingleton ourInstance = new WifiP2pSingleton();

    public static WifiP2pSingleton getInstance() {
        return ourInstance;
    }

    private WifiP2pSingleton() {
    }
}
