package ge.edu.freeuni.chatuna.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.App;
import ge.edu.freeuni.chatuna.Injection;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.component.CustomToolbar;
import ge.edu.freeuni.chatuna.model.MessageModel;

public class ChatActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener,
        WifiP2pManager.ConnectionInfoListener, ChatContract.ChatView {

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

    @OnClick(R.id.btn_send)
    void onSendClick() {
        String message = etMessageInput.getText().toString();
        etMessageInput.setText("");
        if (!message.isEmpty()) {
            presenter.sendMessage(new MessageModel(message, new Date(), App.username, true));
            adapter.bindSingleItem(new MessageModel(message, new Date(), App.username, true));
        }
    }

    @BindView(R.id.layout_message_input)
    ConstraintLayout layoutMessageInput;

    private ChatRecyclerAdapter adapter;
    private ChatContract.ChatPresenter presenter;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    private List<WifiP2pDevice> peers = new ArrayList<>();
    private String[] deviceNames;
    private WifiP2pDevice[] devices;

    private FoundPeersRecyclerAdapter foundPeersRecyclerAdapter;

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
        if (isHistory) {
            presenter.loadChatHistory(senderName);
        }

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

            List<String> peersList = Arrays.asList(deviceNames);
            foundPeersRecyclerAdapter.bindData((ArrayList)peersList);
        }

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        final InetAddress groupOwner = info.groupOwnerAddress;

        if (info.groupFormed && info.isGroupOwner) {

        } else {

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
                    Log.d("test", "Connected to " + device.deviceName);
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("test", "Not connected to " + device.deviceName);
                }
            });
            //TODO
        }
    }
}
