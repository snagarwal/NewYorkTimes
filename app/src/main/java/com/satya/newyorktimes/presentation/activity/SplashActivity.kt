package com.satya.newyorktimes.presentation.activity

import android.animation.Animator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.adapters.ViewGroupBindingAdapter.setListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.Log
import android.view.View
import com.satya.newyorktimes.R
import com.satya.newyorktimes.R.string.dummy_button
import com.satya.newyorktimes.data.model.Status
import com.satya.newyorktimes.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {
    private var callDone = false
    private var animDone = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViewModel()
    }

    private var newsViewModel: NewsViewModel? = null

    private fun initViewModel() {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel::class.java)
        newsViewModel?.newsFeed?.observe(this, Observer {
            Log.i("SplashActivity", " ${it?.status} ${it?.data}")

            when (it?.status) {
                Status.ERROR, Status.SUCCESS -> {
                    callDone = true
                    finishSplash()
                }
                else -> {
                }
            }

        })
        newsViewModel?.getNewsFeed()
    }

    private fun finishSplash() {
        if (callDone && animDone){
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        hide()
        animateLogo()
    }

    private fun animateLogo() {
        fullscreenLogo.scaleX = .5f
        fullscreenLogo.scaleY = .5f
        fullscreenLogo.alpha = 0f
        fullscreenLogo.animate()
            .scaleX(1.20f)
            .scaleY(1.20f)
            .alpha(1.0f)
            .setDuration(2000)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    animDone= true
                    finishSplash()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            }).interpolator = LinearOutSlowInInterpolator()
    }

    private fun hide() {
        supportActionBar?.hide()
        fullscreenLogo.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

}