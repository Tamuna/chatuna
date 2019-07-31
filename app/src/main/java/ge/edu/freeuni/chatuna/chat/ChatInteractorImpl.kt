package ge.edu.freeuni.chatuna.chat

import ge.edu.freeuni.chatuna.data.Message
import ge.edu.freeuni.chatuna.data.source.ChatDataSource
import ge.edu.freeuni.chatuna.data.source.ChatRepository
import ge.edu.freeuni.chatuna.model.MessageModel

class ChatInteractorImpl(private val chatRepository: ChatRepository) : ChatContract.ChatInteractor {
    override fun loadHistory(senderName: String, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {

    }

    override fun sendMessage(message: MessageModel, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {
        chatRepository.getUserIdByName(message.senderName, object : ChatDataSource.GetIdCallback {
            override fun onIdLoaded(id: Long) {
                chatRepository.saveMessage(Message(message.messageText, message.createDate, id))
            }

            override fun onDataNotAvailable() {

            }

        })

    }
}