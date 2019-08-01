package ge.edu.freeuni.chatuna.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import ge.edu.freeuni.chatuna.R

class FoundPeersRecyclerAdapter(private val onItemClickedListener: OnItemClickListener) : RecyclerView.Adapter<FoundPeersRecyclerAdapter.PeerViewHolder>() {
    interface OnItemClickListener {
        fun onPeerSelected(peerName: Int)
    }

    private val data = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_peer, parent, false)
        return PeerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    fun bindData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }


    inner class PeerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.tv_peer_name)
        lateinit var tvPeerName: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(peerName: String) {
            itemView.setOnClickListener { onItemClickedListener.onPeerSelected(adapterPosition) }
            tvPeerName.text = peerName
        }

    }
}