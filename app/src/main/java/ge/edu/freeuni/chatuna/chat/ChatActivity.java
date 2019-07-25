package ge.edu.freeuni.chatuna.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.component.CustomToolbar;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.view_loader)
    ConstraintLayout viewLoader;

    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

    private static final String EXTRA_SENDER_NAME = "EXTRA_SENDER_NAME";

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ChatActivity.class);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, String senderName) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(EXTRA_SENDER_NAME, senderName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setListener(new OnSecondaryItemsClickListenerImpl());
    }

    class OnSecondaryItemsClickListenerImpl implements CustomToolbar.OnSecondaryItemsClickListener {

        @Override
        public void onBackClick() {
            ChatActivity.this.onBackPressed();
        }

        @Override
        public void onDeleteClick() {
            //TOOD
        }
    }
}
