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
import groovy.transform.CompileStatic

@CompileStatic
class ProcessActivity extends BaseBackActivity {
    private static final String URL_PROCESS = "https://github.com/msdx/gradledoc/commits/1.12"
    private ProgressDialog mProgressDialog
    private ListView mListView
    private Context mContext

    private ProcessHandler mProcessHandler = new ProcessHandler() {
        @Override
        void onResult(List<Commit> commits) {
            mListView.setAdapter(new BaseListAdapter<Commit>(mContext, commits, R.layout.item_process) {
                @Override
                void initItemView(int position, View itemView) {
                    Commit commit = getItem(position);
                    ((TextView) itemView.findViewById(R.id.commit_title)).setText(commit.getTitle());
                    ((TextView) itemView.findViewById(R.id.commit_meta)).setText(commit.getMeta());
                }
            })
        }

        @Override
        void onUIFinish() {
            mProgressDialog.dismiss()
        }

        @Override
        void onUIFailed(Throwable e) {
            Toast.makeText(mContext, "$e.message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process)

        mListView = (ListView) findViewById(R.id.translation_process)
        mListView.addFooterView(new View(this))
        mListView.addHeaderView(new View(this))

        mProgressDialog = new ProgressDialog(this)
        mProgressDialog.setCancelable(true)
        mProgressDialog.setMessage(getString(R.string.loading))
        mProgressDialog.setOnCancelListener({
            HttpProxy.getInstance(mContext).cancelRequests(mContext);
        } as DialogInterface.OnCancelListener)

        mProgressDialog.show()
        boolean isCache = HttpProxy.getInstance(this).requestUrl(this, URL_PROCESS, mProcessHandler)
        if (isCache) {
            mProgressDialog.show()
            HttpProxy.getInstance(this).forceRequestUrl(this, URL_PROCESS, mProcessHandler)
        }
    }

    @Override
    boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chapter, menu)
        return true
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId()

        if (id == R.id.action_refresh) {
            mProgressDialog.show()
            HttpProxy.getInstance(this).forceRequestUrl(mContext, URL_PROCESS, mProcessHandler)
            return true
        }
        super.onOptionsItemSelected(item)
    }
}
