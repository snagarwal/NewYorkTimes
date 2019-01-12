package com.satya.newyorktimes.util

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * Created by VipulKumar on 2/7/18.
 * helpful extension functions for presentation layer.
 */

/** Get children of a [ViewGroup] */
val ViewGroup.children: List<*>
    get() = (0 until childCount).map { getChildAt(it) }

/** Inflate a [ViewGroup] */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

/** Inflate a [ViewGroup] with DataBinding */
fun ViewGroup.inflateWithDataBinding(
    layoutId: Int,
    attachToRoot: Boolean = false
): ViewDataBinding? {
    return DataBindingUtil
        .inflate(
            LayoutInflater.from(context),
            layoutId, this, attachToRoot
        )
}

/** Apply [FragmentTransaction]s */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

/** Check [Collection] for null or empty */
fun Collection<*>?.isNotNullOrEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

/** Check [Collection] for null or empty */
fun Collection<*>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

fun Bitmap.bitmapToByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    return stream.toByteArray()
}

fun String.toTimeStamp(dateFormat: String): Long {

    val f = SimpleDateFormat(dateFormat)
    try {
        val d = f.parse(this)
        val milliseconds = d.time
        return milliseconds
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return -1

}