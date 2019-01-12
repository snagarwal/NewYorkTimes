package com.satya.newyorktimes.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.satya.newyorktimes.R
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.data.model.Status
import com.satya.newyorktimes.presentation.adapter.NewsDetailAdapter
import com.satya.newyorktimes.presentation.adapter.NewsItemDetailInteractor
import com.satya.newyorktimes.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news_list.*
import android.content.Intent
import android.widget.Toast
import com.satya.newyorktimes.presentation.activity.WebDetailActivity


private const val ARG_TITLE = "title"
private const val ARG_SECTION = "section"

/**
 * A simple [Fragment] subclass.
 *
 *
 */
class NewsDetailFragment : BaseFragment(), NewsItemDetailInteractor {


    private var newsViewModel: NewsViewModel? = null
    private var title: String? = null
    private var section: String? = null
    private var newsList = arrayListOf<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE, null)
            section = it.getString(ARG_SECTION, null)
        }
        initViewModel()
    }

    private var listener: FragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
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
        showProgress()
        title?.let { newsViewModel?.getNewsByTitle(it) }
        section?.let { newsViewModel?.getNewsFeed(it) }

    }

    private var adapter: NewsDetailAdapter? = null

    private fun initUi() {
        tabs.visibility = View.GONE
        hideEmptyView()
        val layoutManager = LinearLayoutManager(context, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerFeed.context,
            layoutManager.orientation
        )
        context?.let { context ->
            ContextCompat.getDrawable(
                context,
                R.drawable.shap_devider
            )?.let { dividerItemDecoration.setDrawable(it) }
        }
        adapter = NewsDetailAdapter(newsList, title, this)
        recyclerFeed.apply {
            setLayoutManager(layoutManager)
            addItemDecoration(dividerItemDecoration)
            adapter = this@NewsDetailFragment.adapter
        }
    }


    private fun initViewModel() {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel::class.java)
        newsViewModel?.newsFeed?.observe(this, Observer {
            Log.i("SplashActivity", " ${it?.status} ${it?.data}")

            if (it?.data != null) {
                hideProgress()
                it?.let {
                    updateData(it.data)
                }
            }

        })

        newsViewModel?.news?.observe(this, Observer {
            if (it?.data != null) {
                hideProgress()
                updateNews(it.data)

            }
        })

    }

    private fun updateNews(data: News?) {
        data?.let {
            val itr = newsList.iterator()
            while (itr.hasNext()) {
                if (it.title == itr.next().title)
                    itr.remove()
            }
            newsList.add(0, it)
            adapter?.setData(newsList)
        }

    }

    private fun updateData(data: List<News>?) {
        data?.let {
            it.forEach { news ->
                if (news.title != title) {
                    newsList.add(news)
                }
            }
            adapter?.setData(newsList)
        }
    }

    override fun onItemClicked(news: News?) {
        news?.let {
            listener?.openFragment(NewsDetailFragment.newInstance(it.title, it.section))
        }
    }

    override fun onReadMoreClicked(news: News?) {
        openReadMore(news)
    }

    private fun openReadMore(news: News?) {
        news?.url?.let {
            if (it.isNotEmpty()) {
                val i = Intent(context, WebDetailActivity::class.java)
                i.putExtra(WebDetailActivity.ARG_URL, it)
                startActivity(i)
            }
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
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        @JvmStatic
        fun newInstance(title: String, section: String?) =
            NewsDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_SECTION, section)
                }
            }
    }
}
