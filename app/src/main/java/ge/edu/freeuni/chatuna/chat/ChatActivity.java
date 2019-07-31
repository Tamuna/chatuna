package ge.edu.freeuni.chatuna.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.Injection;
import ge.edu.freeuni.chatuna.R;
import ge.edu.freeuni.chatuna.component.CustomToolbar;
import ge.edu.freeuni.chatuna.model.MessageModel;

public class ChatActivity extends AppCompatActivity implements ChatContract.ChatView {

    @BindView(R.id.view_loader)
    ConstraintLayout viewLoader;

    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;

    @BindView(R.id.et_message_input)
    EditText etMessageInput;

    @OnClick(R.id.btn_send)
    void onSendClick() {
        String message = etMessageInput.getText().toString();
        if (!message.isEmpty()) {
            presenter.sendMessage(new MessageModel(message, new Date(), "tamuna", true));
        }
    }

    @BindView(R.id.layout_message_input)
    ConstraintLayout layoutMessageInput;

    private ChatRecyclerAdapter adapter;
    private ChatContract.ChatPresenter presenter;

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
    }

    @Override
    public void sendMessage(@NotNull MessageModel messsage) {
        adapter.bindSingleItem(messsage);
    }

    @Override
    public void displayHistory(@NotNull List<MessageModel> history) {
        adapter.bindData(history);
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
