package ru.notesapp

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class NotesAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(2f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))

        builder.setDiskCache(
            DiskLruCacheFactory(
                context.cacheDir.absolutePath + "/image_cache",
                250L * 1024 * 1024
            )
        )

        builder.setLogLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.ERROR)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = EntryPointAccessors.fromApplication(context, GlideEntryPoint::class.java)
            .okHttpClient()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GlideEntryPoint {
    fun okHttpClient(): OkHttpClient
}
