package ge.edu.freeuni.chatuna;

import android.app.Application;

public class App extends Application {

    public static String username = null;
    public static boolean nameUpdated = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
