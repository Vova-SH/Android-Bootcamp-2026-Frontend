package com.teto.planner

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.HttpClient
import javax.inject.Inject


@HiltAndroidApp
class PlannerApplication : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var httpClient: HttpClient

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(httpClient))
            }
            .crossfade(true)
            .build()
    }
}
