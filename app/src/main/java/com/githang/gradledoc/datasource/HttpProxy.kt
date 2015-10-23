package com.githang.gradledoc.datasource

import android.content.Context
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 12:19
 * FIXME
 */
class HttpProxy(context: Context) {

    private val mCache: HttpDBCache
    private val mHttpClient: OkHttpClient
    private val mRequestTags: WeakHashMap<Context, MutableList<Int>>

    init {
        mHttpClient = OkHttpClient()
        mHttpClient.setConnectTimeout(15, TimeUnit.SECONDS)
        mHttpClient.setReadTimeout(15, TimeUnit.SECONDS)
        mHttpClient.setWriteTimeout(15, TimeUnit.SECONDS)
        mRequestTags = WeakHashMap<Context, MutableList<Int>>()
        mCache = HttpDBCache.getInstance(context)
    }

    /**
     * 强制从互联网上请求
     * @param context
     * @param url
     * @param resp
     */
    fun forceRequestUrl(context: Context, url: String, resp: AbstractResponse) {
        val tag = TAG_GENERATOR.andIncrement
        saveTag(context, tag)
        mHttpClient.newCall(Request.Builder().url(url).tag(tag).build()).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException) {
                val msg = e.getMessage()!!.toUpperCase(Locale.US)
                if (!msg.contains("CANCELED") && !msg.contains("SOCKET CLOSED")) {
                    resp.onFailure("", e)
                }
                resp.onFinish()
                removeTag(context, tag)
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                if (response.isSuccessful) {
                    try {
                        val body = response.body().string()
                        val handled = resp.handleResponse(body)
                        mCache.saveResponse(url, handled)
                        resp.onSuccess(handled)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        resp.onFinish()
                    }
                } else {
                    onFailure(null, IOException("请求失败"))
                }
                removeTag(context, tag)
            }
        })
    }

    /**
     * 请求页面。
     * @param context
     * @param url
     * @param response
     * @return true 表示从缓存当中获取内容，false表示缓存当在没有内容，通过网络获取。
     */
    fun requestUrl(context: Context, url: String, response: AbstractResponse): Boolean {
        val result = mCache.queryResponse(url)
        if (result != null) {
            response.onSuccess(result)
            response.onFinish()
            return true
        } else {
            forceRequestUrl(context, url, response)
            return false
        }
    }

    /**
     * 取消请求
     * @param context
     */
    fun cancelRequests(context: Context) {
        synchronized (mRequestTags) {
            val tags = mRequestTags[context]
            if (tags != null) {
                for (tag in tags) {
                    mHttpClient.cancel(tag)
                }
                tags.orEmpty()
            }
        }
    }

    private fun saveTag(context: Context, tag: Int) {
        synchronized (mRequestTags) {
            var tags: MutableList<Int>? = mRequestTags[context]
            if (tags == null) {
                tags = ArrayList<Int>()
                mRequestTags.put(context, tags)
            }
            tags.add(tag)
        }
    }

    @Synchronized private fun removeTag(context: Context, tag: Int?) {
        synchronized (mRequestTags) {
            val tags = mRequestTags[context]
            tags?.remove(tag)
        }
    }

    companion object {
        private val TAG_GENERATOR = AtomicInteger(0)
        private var instance: HttpProxy? = null

        @Synchronized fun getInstance(context: Context): HttpProxy {
            if (instance == null) {
                instance = HttpProxy(context)
            }
            return instance!!
        }
    }
}
