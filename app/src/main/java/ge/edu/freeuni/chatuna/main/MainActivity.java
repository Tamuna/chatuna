package ge.edu.freeuni.chatuna.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.chat.ChatActivity;
import ge.edu.freeuni.chatuna.component.CustomToolbar;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_navigation)
    DrawerLayout navigationView;

    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

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
    }

    private void initView() {
        toolbar.setListener(new OnMainItemsClickListenerImpl());
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
}
