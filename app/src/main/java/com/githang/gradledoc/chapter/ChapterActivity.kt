package com.githang.gradledoc.chapter

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.githang.gradledoc.Consts
import com.githang.gradledoc.R
import com.githang.gradledoc.common.BaseBackActivity
import com.githang.gradledoc.datasource.HttpProxy
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.umeng.analytics.MobclickAgent

class ChapterActivity : BaseBackActivity() {
    private var url: String? = null

    private var mHttpProxy: HttpProxy? = null

    private var mContext: Context? = null

    private var mDocView: TextView? = null
    private var mProgressDialog: ProgressDialog? = null

    private val mChapterHandler = object : ChapterHandler() {
        override fun onUISuccess(content: String) {
            mDocView!!.text = Html.fromHtml(content, URLImageParser(mDocView), ExtendedTagHandler())
        }

        override fun onUIFailed(e: Throwable) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show()
        }

        override fun onUIFinish() {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)
        mContext = this
        mHttpProxy = HttpProxy.getInstance(this)

        val bundle = intent.extras
        url = bundle.getString(Consts.URL)
        supportActionBar!!.title = bundle.getString(Consts.TITLE)
        initViews()

        requestContents()
    }


    private fun requestContents() {
        mProgressDialog!!.show()
        HttpProxy.getInstance(this).requestUrl(this, url, mChapterHandler)
    }


    private fun initViews() {
        mDocView = findViewById(R.id.doc) as TextView
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setCancelable(true)
        mProgressDialog!!.setMessage(getString(R.string.loading))
        mProgressDialog!!.setOnCancelListener(object : DialogInterface.OnCancelListener {
            override fun onCancel(dialog: DialogInterface) {
                mHttpProxy!!.cancelRequests(mContext)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_chapter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_refresh) {
            mProgressDialog!!.show()
            mHttpProxy!!.forceRequestUrl(mContext, url, mChapterHandler)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(LOG_TAG)
        MobclickAgent.onPause(mContext)
    }

    public override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(LOG_TAG)
        MobclickAgent.onResume(mContext)
    }


    inner class URLImageParser(internal var mTextView: TextView) : Html.ImageGetter {

        override fun getDrawable(source: String): Drawable {
            val urlDrawable = URLDrawable()
            Log.d("ChapterActivity", Consts.BASE_URL + source)
            ImageLoader.getInstance().loadImage(Consts.BASE_URL + source, object : SimpleImageLoadingListener() {
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                    urlDrawable.bitmap = loadedImage
                    val width = mTextView.width - 2 * mTextView.totalPaddingRight
                    val height = loadedImage!!.height * width / loadedImage.width
                    urlDrawable.setBounds(0, 0, width, height)
                    mTextView.invalidate()
                    mTextView.text = mTextView.text
                }
            })
            return urlDrawable
        }
    }

    companion object {
        private val LOG_TAG = ChapterActivity::class.java.simpleName
    }
}
