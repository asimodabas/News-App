package com.asimodabas.haberinolsun.ui.activity.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asimodabas.haberinolsun.R
import com.asimodabas.haberinolsun.domain.model.Article
import com.bumptech.glide.Glide

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private var onItemClickListener: ((Article) -> Unit)? = null

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSource: TextView = itemView.findViewById(R.id.tvSource)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
        private val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)

        fun bind(article: Article) {
            itemView.apply {
                Glide.with(this).load(article.urlToImage).into(ivArticleImage)
                tvSource.text = article.source?.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt
                setOnClickListener {
                    onItemClickListener?.invoke(article)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_article_preview, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}