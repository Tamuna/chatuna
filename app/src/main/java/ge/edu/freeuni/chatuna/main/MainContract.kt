package ge.edu.freeuni.chatuna.main

import ge.edu.freeuni.chatuna.model.HistoryModel

interface MainContract {
    interface MainView {
        fun onNoDataLoaded()
        fun onDataLoaded(histories: List<HistoryModel>)
    }

    interface MainPresenter {
        fun start()
        fun getHistory()
    }

    interface MainInteractor {
        interface OnFinishListener {
            fun onFinished(histories: List<HistoryModel>)
        }

        interface OnSelfFound {
            fun onFinished()
        }

        fun findSelf(onSelfFound: OnSelfFound)
        fun getHistory(onFinishListener: OnFinishListener)
    }
}