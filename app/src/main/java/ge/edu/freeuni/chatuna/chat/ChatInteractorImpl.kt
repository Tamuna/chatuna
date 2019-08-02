package ge.edu.freeuni.chatuna.chat

import android.util.Log
import ge.edu.freeuni.chatuna.App
import ge.edu.freeuni.chatuna.data.Message
import ge.edu.freeuni.chatuna.data.User
import ge.edu.freeuni.chatuna.data.source.ChatDataSource
import ge.edu.freeuni.chatuna.data.source.ChatRepository
import ge.edu.freeuni.chatuna.model.MessageModel

class ChatInteractorImpl(private val chatRepository: ChatRepository) : ChatContract.ChatInteractor {
    override fun deleteMessage(senderName: String, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {
        chatRepository.getUserIdByName(senderName, object : ChatDataSource.GetIdCallback {
            override fun onDataNotAvailable() {

            }

            override fun onIdLoaded(id: Long) {
                chatRepository.deleteHistoryByPeerIds(id)
                onFinishListener.onHistoryDeleted()
            }
        })
    }

    @Override
    override fun handleCurrentUser(username: String) {
        chatRepository.getUserIdByName(username, object : ChatDataSource.GetIdCallback {
            override fun onIdLoaded(id: Long) {
                if (id <= 0) {
                    chatRepository.saveUser(User(App.username), object : ChatDataSource.InsertUserCallback {
                        override fun onUserInserted(id: Long) {
                        }
                    })
                }
            }

            override fun onDataNotAvailable() {

            }
        });
    }

    override fun loadHistory(senderName: String, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {
        chatRepository.getUserIdByName(senderName, object : ChatDataSource.GetIdCallback {
            override fun onIdLoaded(id: Long) {
                chatRepository.getSingleChatById(id, object : ChatDataSource.GetSingleChatCallback {
                    override fun onDataNotAvailable() {}

                    override fun onSingleChatLoaded(chat: MutableList<MessageModel>?) {
                        chat?.forEach { message ->
                            if (message.isSent) {
                                message.senderName = App.username
                            } else {
                                message.senderName = senderName
                            }

                        }
                        onFinishListener.onHistoryLoaded(chat!!.map { it })
                    }

                })
            }

            override fun onDataNotAvailable() {

            }

        })
    }

    override fun sendMessage(message: MessageModel, onFinishListener: ChatContract.ChatInteractor.OnFinishListener, receiverName: String) {
        Log.d("test_name", message.senderName + " " + receiverName);
        chatRepository.getUserIdByName(receiverName, object : ChatDataSource.GetIdCallback {
            override fun onIdLoaded(receiverId: Long) {
                chatRepository.getUserIdByName(App.username, object : ChatDataSource.GetIdCallback {
                    override fun onIdLoaded(senderId: Long) {
                        chatRepository.saveMessage(Message(message.messageText, message.createDate, senderId, receiverId))
                    }

                    override fun onDataNotAvailable() {

                    }

                })
            }

            override fun onDataNotAvailable() {

            }

        })

    }
}