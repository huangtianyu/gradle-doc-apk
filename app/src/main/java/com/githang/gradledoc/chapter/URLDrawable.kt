package com.githang.gradledoc.chapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-30
 * Time: 00:09
 * FIXME
 */
class URLDrawable : BitmapDrawable() {
    protected var mBitmap: Bitmap? = null
    protected var mSrc: Rect? = null

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        mSrc = Rect(0, 0, bitmap.width, bitmap.height)
    }

    override fun draw(canvas: Canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mSrc, bounds, paint)
        }
    }
}
