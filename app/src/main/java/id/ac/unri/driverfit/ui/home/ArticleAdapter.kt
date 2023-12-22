package id.ac.unri.driverfit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.unri.driverfit.R
import id.ac.unri.driverfit.databinding.ItemArticleBinding
import id.ac.unri.driverfit.domain.model.Article

class ArticleAdapter :
    ListAdapter<Article, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(transaction: Article)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // berfungsi untuk menghubungkan data dengan view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(item)
                }
            }

            Glide.with(binding.root.context)
                .load(item.image)
                .placeholder(R.drawable.profile_image_placeholder)
                .into(binding.ivImageArticle)

            binding.tvTitleArticle.text = item.title
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(
                    oldData: Article, newData: Article
                ): Boolean {
                    return oldData.id == newData.id
                }

                override fun areContentsTheSame(
                    oldData: Article, newData: Article
                ): Boolean {
                    return oldData == newData
                }
            }
    }
}
