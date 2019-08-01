package ge.edu.freeuni.chatuna.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import ge.edu.freeuni.chatuna.App;

public class WiFiP2pBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "P2pBroadcastReceiver";

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private Activity activity;
    private ChatContract.ChatView.OnWifiDirectNameChanged listener;

    public WiFiP2pBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, Activity activity) {
        super();

        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d("test", action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (manager != null) {
                manager.requestPeers(channel, (WifiP2pManager.PeerListListener)activity);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                manager.requestConnectionInfo(channel, (WifiP2pManager.ConnectionInfoListener)activity);
            } else {

            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            String name = device.deviceName;
            App.username = name;
            Log.d("test", App.username);
            listener.onNameChanged();
        }
    }

    void setOnWifiDirectNameChangedListener(ChatContract.ChatView.OnWifiDirectNameChanged listener) {
        this.listener = listener;
    }
}