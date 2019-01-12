package com.satya.newyorktimes.presentation.adapter

import android.support.v7.util.DiffUtil
import com.satya.newyorktimes.data.model.News


class NewsDiffCallback(internal var newlist: List<News>, internal var oldList: List<News>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newlist.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newlist[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == (newlist[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}