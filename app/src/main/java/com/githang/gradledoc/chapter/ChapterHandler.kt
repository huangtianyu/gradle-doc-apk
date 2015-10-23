package com.githang.gradledoc.chapter

import com.githang.gradledoc.datasource.AbstractResponse

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 21:36
 * 文章处理
 */
abstract class ChapterHandler : AbstractResponse() {
    override fun handleResponse(response: String): String {
        val doc = Jsoup.parse(response)
        val chapter = doc.select("div.chapter").first()
        val title = chapter.select("div.titlepage").select("h1").text()
        chapter.removeClass("titlepage")
        val preElems = chapter.select("pre")
        for (elem in preElems) {
            elem.html(elem.html().replace("\n".toRegex(), "<br/>").replace(" ".toRegex(), "&nbsp;"))
        }
        return chapter.html()
    }
}
