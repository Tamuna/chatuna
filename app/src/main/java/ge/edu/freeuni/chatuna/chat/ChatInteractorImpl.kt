package ge.edu.freeuni.chatuna.chat

import ge.edu.freeuni.chatuna.model.MessageModel

class ChatInteractorImpl : ChatContract.ChatInteractor {
    override fun loadHistory(senderName: String, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {
        //TODO load messages from db
        val test = ArrayList<MessageModel>()
        test.add(MessageModel("hello darling","12/12", "Tamuna"))
        test.add(MessageModel("hello darling","12/12", "Tamuna"))
        test.add(MessageModel("hello darling","12/12", "Tamuna"))
        onFinishListener.onHistoryLoaded(test)
    }

    override fun sendMessage(message: MessageModel, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {
        //TODO save message in db
    }
}