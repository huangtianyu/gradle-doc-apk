package com.githang.gradledoc.others;

import android.os.Bundle;
import android.widget.TextView;

import com.githang.gradledoc.BuildConfig;
import com.githang.gradledoc.R;
import com.githang.gradledoc.common.BaseBackActivity;
import com.umeng.analytics.MobclickAgent;

public class AboutActivity extends BaseBackActivity {
    private static final String LOG_TAG = AboutActivity.class.getSimpleName();

    private TextView mVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mVersion = (TextView) findViewById(R.id.about_version);
        setVersion();
    }

    private void setVersion() {
        mVersion.setText("版本: v" + BuildConfig.VERSION_NAME);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(LOG_TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(LOG_TAG);
        MobclickAgent.onResume(this);
    }
}
