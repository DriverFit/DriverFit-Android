package id.ac.unri.driverfit.ui.maps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.ac.unri.driverfit.data.remote.payload.Place
import id.ac.unri.driverfit.databinding.ItemNearestPlacesBinding

class MapsAdapter :
    PagingDataAdapter<Place, MapsAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(
            items: Place,
        )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickCallback = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNearestPlacesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: ItemNearestPlacesBinding,
        itemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val place = getItem(position)
                    if (place != null) {
                        itemClickListener?.onItemClick(
                            place
                        )
                    }
                }
            }
        }

        fun bind(item: Place) = with(binding) {
            tvTitle.text = item.name
            tvVicinity.text = item.vicinity
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(
                oldItem: Place,
                newItem: Place
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Place,
                newItem: Place
            ): Boolean {
                return oldItem.placeId == newItem.placeId
            }
        }
    }
}