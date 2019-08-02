package ge.edu.freeuni.chatuna.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.Injection;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.chat.ChatActivity;
import ge.edu.freeuni.chatuna.component.CustomToolbar;
import ge.edu.freeuni.chatuna.model.HistoryModel;

public class MainActivity extends AppCompatActivity implements
        MainContract.MainView {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

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

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.layoutMenuChat)
    void onChatMenuItemClick() {
        navigationView.closeDrawer(Gravity.LEFT);
        ChatActivity.start(this);
    }

    @OnClick(R.id.layoutMenuHistory)
    void onHistoryMenuItemClick() {
        navigationView.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initView();

        presenter = new MainPresenterImpl(new MainInteractorImpl(Injection.
                provideChatRepository(this.getApplicationContext())), this);
        if (hasReadPermissions()) {
            presenter.start();
        }
    }

    private boolean hasReadPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                }
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
