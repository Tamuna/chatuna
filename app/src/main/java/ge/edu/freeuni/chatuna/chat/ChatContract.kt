package ge.edu.freeuni.chatuna.chat

import ge.edu.freeuni.chatuna.model.MessageModel

interface ChatContract {
    interface ChatView {
        fun sendMessage(messsage: MessageModel)
        fun displayHistory(history: List<MessageModel>)
        fun registerReceiver()
        fun unregisterReceiver()
        fun redirectToMain()

        interface OnWifiDirectNameChanged {
            fun onNameChanged()
        }
    }

    interface ChatPresenter {
        fun sendMessage(message: MessageModel, senderName: String)
        fun loadChatHistory(senderName: String)
        fun start()
        fun handleCurrentUser(username: String)
        fun deleteHistory(name: String)
    }

    interface ChatInteractor {
        interface OnFinishListener {
            fun onMessageSent(message: MessageModel)
            fun onHistoryLoaded(history: List<MessageModel>)
            fun onHistoryDeleted()
        }

        fun sendMessage(message: MessageModel, onFinishListener: OnFinishListener, receiverName: String)
        fun loadHistory(senderName: String, onFinishListener: OnFinishListener)
        fun handleCurrentUser(username: String)
        fun deleteMessage(senderName: String, onFinishListener: OnFinishListener)
    }
}