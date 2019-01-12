package com.satya.newyorktimes.presentation.adapter

import com.satya.newyorktimes.data.model.News

interface NewsItemDetailInteractor : NewsItemInteractor{
   fun onReadMoreClicked(news: News?)
}