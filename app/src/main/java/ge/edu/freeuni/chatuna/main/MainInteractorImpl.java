package ge.edu.freeuni.chatuna.main;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
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
        List<HistoryModel> testData = new ArrayList<>();
        chatRepository.getHistory(2, new ChatDataSource.GetHistoryCallback() {
            @Override
            public void onHistoryLoaded(List<HistoryModel> histories) {
                testData.addAll(histories);
                Log.d("test", testData.toString());
                onFinishListener.onFinished(testData);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
//        testData.add(new HistoryModel("tamuna", 89, "12/12"));
//        testData.add(new HistoryModel("elene", 89, "12/12"));
    }
}
