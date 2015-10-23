package com.githang.gradledoc.contents

import com.githang.gradledoc.datasource.AbstractResponse
import groovy.transform.CompileStatic
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * 目录。
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 13:03
 */
@CompileStatic
abstract class ContentsHandler extends AbstractResponse {
    @Override
    void onUISuccess(String response) {
        Document doc = Jsoup.parse(response)
        Elements elements = doc.select("span.chapter")
        List<ChapterUrl> chapterUrls = new ArrayList<>()
        elements.each { elem ->
            ChapterUrl url = new ChapterUrl()
            url.setUrl(elem.select("a[href]").attr("href"))
            url.setTitle(elem.text())
            chapterUrls.add(url)
        }
        onResult(chapterUrls)
    }

    abstract void onResult(List<ChapterUrl> chapterUrls)
}
