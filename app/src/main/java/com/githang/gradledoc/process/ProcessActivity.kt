package com.githang.gradledoc.process

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.githang.gradledoc.R
import com.githang.gradledoc.common.BaseBackActivity
import com.githang.gradledoc.common.BaseListAdapter
import com.githang.gradledoc.datasource.HttpProxy

class ProcessActivity : BaseBackActivity() {
    private var mProgressDialog: ProgressDialog? = null
    private var mListView: ListView? = null
    private var mContext: Context? = null

    private val mProcessHandler = object : ProcessHandler() {
        override fun onResult(commits: List<Commit>) {
            mListView!!.adapter = object : BaseListAdapter<Commit>(mContext!!, commits, R.layout.item_process) {
                override fun initItemView(position: Int, itemView: View) {
                    val commit = getItem(position)
                    (itemView.findViewById(R.id.commit_title) as TextView).text = commit.title
                    (itemView.findViewById(R.id.commit_meta) as TextView).text = commit.meta
                }
            }
        }

        override fun onUIFinish() {
            mProgressDialog!!.dismiss()
        }

        override fun onUIFailed(e: Throwable) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process)

        mListView = findViewById(R.id.translation_process) as ListView
        mListView!!.addFooterView(View(this))
        mListView!!.addHeaderView(View(this))

        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setCancelable(true)
        mProgressDialog!!.setMessage(getString(R.string.loading))
        mProgressDialog!!.setOnCancelListener(object : DialogInterface.OnCancelListener {
            override fun onCancel(dialog: DialogInterface) {
                HttpProxy.getInstance(mContext!!).cancelRequests(mContext!!)
            }
        })

        mProgressDialog!!.show()
        val isCache = HttpProxy.getInstance(this).requestUrl(this, URL_PROCESS, mProcessHandler)
        if (isCache) {
            mProgressDialog!!.show()
            HttpProxy.getInstance(this).forceRequestUrl(this, URL_PROCESS, mProcessHandler)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_chapter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_refresh) {
            mProgressDialog!!.show()
            HttpProxy.getInstance(this).forceRequestUrl(mContext!!, URL_PROCESS, mProcessHandler)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val URL_PROCESS = "https://github.com/msdx/gradledoc/commits/1.12"
    }
}
