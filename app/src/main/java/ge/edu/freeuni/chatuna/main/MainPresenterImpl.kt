package ge.edu.freeuni.chatuna.main

import android.util.Log
import ge.edu.freeuni.chatuna.App
import ge.edu.freeuni.chatuna.model.HistoryModel

class MainPresenterImpl(
        private val interactor: MainContract.MainInteractor,
        private val view: MainContract.MainView
) : MainContract.MainPresenter {
    override fun start() {
        interactor.findSelf(OnSelfFoundImpl())
    }

    override fun getHistory() {
        interactor.getHistory(OnFinishListenerImpl())
    }

    inner class OnFinishListenerImpl : MainContract.MainInteractor.OnFinishListener {
        override fun onFinished(histories: List<HistoryModel>) {
            if (histories.isEmpty()) {
                view.onNoDataLoaded()
            } else {
                view.onDataLoaded(histories)
            }
        }

    }

    inner class OnSelfFoundImpl : MainContract.MainInteractor.OnSelfFound {
        override fun onFinished() {
            interactor.getHistory(OnFinishListenerImpl())
        }
    }
}