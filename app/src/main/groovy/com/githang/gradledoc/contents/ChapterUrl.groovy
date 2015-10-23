package com.githang.gradledoc.contents

import groovy.transform.CompileStatic;

/**
 * 目录中的章节名称及名字
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 13:00
 * FIXME
 */
@CompileStatic
class ChapterUrl {
    String title
    String url

    @Override
    String toString() {
        title
    }
}
