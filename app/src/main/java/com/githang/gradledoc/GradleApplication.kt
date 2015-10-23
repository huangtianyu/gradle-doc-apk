package com.githang.gradledoc

import android.app.Application
import android.graphics.Bitmap

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.umeng.analytics.MobclickAgent

import java.io.IOException

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 23:47
 */
class GradleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initUmeng()
        initImageLoader()
    }

    private fun initImageLoader() {
        try {
            val options = DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build()
            val config = ImageLoaderConfiguration.Builder(this).diskCache(LruDiscCache(cacheDir,
                    Md5FileNameGenerator(), 20 * 1024 * 1024.toLong())).memoryCache(LruMemoryCache(8 * 1024 * 1024)).defaultDisplayImageOptions(options).build()
            ImageLoader.getInstance().init(config)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun initUmeng() {
        MobclickAgent.setDebugMode(BuildConfig.DEBUG)
        MobclickAgent.openActivityDurationTrack(false)
        MobclickAgent.updateOnlineConfig(this)

        MobclickAgent.setCatchUncaughtExceptions(true)
    }
}
