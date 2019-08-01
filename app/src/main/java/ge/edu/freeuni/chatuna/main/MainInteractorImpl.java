package ge.edu.freeuni.chatuna.main;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.chatuna.App;
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
        chatRepository.getUserIdByName(App.username, new ChatDataSource.GetIdCallback() {

            @Override
            public void onIdLoaded(long id) {
                List<HistoryModel> testData = new ArrayList<>();
                chatRepository.getHistory(id, new ChatDataSource.GetHistoryCallback() {
                    @Override
                    public void onHistoryLoaded(List<HistoryModel> histories) {
                        testData.addAll(histories);
                        onFinishListener.onFinished(testData);
                    }


                    @Override
                    public void onDataNotAvailable() {
                        onFinishListener.onFinished(testData);
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    @Override
    public void handleCurrentUser() {
        chatRepository.getUserIdByName(App.username, new ChatDataSource.GetIdCallback() {
            @Override
            public void onIdLoaded(long id) {
                if (id <= 0) {
                    chatRepository.saveUser(new User(App.username), id1 -> Log.d("", ""));
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
