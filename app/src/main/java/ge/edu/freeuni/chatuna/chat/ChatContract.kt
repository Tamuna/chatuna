package ge.edu.freeuni.chatuna.chat

interface ChatContract {
    interface ChatView {}
    interface ChatPresenter {}
    interface ChatInteractor {
        interface OnFinishListener {}
    }
}