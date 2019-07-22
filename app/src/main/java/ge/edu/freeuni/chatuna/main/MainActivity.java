package ge.edu.freeuni.chatuna.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ge.edu.freeuni.chatuna.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @OnClick(R.id.layoutMenuChat)
    void onChatMenuItemClick() {

    }

    @OnClick(R.id.layoutMenuHistory)
    void onHistoryMenuItemClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }
}
