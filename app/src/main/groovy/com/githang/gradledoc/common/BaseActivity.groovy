package com.githang.gradledoc.common

import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import com.readystatesoftware.systembartint.SystemBarTintManager
import groovy.transform.CompileStatic

/**
 * @author Geek_Soledad (msdx.android@qq.com)
 */
@CompileStatic
class BaseActivity extends ActionBarActivity {
    private static final int ACTION_BAR_BG = 0xffeeeeee as int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        SystemBarTintManager tintManager = new SystemBarTintManager(this)
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true)
        tintManager.setNavigationBarTintEnabled(true)
        // set a custom tint color for all system bars
        tintManager.setTintColor(ACTION_BAR_BG)
    }
}
