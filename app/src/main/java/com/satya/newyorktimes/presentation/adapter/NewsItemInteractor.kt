package com.satya.newyorktimes.presentation.adapter

import com.satya.newyorktimes.data.model.News

interface NewsItemInteractor{
   fun onItemClicked(news: News?)
}