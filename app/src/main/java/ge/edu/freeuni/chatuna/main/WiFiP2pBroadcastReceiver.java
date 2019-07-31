package ge.edu.freeuni.chatuna.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

public class WiFiP2pBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "P2pBroadcastReceiver";

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity mainActivity;

    public WiFiP2pBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity mainActivity) {
        super();

        this.manager = manager;
        this.channel = channel;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (manager != null) {
                manager.requestPeers(channel, mainActivity.peerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }
}
