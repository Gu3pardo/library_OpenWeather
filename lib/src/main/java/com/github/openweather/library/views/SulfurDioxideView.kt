package com.github.openweather.library.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.baoyz.widget.PullRefreshLayout
import com.github.openweather.library.adapter.SulfurDioxideListAdapter
import com.github.openweather.library.extensions.airPollutionCurrentDateTime
import com.github.openweather.library.services.openweather.OpenWeatherService
import guepardoapps.lib.openweather.R
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

@SuppressLint("CheckResult", "SetTextI18n")
class SulfurDioxideView(context: Context, attrs: AttributeSet?) : CoordinatorLayout(context, attrs) {

    private var progressBar: ProgressBar? = null
    private var fallbackTextView: TextView? = null
    private var pullRefreshLayout: PullRefreshLayout? = null
    private var listView: ListView? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_sulfurdioxide, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        progressBar = findViewById(R.id.lib_sulfurdioxide_progress_bar)
        fallbackTextView = findViewById(R.id.lib_sulfurdioxide_no_data_fallback)
        pullRefreshLayout = findViewById(R.id.pull_lib_sulfurdioxide_refresh_layout)
        listView = findViewById(R.id.lib_sulfurdioxide_list_view)

        pullRefreshLayout?.setOnRefreshListener {
            listView?.visibility = View.GONE
            progressBar?.visibility = View.VISIBLE
            OpenWeatherService.instance.loadSulfurDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        }

        subscriptions = subscriptions.plus(
                OpenWeatherService.instance.sulfurDioxidePublishSubject
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { response ->
                                    pullRefreshLayout?.setRefreshing(false)
                                    progressBar?.visibility = View.GONE
                                    fallbackTextView?.visibility = View.VISIBLE

                                    val sulfurDioxide = response?.value
                                    if (sulfurDioxide != null) {
                                        val sulfurDioxideData = sulfurDioxide.data
                                        if (sulfurDioxideData.isNotEmpty()) {
                                            fallbackTextView?.visibility = View.GONE
                                            listView?.visibility = View.VISIBLE
                                            listView?.adapter = SulfurDioxideListAdapter(context!!, sulfurDioxideData)
                                        }
                                    }
                                },
                                {
                                    pullRefreshLayout?.setRefreshing(false)
                                    progressBar?.visibility = View.GONE
                                    fallbackTextView?.visibility = View.VISIBLE
                                }
                        ))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}