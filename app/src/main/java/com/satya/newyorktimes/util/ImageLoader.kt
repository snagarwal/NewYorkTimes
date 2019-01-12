package com.satya.newyorktimes.util

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.satya.newyorktimes.R
import jp.wasabeef.glide.transformations.BitmapTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * ImageLoader to load images to image view from different sources.
 */

object ImageLoader {
    fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context)
                .load(url)
                .apply(
                    RequestOptions.bitmapTransform(
                        RoundedCornersTransformation(
                            10,
                            0
                        )
                    ).error(R.drawable.image_loading)
                )
                .into(imageView)
        }
    }

    fun loadImage(context: Context, resource: Int, imageView: ImageView) {
        Glide.with(context)
            .load(resource)
            .into(imageView)
    }

    fun loadImage(context: Context, uri: Uri?, imageView: ImageView) {
        Glide.with(context)
            .load(uri)
            .into(imageView)
    }

//    fun loadImage(context: Context, uri: Uri?, imageView: ImageView, placeHolder: Int, errorResource: Int) {
//        Glide.with(context)
//                .load(uri)
//                .error(errorResource)
//                .into(imageView)
//    }
}
