package ge.edu.freeuni.chatuna.main;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.App;
import ge.edu.freeuni.chatuna.Injection;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.chat.ChatActivity;
import ge.edu.freeuni.chatuna.component.CustomToolbar;
import ge.edu.freeuni.chatuna.model.HistoryModel;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    private WiFiP2pBroadcastReceiver wiFiP2pBroadcastReceiver;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    private List<WifiP2pDevice> peers = new ArrayList<>();
    private String[] deviceNames;
    private WifiP2pDevice[] devices;

    private final int REQUEST_ACCESS_FINE_LOCATION = 11;

    private HistoryRecyclerAdapter adapter;
    private MainContract.MainPresenter presenter;
    @BindView(R.id.view_navigation)
    DrawerLayout navigationView;

    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

    @BindView(R.id.rv_history)
    RecyclerView rvHistory;

    @BindView(R.id.view_nodata)
    ConstraintLayout viewNodata;

    @OnClick(R.id.layoutMenuChat)
    void onChatMenuItemClick() {
        navigationView.closeDrawer(Gravity.LEFT);
        ChatActivity.start(this);
    }

    @OnClick(R.id.layoutMenuHistory)
    void onHistoryMenuItemClick() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("test", "Discovery Started");
            }

            @Override
            public void onFailure(int i) {

            }
        });
        navigationView.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initView();
        App.username = "tamuna";

        presenter = new MainPresenterImpl(new MainInteractorImpl(Injection.
                provideChatRepository(this.getApplicationContext())), this);
        if(hasReadPermissions())
            presenter.start();

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                Log.d("test", "size " + Integer.toString(peerList.getDeviceList().size()));
                deviceNames = new String[peerList.getDeviceList().size()];
                devices = new WifiP2pDevice[peerList.getDeviceList().size()];

                int i = 0;
                for(WifiP2pDevice device : peerList.getDeviceList()) {
                    deviceNames[i] = device.deviceName;
                    Log.d("test", "device " + deviceNames[i]);
                    devices[i] = device;
                    i++;
                }
            }
        }
    };

    void handleItemClick(int i) {
        final WifiP2pDevice device = devices[i];
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("test", "Connected to " + device.deviceName);
            }

            @Override
            public void onFailure(int reason) {
                Log.d("test", "Not connected to " + device.deviceName);
            }
        });
    }

    private boolean hasReadPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                }
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void initView() {
        toolbar.setListener(new OnMainItemsClickListenerImpl());
        adapter = new HistoryRecyclerAdapter(new OnItemClickedListenerImpl());
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void registerReceiver() {
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        wiFiP2pBroadcastReceiver = new WiFiP2pBroadcastReceiver(manager, channel, this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(wiFiP2pBroadcastReceiver, intentFilter);
    }

    @Override
    public void unregisterReceiver() {
        if (wiFiP2pBroadcastReceiver == null) {
            unregisterReceiver(wiFiP2pBroadcastReceiver);
        }
    }

    @Override
    public void onNoDataLoaded() {
        viewNodata.setVisibility(View.VISIBLE);
        rvHistory.setVisibility(View.GONE);
    }

    @Override
    public void onDataLoaded(List<HistoryModel> histories) {
        viewNodata.setVisibility(View.GONE);
        rvHistory.setVisibility(View.VISIBLE);
        adapter.bindData(histories);
    }

    class OnMainItemsClickListenerImpl implements CustomToolbar.OnMainItemsClickListener {

        @Override
        public void onMenuClick() {
            if (navigationView.isDrawerVisible(Gravity.LEFT)) {
                navigationView.closeDrawer(Gravity.LEFT);
            } else {
                navigationView.openDrawer(Gravity.LEFT);
            }
        }
    }

    class OnItemClickedListenerImpl implements HistoryRecyclerAdapter.OnItemClickedListener {

        @Override
        public void onHistoryItemClick(@NotNull HistoryModel historyModel) {
            ChatActivity.start(MainActivity.this, historyModel.getSenderName(), true);
        }
    }
}
