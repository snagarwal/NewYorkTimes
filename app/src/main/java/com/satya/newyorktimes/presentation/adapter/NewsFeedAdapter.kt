package com.satya.newyorktimes.presentation.adapter


/*
 *
 *
 * Recycler view adapter with multiple view type
 */

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide.init
import com.satya.newyorktimes.R
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.databinding.ItemNewsFeedBannerBinding
import com.satya.newyorktimes.databinding.ItemNewsFeedThumbBinding
import com.satya.newyorktimes.util.inflateWithDataBinding

class NewsFeedAdapter(
    data: ArrayList<News>,
    private val newItemInteractor: NewsItemInteractor
) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {
    private var inflater: LayoutInflater? = null
    private val newsList = arrayListOf<News>()

    init {
        newsList.clear()
        newsList.addAll(data)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        when (viewType) {
            ITEM_LARGE_BANNER -> {
                return ViewHolderNewsFeedBanner(parent.inflateWithDataBinding(R.layout.item_news_feed_banner) as ItemNewsFeedBannerBinding)
            }
            else -> {

                return ViewHolderNewsFeedThumb(parent.inflateWithDataBinding(R.layout.item_news_feed_thumb) as ItemNewsFeedThumbBinding)
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            ITEM_LARGE_BANNER -> (holder as ViewHolderNewsFeedBanner).bind(newsList[position])
            else -> (holder as ViewHolderNewsFeedThumb).bind(newsList[position])
        }

    }


    override fun getItemViewType(position: Int): Int {
        return if (position % 14 == 0 || position % 14 == 4 || position % 14 == 10) return ITEM_LARGE_BANNER else ITEM_SQUARE_THUMB
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(news: News)
    }

    inner class ViewHolderNewsFeedBanner(private val binding: ItemNewsFeedBannerBinding) :
        ViewHolder(binding.root) {
        override fun bind(news: News) {
            binding.news = news
            binding.itemClickListener = View.OnClickListener {
                newItemInteractor.onItemClicked(binding.news)
            }
        }

    }

    inner class ViewHolderNewsFeedThumb(private val binding: ItemNewsFeedThumbBinding) :
        ViewHolder(binding.root) {
        override fun bind(news: News) {
            binding.news = news
            binding.itemClickListener = View.OnClickListener {
                newItemInteractor.onItemClicked(binding.news)
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