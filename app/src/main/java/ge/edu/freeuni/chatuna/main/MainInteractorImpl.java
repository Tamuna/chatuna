package ge.edu.freeuni.chatuna.main;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.data.source.ChatDataSource;
import ge.edu.freeuni.chatuna.data.source.ChatRepository;
import ge.edu.freeuni.chatuna.model.HistoryModel;

public class MainInteractorImpl implements MainContract.MainInteractor {
    private final ChatRepository chatRepository;

    public MainInteractorImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void getHistory(@NotNull OnFinishListener onFinishListener) {
        //TODO: elene implement this and return real data <3
        List<HistoryModel> testData = new ArrayList();
        testData.add(new HistoryModel("tamuna", 89, "12/12"));
        testData.add(new HistoryModel("elene", 89, "12/12"));
        onFinishListener.onFinished(testData);
        chatRepository.saveUser(new User("Elene"), new ChatDataSource.InsertUserCallback() {
            @Override
            public void onUserInserted(long id) {
                Log.d("test", "Elene " + Long.toString(id));
            }
        });
        chatRepository.saveUser(new User("Tamuna"), new ChatDataSource.InsertUserCallback() {
            @Override
            public void onUserInserted(long id) {
                Log.d("test", "Tamuna " + Long.toString(id));
            }
        });
    }
}
