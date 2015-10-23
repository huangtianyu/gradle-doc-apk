package com.githang.gradledoc.contents

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

import com.githang.gradledoc.Consts
import com.githang.gradledoc.R
import com.githang.gradledoc.chapter.ChapterActivity
import com.githang.gradledoc.common.BaseActivity
import com.githang.gradledoc.datasource.HttpProxy
import com.githang.gradledoc.others.AboutActivity
import com.githang.gradledoc.process.ProcessActivity
import com.umeng.analytics.MobclickAgent
import com.umeng.update.UmengUpdateAgent


/**
 * 目录。
 * @author Geek_Soledad (msdx.android@qq.com)
 */
class ContentsActivity : BaseActivity() {

    private var mProgressDialog: ProgressDialog? = null
    private var mListView: ListView? = null

    private var mHttpProxy: HttpProxy? = null
    private var mContext: Context? = null
    private val mContentsHandler = object : ContentsHandler() {
        override fun onResult(chapterUrls: List<ChapterUrl>) {
            val adapter = ArrayAdapter(mContext, R.layout.item_contents, chapterUrls)
            mListView!!.adapter = adapter
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
        mContext = this
        mHttpProxy = HttpProxy.getInstance(this)
        supportActionBar!!.setTitle(R.string.app_title)
        setContentView(R.layout.activity_contents)
        mListView = findViewById(R.id.contents) as ListView
        mListView!!.addHeaderView(View(this))
        mListView!!.addFooterView(View(this))
        mListView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val chapterUrl = parent.adapter.getItem(position) as ChapterUrl
                val intent = Intent(mContext, ChapterActivity::class.java)
                intent.putExtra(Consts.TITLE, chapterUrl.title)
                intent.putExtra(Consts.URL, Consts.BASE_URL + chapterUrl.url)
                startActivity(intent)
            }
        }
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setCancelable(true)
        mProgressDialog!!.setMessage(getString(R.string.loading))
        mProgressDialog!!.setOnCancelListener(object : DialogInterface.OnCancelListener {
            override fun onCancel(dialog: DialogInterface) {
                mHttpProxy!!.cancelRequests(mContext!!)
            }
        })

        requestContents()

        UmengUpdateAgent.setUpdateAutoPopup(true)
        UmengUpdateAgent.setUpdateOnlyWifi(false)
        UmengUpdateAgent.update(this)
    }

    private fun requestContents() {
        mProgressDialog!!.show()
        HttpProxy.getInstance(this).requestUrl(this, Consts.USER_GUIDE, mContentsHandler)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_contents, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        when (id) {
            R.id.action_refresh -> {
                mProgressDialog!!.show()
                mHttpProxy!!.forceRequestUrl(mContext!!, Consts.USER_GUIDE, mContentsHandler)
                return true
            }
            R.id.action_about -> {
                startActivity(Intent(mContext, AboutActivity::class.java))
                return true
            }
            R.id.action_process -> {
                startActivity(Intent(mContext, ProcessActivity::class.java))
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

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

    companion object {
        private val LOG_TAG = ContentsActivity::class.java.simpleName
    }
}
