package ge.edu.freeuni.chatuna.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import ge.edu.freeuni.chatuna.R
import ge.edu.freeuni.chatuna.model.HistoryModel
import java.util.*
import kotlin.collections.ArrayList


class HistoryRecyclerAdapter(private val onItemClickedListener: OnItemClickedListener) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {

    private val data: MutableList<HistoryModel> = ArrayList()

    interface OnItemClickedListener {
        fun onHistoryItemClick(historyModel: HistoryModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    fun bindData(data: List<HistoryModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.tv_last_message_date)
        lateinit var tvLastMessageDate: TextView

        @BindView(R.id.tv_message_count)
        lateinit var tvMessageCount: TextView

        @BindView(R.id.tv_phone_name)
        lateinit var tvPhoneName: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(data: HistoryModel) {
            val date = Date(data.date.toLong()).toString().substring(0, 19)
            tvLastMessageDate.text = date
            tvMessageCount.text = data.messageCount.toString()
            tvPhoneName.text = data.senderName

            itemView.setOnClickListener { onItemClickedListener.onHistoryItemClick(data) }
        }
    }
}