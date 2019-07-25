package ge.edu.freeuni.chatuna.main;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.chat.ChatActivity;
import ge.edu.freeuni.chatuna.component.CustomToolbar;
import ge.edu.freeuni.chatuna.model.HistoryModel;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

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
        navigationView.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initView();

        presenter = new MainPresenterImpl(new MainInteractorImpl(), this);
        presenter.getHistory();
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
            ChatActivity.start(MainActivity.this, historyModel.getSenderName());
        }
    }
}
