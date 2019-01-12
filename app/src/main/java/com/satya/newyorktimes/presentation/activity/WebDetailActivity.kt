package com.satya.newyorktimes.presentation.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.satya.newyorktimes.R
import com.satya.newyorktimes.R.string.dummy_button
import com.satya.newyorktimes.data.model.Status
import com.satya.newyorktimes.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import android.webkit.WebSettings
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_web_detail.*
import android.webkit.WebViewClient


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WebDetailActivity : AppCompatActivity() {
    private var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        url = intent.getStringExtra(ARG_URL)
        initUi()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initUi() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        if (url.isNullOrEmpty()) {
            Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_LONG).show()
            finish()
        } else {
            url?.let { webView.loadUrl(it) }

        }
    }


    override fun onResume() {
        super.onResume()
        hide()
    }

    private fun hide() {
        supportActionBar?.hide()
    }

    companion object {
        const val ARG_URL = "url"
    }

}