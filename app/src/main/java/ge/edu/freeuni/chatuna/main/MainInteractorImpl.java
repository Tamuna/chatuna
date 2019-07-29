package ge.edu.freeuni.chatuna.main;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ge.edu.freeuni.chatuna.data.source.ChatRepository;
import ge.edu.freeuni.chatuna.model.HistoryModel;

public class MainInteractorImpl implements MainContract.MainInteractor {

    public MainInteractorImpl(ChatRepository chatRepository) {
    }

    @Override
    public void getHistory(@NotNull OnFinishListener onFinishListener) {
        //TODO: elene implement this and return real data <3
        List<HistoryModel> testData = new ArrayList();
        testData.add(new HistoryModel("tamuna", 89, "12/12"));
        testData.add(new HistoryModel("elene", 89, "12/12"));
        onFinishListener.onFinished(testData);
    }
}
