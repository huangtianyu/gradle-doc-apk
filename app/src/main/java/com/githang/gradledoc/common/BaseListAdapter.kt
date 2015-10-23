package com.githang.gradledoc.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-12-03
 * Time: 22:31
 */
abstract class BaseListAdapter<T>(private val mContext: Context, private val mData: List<T>, private val mLayoutId: Int) : BaseAdapter() {

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): T {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = View.inflate(mContext, mLayoutId, null)
        }
        initItemView(position, convertView!!)
        return convertView
    }

    abstract fun initItemView(position: Int, itemView: View)
}
