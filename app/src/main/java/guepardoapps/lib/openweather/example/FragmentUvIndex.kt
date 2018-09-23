package guepardoapps.lib.openweather.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import es.dmoral.toasty.Toasty
import guepardoapps.lib.openweather.extensions.doubleFormat
import guepardoapps.lib.openweather.models.UvIndex
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_uv_index.*

class FragmentUvIndex : Fragment() {
    private lateinit var uvIndex: UvIndex
    private var subscriptions: Array<Disposable?> = arrayOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) {
            subscriptions = subscriptions.plus(
                    OpenWeatherService.instance.uvIndexPublishSubject
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { response ->
                                        if (response.value != null) {
                                            handleOnUvIndex(response.value as UvIndex)
                                        } else {
                                            if (isAdded) {
                                                context.runOnUiThread {
                                                    Toasty.warning(context, "uv index subscribe was not successfully", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    },
                                    { _ -> }))

            OpenWeatherService.instance.loadUvIndex()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uv_index, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        subscriptions.forEach { x -> x?.dispose() }
        subscriptions = arrayOf()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun handleOnUvIndex(uvIndex: UvIndex) {
        this.uvIndex = uvIndex
        uv_index_value.text = uvIndex.value.doubleFormat(2)
    }
}