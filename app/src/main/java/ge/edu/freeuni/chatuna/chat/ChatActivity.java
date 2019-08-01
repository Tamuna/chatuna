package ge.edu.freeuni.chatuna.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.App;
import ge.edu.freeuni.chatuna.Injection;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.SendReceiveHelper;
import ge.edu.freeuni.chatuna.component.CustomToolbar;
import ge.edu.freeuni.chatuna.model.MessageModel;

public class ChatActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener,
        WifiP2pManager.ConnectionInfoListener, ChatContract.ChatView,
        ChatContract.ChatView.OnWifiDirectNameChanged {

    @BindView(R.id.view_loader)
    ConstraintLayout viewLoader;

    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;

    @BindView(R.id.et_message_input)
    EditText etMessageInput;

    @BindView(R.id.rv_found_peers)
    RecyclerView rvFoundPeers;

    @BindView(R.id.layout_message_input)
    ConstraintLayout layoutMessageInput;

    @OnClick(R.id.btn_send)
    void onSendClick() {
        String message = etMessageInput.getText().toString();
        etMessageInput.setText("");
        if (!message.isEmpty()) {
            presenter.sendMessage(new MessageModel(message, new Date(), App.username, true));
            adapter.bindSingleItem(new MessageModel(message, new Date(), App.username, true));
        }
        sendReceive.write(message.getBytes());
    }

    private ChatRecyclerAdapter adapter;
    private ChatContract.ChatPresenter presenter;

    private WiFiP2pBroadcastReceiver wiFiP2pBroadcastReceiver;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    private List<WifiP2pDevice> peers = new ArrayList<>();
    private ArrayList<String> deviceNames;

    private WifiP2pDevice[] devices;

    private ServerClass serverClass;
    private ClientClass clientClass;

    private SendReceive sendReceive;

    static final int MESSAGE_READ = 1;

    private FoundPeersRecyclerAdapter foundPeersRecyclerAdapter;

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) msg.obj;
                    String message = new String(readBuff, 0, msg.arg1);
                    adapter.bindSingleItem(new MessageModel(message, new Date(), "Tamuna", false));
                    presenter.sendMessage(new MessageModel(message, new Date(), "Tamuna", false));
                    if (rvFoundPeers.getVisibility() == View.VISIBLE) {
                        rvFoundPeers.setVisibility(View.GONE);
                        rvMessages.setVisibility(View.VISIBLE);
                        layoutMessageInput.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onNameChanged() {
        presenter.handleCurrentUser();
    }

    public class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket socket) {
            this.socket = socket;
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (socket != null) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) {
            SendReceiveHelper helper = new SendReceiveHelper(outputStream, bytes);
            helper.start();
        }
    }


    public class ServerClass extends Thread {
        private Socket socket;
        private ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientClass extends Thread {

        private Socket socket;
        private String hostAdd;

        public ClientClass(InetAddress hostAddress) {
            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static final String EXTRA_SENDER_NAME = "EXTRA_SENDER_NAME";
    private static final String EXTRA_IS_HISTORY = "EXTRA_IS_HISTORY";

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ChatActivity.class);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, String senderName, boolean isHistory) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(EXTRA_SENDER_NAME, senderName);
        intent.putExtra(EXTRA_IS_HISTORY, isHistory);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        boolean isHistory = getIntent().getBooleanExtra(EXTRA_IS_HISTORY, false);
        String senderName = getIntent().getStringExtra(EXTRA_SENDER_NAME);
        ButterKnife.bind(this);
        initView(isHistory);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(getApplicationContext(), getMainLooper(), null);

        presenter = new ChatPresenterImpl(this, new ChatInteractorImpl(Injection.
                provideChatRepository(this.getApplicationContext())));
        presenter.start();
        if (isHistory) {
            presenter.loadChatHistory(senderName);
            rvFoundPeers.setVisibility(View.GONE);
            rvMessages.setVisibility(View.VISIBLE);
        }
        viewLoader.setVisibility(View.VISIBLE);
        discoverPeers();

    }

    private void discoverPeers() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    private void initView(boolean isHistory) {
        toolbar.setListener(new OnSecondaryItemsClickListenerImpl());
        adapter = new ChatRecyclerAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);
        if (isHistory) {
            layoutMessageInput.setVisibility(View.GONE);
        }
        foundPeersRecyclerAdapter = new FoundPeersRecyclerAdapter(new OnItemClickListenerImpl());
        rvFoundPeers.setLayoutManager(new LinearLayoutManager(this));
        rvFoundPeers.setAdapter(foundPeersRecyclerAdapter);
    }

    @Override
    public void sendMessage(@NotNull MessageModel messsage) {
        adapter.bindSingleItem(messsage);
    }

    @Override
    public void displayHistory(@NotNull List<MessageModel> history) {
        adapter.bindData(history);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        if (!peerList.getDeviceList().equals(peers)) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            deviceNames = new ArrayList<>();
            devices = new WifiP2pDevice[peerList.getDeviceList().size()];

            int i = 0;
            for (WifiP2pDevice device : peerList.getDeviceList()) {
                deviceNames.add(device.deviceName);
                devices[i] = device;
                i++;
            }

            foundPeersRecyclerAdapter.bindData(deviceNames);
            viewLoader.setVisibility(View.GONE);
        }

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        final InetAddress groupOwner = info.groupOwnerAddress;

        if (info.groupFormed && info.isGroupOwner) {
            serverClass = new ServerClass();
            serverClass.start();
        } else {
            clientClass = new ClientClass(groupOwner);
            clientClass.start();
        }
    }

    @Override
    public void registerReceiver() {
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(getApplicationContext(), getMainLooper(), null);
        wiFiP2pBroadcastReceiver = new WiFiP2pBroadcastReceiver(manager, channel, this);
        wiFiP2pBroadcastReceiver.setOnWifiDirectNameChangedListener(this);
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

    class OnSecondaryItemsClickListenerImpl implements CustomToolbar.OnSecondaryItemsClickListener {

        @Override
        public void onBackClick() {
            ChatActivity.this.onBackPressed();
        }

        @Override
        public void onDeleteClick() {
            //TODO
        }
    }

    class OnItemClickListenerImpl implements FoundPeersRecyclerAdapter.OnItemClickListener {

        @Override
        public void onPeerSelected(int i) {
            final WifiP2pDevice device = devices[i];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    layoutMessageInput.setVisibility(View.VISIBLE);
                    rvMessages.setVisibility(View.VISIBLE);
                    rvFoundPeers.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int reason) {
                }
            });
            //TODO
        }
    }
}
