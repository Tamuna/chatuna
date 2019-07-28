package ge.edu.freeuni.chatuna.chat

class ChatPresenterImpl(
        private val chatView: ChatContract.ChatView,
        private val chatInteractor: ChatContract.ChatInteractor
) : ChatContract.ChatPresenter {

    inner class OnFinishListenerImpl : ChatContract.ChatInteractor.OnFinishListener {
    }
}