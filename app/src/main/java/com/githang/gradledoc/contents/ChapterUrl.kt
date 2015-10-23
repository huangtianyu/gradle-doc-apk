package com.githang.gradledoc.contents

/**
 * 目录中的章节名称及名字
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 13:00
 * FIXME
 */
class ChapterUrl {
    var title: String? = null
    var url: String? = null

    override fun toString(): String {
        return title!!
    }
}
