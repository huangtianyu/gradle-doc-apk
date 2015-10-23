package com.githang.gradledoc.others

import android.os.Bundle
import android.widget.TextView
import com.githang.gradledoc.BuildConfig
import com.githang.gradledoc.R
import com.githang.gradledoc.common.BaseBackActivity
import groovy.transform.CompileStatic

@CompileStatic
class AboutActivity extends BaseBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        TextView version = (TextView) findViewById(R.id.about_version)
        version.setText("版本: V" + BuildConfig.VERSION_NAME)
    }
}
