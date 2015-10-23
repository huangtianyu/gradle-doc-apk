package com.githang.gradledoc.common

import android.os.Bundle
import android.view.MenuItem
import groovy.transform.CompileStatic

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-12-03
 * Time: 21:59
 */
@CompileStatic
class BaseBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
