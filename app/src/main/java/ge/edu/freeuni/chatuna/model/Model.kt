package ge.edu.freeuni.chatuna.model

data class MessageModel(
        val messageText: String,
        val createDate: String,
        val senderName: String
)

data class HistoryModel(
        val senderName: String,
        val messageCount: Long,
        val date: String
)

