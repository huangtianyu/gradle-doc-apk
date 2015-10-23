package com.githang.gradledoc.others

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView

import com.githang.gradledoc.R
import com.githang.gradledoc.common.BaseBackActivity
import com.umeng.analytics.MobclickAgent

class AboutActivity : BaseBackActivity() {

    private var mVersion: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        mVersion = findViewById(R.id.about_version) as TextView
        setVersion()
    }

    private fun setVersion() {
        val info = this.applicationInfo
        mVersion!!.text = "版本: "
        try {
            val packageInfo = packageManager.getPackageInfo(info.packageName, 0)
            mVersion!!.append(" V")
            mVersion!!.append(packageInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }


    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(LOG_TAG)
        MobclickAgent.onPause(this)
    }

    public override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(LOG_TAG)
        MobclickAgent.onResume(this)
    }

    companion object {
        private val LOG_TAG = AboutActivity::class.java.simpleName
    }
}
