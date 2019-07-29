package ge.edu.freeuni.chatuna.chat

import ge.edu.freeuni.chatuna.model.MessageModel

interface ChatContract {
    interface ChatView {
        fun sendMessage(messsage: MessageModel)
        fun displayHistory(history: List<MessageModel>)
    }

    interface ChatPresenter {
        fun sendMessage(message: MessageModel)
        fun loadChatHistory(senderName: String)
    }

    interface ChatInteractor {
        interface OnFinishListener {
            fun onMessageSent(message: MessageModel)
            fun onHistoryLoaded(history: List<MessageModel>)
        }

        fun sendMessage(message: MessageModel, onFinishListener: OnFinishListener)
        fun loadHistory(senderName: String, onFinishListener: OnFinishListener)
    }
}