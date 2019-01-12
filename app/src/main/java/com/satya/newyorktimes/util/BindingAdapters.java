package com.satya.newyorktimes.util;

import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleInvisible")
    public static void visibleInvisible(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("imageUrl")
    public static void bindImage(ImageView imageView, String url) {
        if (imageView != null && url != null) {
            ImageLoader.INSTANCE.loadImage(imageView.getContext(), url, imageView);
        }
    }

    @BindingAdapter("imageUrl")
    public static void bindImage(ImageView imageView, int resource) {
        if (imageView != null && resource != 0) {
            ImageLoader.INSTANCE.loadImage(imageView.getContext(), resource, imageView);
        }
    }

    @BindingAdapter("imageUrl")
    public static void bindImage(ImageView imageView, Uri uri) {
        if (imageView != null && uri != null) {
            ImageLoader.INSTANCE.loadImage(imageView.getContext(), uri, imageView);
        }
    }

    @BindingAdapter("progressColor")
    public static void setProgressColor(ProgressBar progressBar, int color) {
        if (progressBar != null && color != 0) {
            progressBar.getIndeterminateDrawable()
                    .setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
    }

    @BindingAdapter("android:text")
    public static void setFloat(TextView view, float value) {
        if (Float.isNaN(value)) view.setText("");
        else view.setText(Float.toString(value));
    }

    @BindingAdapter("android:text")
    public static void setText(EditText view, String text) {
        view.setText(text);
        if (text != null) {
            view.setSelection(text.length());
        }
    }

    @BindingAdapter("referenceTime")
    public static void setFloat(RelativeTimeTextView view, String value) {
        long time = -1;
        SimpleDateFormat f = new SimpleDateFormat(Constants.DATE_FORMATE);
        try {
            Date d = f.parse(value.substring(0,10));
            time = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (time >= 0) {
            view.setReferenceTime(time);
        } else {
            view.setText("");
        }

    }
}
