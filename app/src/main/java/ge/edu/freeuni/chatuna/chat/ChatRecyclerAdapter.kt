package ge.edu.freeuni.chatuna.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import ge.edu.freeuni.chatuna.R
import ge.edu.freeuni.chatuna.model.MessageModel

class ChatRecyclerAdapter : RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder>() {
    private val data: ArrayList<MessageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    fun bindData(history: MutableList<MessageModel>) {
        data.clear()
        data.addAll(history)
        notifyDataSetChanged()
    }

    fun bindSingleItem(messsage: MessageModel) {
        data.add(messsage)
        notifyItemChanged(data.size - 1)
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.tv_date)
        lateinit var tvDate: TextView

        @BindView(R.id.tv_message)
        lateinit var tvMessage: TextView

        @BindView(R.id.layput_message_holder)
        lateinit var layoutMessageHolder: LinearLayout

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindView(message: MessageModel) {
            if (message.isSent) {
                tvMessage.background = itemView.context.getDrawable(R.drawable.shape_sent_message)
                layoutMessageHolder.gravity = Gravity.END
            } else {
                tvMessage.background = itemView.context.getDrawable(R.drawable.shape_received_message)
                layoutMessageHolder.gravity = Gravity.START
            }
            tvMessage.text = message.messageText
            tvDate.text = message.createDate.toString().substring(0, 19)
        }
    }
}