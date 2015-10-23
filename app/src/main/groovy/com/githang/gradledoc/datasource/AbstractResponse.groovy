package com.githang.gradledoc.datasource

import android.os.Handler
import android.os.Looper
import android.os.Message
import groovy.transform.CompileStatic

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 12:51
 * FIXME
 */
@CompileStatic
abstract class AbstractResponse {
    static final int SUCCESS = 0
    static final int FAILED = 1
    static final int FINISH = 3

    private final Handler mUIHandler = new Handler(Looper.getMainLooper(), { Message msg ->
        if (msg.what == SUCCESS) {
            onUISuccess((String) msg.obj)
        } else if (msg.what == FAILED) {
            onUIFailed((Throwable) msg.obj)
        } else if (msg.what == FINISH) {
            onUIFinish()
        } else {
            return false
        }
        return true
    } as Handler.Callback)

    final void onSuccess(String article) {
        mUIHandler.obtainMessage(SUCCESS, article).sendToTarget()
    }

    final void onFailure(String response = "", Throwable e) {
        mUIHandler.obtainMessage(FAILED, e).sendToTarget()
    }

    final void onFinish() {
        mUIHandler.obtainMessage(FINISH).sendToTarget()
    }

    String handleResponse(String response) {
        return response
    }

    abstract void onUISuccess(String article)

    abstract void onUIFailed(Throwable e)

    abstract void onUIFinish()
}
