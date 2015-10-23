package com.githang.gradledoc.contents

import com.githang.gradledoc.datasource.AbstractResponse

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.ArrayList

/**
 * 目录。
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 13:03
 */
abstract class ContentsHandler : AbstractResponse() {
    override fun onUISuccess(response: String) {
        val doc = Jsoup.parse(response)
        val elements = doc.select("span.chapter")
        val chapterUrls = ArrayList<ChapterUrl>()
        for (elem in elements) {
            val url = ChapterUrl()
            url.url = elem.select("a[href]").attr("href")
            url.title = elem.text()
            chapterUrls.add(url)
        }
        onResult(chapterUrls)
    }

    abstract fun onResult(chapterUrls: List<ChapterUrl>)
}
