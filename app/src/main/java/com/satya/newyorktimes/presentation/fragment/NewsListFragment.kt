package com.satya.newyorktimes.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.satya.newyorktimes.R
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.data.model.Status
import com.satya.newyorktimes.presentation.adapter.NewsFeedAdapter
import com.satya.newyorktimes.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news_list.*
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DividerItemDecoration
import com.satya.newyorktimes.data.model.Section
import com.satya.newyorktimes.presentation.adapter.NewsItemInteractor
import android.support.design.widget.TabLayout
import android.widget.Toast
import com.satya.newyorktimes.R.string.retry


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewsListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NewsListFragment : BaseFragment(), NewsItemInteractor {


    private var newsViewModel: NewsViewModel? = null
    private val newsList = arrayListOf<News>()
    private var isTabsUpdated = false
    private var adapter: NewsFeedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        retry()
    }


    private fun initUi() {
        val layoutManager = LinearLayoutManager(context, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerFeed.context,
            layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.shap_devider
            )!!
        )
        adapter = NewsFeedAdapter(newsList, this)
        recyclerFeed.apply {
            setLayoutManager(layoutManager)
            addItemDecoration(dividerItemDecoration)
            adapter = this@NewsListFragment.adapter
        }
        buttonRetry.setOnClickListener { retry() }
    }

    private var listener: FragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement FragmentInteractionListener")
        }
    }


    private fun initViewModel() {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel::class.java)
        newsViewModel?.newsFeed?.observe(this, Observer {
            Log.i("NewsListFragment", " news ${it?.status} ${it?.data}")


            when (it?.status) {
                Status.LOADING -> {
                    if (it.data?.isNotEmpty() == true) {
                        hideProgress()
                        updateData(it.data)
                    }
                }
                Status.SUCCESS -> {
                    hideProgress()
                    if (it.data?.isNotEmpty() == true) {
                        updateData(it.data)
                    } else {
                        showEmptyView()
                    }
                }
                Status.ERROR -> {
                    hideProgress()
                    if (it.data?.isNotEmpty() == true) {
                        updateData(it.data)
                        showAlert(it.error?.message)
                    } else {
                        showEmptyView()
                    }
                }

            }

        })

        newsViewModel?.sections?.observe(this, Observer {
            Log.i("SplashActivity", " tabs ${it?.status} ${it?.data}")

            when (it?.status) {

                Status.LOADING, Status.SUCCESS -> {
                    if (it?.data != null) {
                        updateTabs(it.data)

                    }
                }

                Status.ERROR -> {
                }
            }
        })

    }


    private fun updateTabs(data: List<Section>?) {
        if (!isTabsUpdated && data?.isNotEmpty() == true) {
            isTabsUpdated = true
            tabs.removeAllTabs()
            tabs.addTab(
                tabs.newTab().setCustomView(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_tab,
                        tabs,
                        false
                    )
                ).setText("Top Stories")
            )
            data?.forEach {
                val tab = tabs.newTab()
                tab.customView =
                        LayoutInflater.from(context).inflate(R.layout.item_tab, tabs, false)
                tab.text = it.section
                tabs.addTab(tab)
            }
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "Top Stories") {
                    newsViewModel?.getNewsFeed()
                } else {
                    newsViewModel?.getNewsFeed(tab.text.toString())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun updateData(data: List<News>?) {
        data?.let {
            adapter?.setData(data)
        }
    }


    private fun retry() {
        hideEmptyView()
        showProgress()
        newsViewModel?.getNewsFeed()
        isTabsUpdated = false
        newsViewModel?.getAllSections()
    }

    override fun onItemClicked(news: News?) {
        news?.let {
            listener?.openFragment(NewsDetailFragment.newInstance(it.title, it.section))
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showEmptyView() {
        blankView.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        blankView.visibility = View.GONE
    }

    private fun showAlert(message: String?) {
        if (!message.isNullOrEmpty())
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NewsListFragment.
         */
        @JvmStatic
        fun newInstance() =
            NewsListFragment()
    }
}

