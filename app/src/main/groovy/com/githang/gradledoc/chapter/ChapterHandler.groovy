package com.githang.gradledoc.chapter

import com.githang.gradledoc.datasource.AbstractResponse
import groovy.transform.CompileStatic
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 21:36
 * 文章处理
 */
@CompileStatic
abstract class ChapterHandler extends AbstractResponse {
    @Override
    String handleResponse(String response) {
        Document doc = Jsoup.parse(response)
        Element chapter = doc.select("div.chapter").first()
        chapter.removeClass("titlepage")
        chapter.select("pre").each { elem ->
            elem.html(elem.html().replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp"))
        }
        return chapter.html()
    }
}
