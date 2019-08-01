package ge.edu.freeuni.chatuna.chat

import android.util.Log
import ge.edu.freeuni.chatuna.App
import ge.edu.freeuni.chatuna.data.Message
import ge.edu.freeuni.chatuna.data.User
import ge.edu.freeuni.chatuna.data.source.ChatDataSource
import ge.edu.freeuni.chatuna.data.source.ChatRepository
import ge.edu.freeuni.chatuna.model.MessageModel

class ChatInteractorImpl(private val chatRepository: ChatRepository) : ChatContract.ChatInteractor {
    @Override
    override fun handleCurrentUser() {
        chatRepository.getUserIdByName(App.username, object : ChatDataSource.GetIdCallback {
            override fun onIdLoaded(id: Long) {
                if (id <= 0) {
                    chatRepository.saveUser( User(App.username), object : ChatDataSource.InsertUserCallback {
                        override fun onUserInserted(id: Long) {
                            Log.d("test", "test");
                        }
                    });
                }
            }

            override fun onDataNotAvailable() {

            }
        });
    }

    override fun loadHistory(senderName: String, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {

    }

    override fun sendMessage(message: MessageModel, onFinishListener: ChatContract.ChatInteractor.OnFinishListener) {
        chatRepository.getUserIdByName(message.senderName, object : ChatDataSource.GetIdCallback {
            override fun onIdLoaded(id: Long) {
                chatRepository.saveMessage(Message(message.messageText, message.createDate, id, id))
            }

            override fun onDataNotAvailable() {

            }

        })

    }
}