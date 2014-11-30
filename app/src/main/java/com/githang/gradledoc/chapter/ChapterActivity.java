package com.githang.gradledoc.chapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.githang.gradledoc.Consts;
import com.githang.gradledoc.R;
import com.githang.gradledoc.datasource.HttpProxy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ChapterActivity extends ActionBarActivity {
    private String title;
    private String url;

    private HttpProxy mHttpProxy;

    private Context mContext;

    private TextView mDocView;
    private ProgressDialog mProgressDialog;

    private ChapterHandler mChapterHandler = new ChapterHandler() {
        @Override
        public void onResult(String title, String doc) {
            mDocView.setText(Html.fromHtml(doc, new URLImageParser(mDocView), null));
        }

        @Override
        public void onFinish() {
            mProgressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        mContext = this;
        mHttpProxy = HttpProxy.getInstance(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(Consts.TITLE);
        url = bundle.getString(Consts.URL);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initViews();

        requestContents();
    }


    private void requestContents() {
        mProgressDialog.show();
        HttpProxy.getInstance(this).requestUrl(this, url, mChapterHandler);
    }


    private void initViews() {
        mDocView = (TextView) findViewById(R.id.doc);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mHttpProxy.cancelRequests(mContext);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chapter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mProgressDialog.show();
            mHttpProxy.forceRequestUrl(mContext, Consts.BASE_URL + url, mChapterHandler);
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class URLImageParser implements Html.ImageGetter {
        TextView mTextView;
        private Drawable mDefaultDrawable;

        public URLImageParser(TextView textView) {
            this.mTextView = textView;
            mDefaultDrawable = getResources().getDrawable(R.drawable.ic_picture);
        }

        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            urlDrawable.drawable = mDefaultDrawable;
            urlDrawable.setBounds(0, 0, urlDrawable.drawable.getIntrinsicWidth(),
                    urlDrawable.drawable.getIntrinsicHeight());
            Log.d("ChapterActivity", Consts.BASE_URL + source);
            ImageLoader.getInstance().loadImage(Consts.BASE_URL + source, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    BitmapDrawable drawable = new BitmapDrawable(null, loadedImage);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());
                    urlDrawable.drawable = drawable;
                    mTextView.requestLayout();
                    mTextView.postInvalidate();
                }
            });
//            urlDrawable.drawable = new BitmapDrawable(null, ImageLoader.getInstance().loadImageSync(Consts.BASE_URL + source));
            return urlDrawable;
        }
    }
}