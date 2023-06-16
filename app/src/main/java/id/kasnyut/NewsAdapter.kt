package id.kasnyut

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(
    private val context: Context,
    private val newsList: List<NewsItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val newsItem = newsList[position]
                    onItemClick(newsItem.link)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        holder.itemView.findViewById<TextView>(R.id.titleTextView).text = newsItem.title
        val imageView =   holder.itemView.findViewById<ImageView>(R.id.imageView) ;
        // Load and display the image using Glide (ensure you have added Glide dependency)
        Glide.with(holder.itemView.context)
            .load(newsItem.image)
            .into(imageView)
    }

    override fun getItemCount() = newsList.size
}
