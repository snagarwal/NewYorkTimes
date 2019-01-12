package com.satya.newyorktimes.presentation.adapter


/*
 *
 *
 * Recycler view adapter with multiple view type
 */

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide.init
import com.satya.newyorktimes.R
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.databinding.ItemNewsDetailBinding
import com.satya.newyorktimes.databinding.ItemNewsFeedThumbBinding
import com.satya.newyorktimes.presentation.adapter.NewsFeedAdapter.Companion.ITEM_LARGE_BANNER
import com.satya.newyorktimes.util.inflateWithDataBinding

class NewsDetailAdapter(
    data: List<News>,
    private val title: String?,
    private val itemInteractor: NewsItemDetailInteractor
) :
    RecyclerView.Adapter<NewsDetailAdapter.ViewHolder>() {

    private val newsList = arrayListOf<News>()

    init {
        newsList.clear()
        newsList.addAll(data)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_LARGE_BANNER -> {
                ViewHolderNewsDetail(parent.inflateWithDataBinding(R.layout.item_news_detail) as ItemNewsDetailBinding)
            }
            else -> {
                ViewHolderNewsThumb(parent.inflateWithDataBinding(R.layout.item_news_feed_thumb) as ItemNewsFeedThumbBinding)
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            ITEM_LARGE_BANNER -> (holder as ViewHolderNewsDetail).bind(newsList[position])
            else -> (holder as ViewHolderNewsThumb).bind(newsList[position])
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (newsList.size > position) {
            if (newsList[position].title == title)
                return ITEM_LARGE_BANNER
        }
        return ITEM_SQUARE_THUMB
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(news: News)
    }

    inner class ViewHolderNewsDetail(private val binding: ItemNewsDetailBinding) :
        ViewHolder(binding.root) {
        override fun bind(news: News) {
            binding.news = news
            binding.readMoreClickListener = View.OnClickListener {
                itemInteractor.onReadMoreClicked(binding.news)
            }
        }

    }

    inner class ViewHolderNewsThumb(private val binding: ItemNewsFeedThumbBinding) :
        ViewHolder(binding.root) {
        override fun bind(news: News) {
            binding.news = news
            binding.itemClickListener = View.OnClickListener {
                itemInteractor.onItemClicked(binding.news)
            }
        }

    }

    fun setData(newList: List<News>) {
        val diffResult = DiffUtil.calculateDiff(NewsDiffCallback(newList, newsList))
        diffResult.dispatchUpdatesTo(this)
        newsList.clear()
        newsList.addAll(newList)
    }

    companion object {
        val ITEM_LARGE_BANNER = 1
        val ITEM_SQUARE_THUMB = 2

    }

}