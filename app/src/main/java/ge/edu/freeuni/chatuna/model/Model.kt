package ge.edu.freeuni.chatuna.model

import java.util.*

data class MessageModel(
        val messageText: String,
        val createDate: Date,
        var senderName: String?,
        val isSent: Boolean
)

data class HistoryModel(
        val senderName: String,
        val messageCount: Long,
        val date: String
)

