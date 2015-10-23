package com.githang.gradledoc.common

import android.os.Bundle
import android.support.v7.app.ActionBarActivity

import com.readystatesoftware.systembartint.SystemBarTintManager

/**
 * @author Geek_Soledad (msdx.android@qq.com)
 */
open class BaseActivity : ActionBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tintManager = SystemBarTintManager(this)
        // enable status bar tint
        tintManager.isStatusBarTintEnabled = true
        tintManager.setNavigationBarTintEnabled(true)
        // set a custom tint color for all system bars
        tintManager.setTintColor(ACTION_BAR_BG)
    }

    companion object {
        private val ACTION_BAR_BG = -1118482
    }
}
