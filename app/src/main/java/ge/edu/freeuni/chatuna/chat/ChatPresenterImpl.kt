package ge.edu.freeuni.chatuna.chat

import ge.edu.freeuni.chatuna.model.MessageModel

class ChatPresenterImpl(
        private val view: ChatContract.ChatView,
        private val interactor: ChatContract.ChatInteractor
) : ChatContract.ChatPresenter {
    override fun start() {
        view.registerReceiver()
    }

    override fun handleCurrentUser() {
        interactor.handleCurrentUser()
    }

    override fun loadChatHistory(senderName: String) {
        interactor.loadHistory(senderName, OnFinishListenerImpl())
    }

    override fun sendMessage(message: MessageModel) {
        interactor.sendMessage(message, OnFinishListenerImpl())
    }

    inner class OnFinishListenerImpl : ChatContract.ChatInteractor.OnFinishListener {
        override fun onMessageSent(message: MessageModel) {
            view.sendMessage(message)
        }

        override fun onHistoryLoaded(history: List<MessageModel>) {
            view.displayHistory(history)
        }
    }
}