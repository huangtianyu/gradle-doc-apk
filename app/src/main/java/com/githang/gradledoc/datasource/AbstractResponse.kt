package com.githang.gradledoc.datasource

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 12:51
 * FIXME
 */
abstract class AbstractResponse {

    private val mUIHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            if (msg.what == SUCCESS) {
                onUISuccess(msg.obj as String)
            } else if (msg.what == FAILED) {
                onUIFailed(msg.obj as Throwable)
            } else if (msg.what == FINISH) {
                onUIFinish()
            } else {
                return false
            }
            return true
        }
    })

    fun onSuccess(article: String) {
        mUIHandler.obtainMessage(SUCCESS, article).sendToTarget()
    }

    fun onFailure(response: String, e: Throwable) {
        mUIHandler.obtainMessage(FAILED, e).sendToTarget()
    }

    fun onFinish() {
        mUIHandler.obtainMessage(FINISH).sendToTarget()
    }

    open fun handleResponse(response: String): String {
        return response
    }

    abstract fun onUISuccess(article: String)

    abstract fun onUIFailed(e: Throwable)

    abstract fun onUIFinish()

    companion object {
        internal val SUCCESS = 0
        internal val FAILED = 1
        internal val FINISH = 3
    }
}
