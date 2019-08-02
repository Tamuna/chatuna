package ge.edu.freeuni.chatuna.chat

import ge.edu.freeuni.chatuna.model.MessageModel

class ChatPresenterImpl(
        private val view: ChatContract.ChatView,
        private val interactor: ChatContract.ChatInteractor
) : ChatContract.ChatPresenter {
    override fun deleteHistory(name: String) {
        interactor.deleteMessage(name, OnFinishListenerImpl())
    }

    override fun start() {
        view.registerReceiver()
    }

    override fun handleCurrentUser() {
        interactor.handleCurrentUser()
    }

    override fun loadChatHistory(senderName: String) {
        interactor.loadHistory(senderName, OnFinishListenerImpl())
    }

    override fun sendMessage(message: MessageModel, receiverName: String) {
        interactor.sendMessage(message, OnFinishListenerImpl(), receiverName)
    }

    inner class OnFinishListenerImpl : ChatContract.ChatInteractor.OnFinishListener {
        override fun onHistoryDeleted() {
            view.redirectToMain()
        }

        override fun onMessageSent(message: MessageModel) {
            view.sendMessage(message)
        }

        override fun onHistoryLoaded(history: List<MessageModel>) {
            view.displayHistory(history)
        }
    }
}