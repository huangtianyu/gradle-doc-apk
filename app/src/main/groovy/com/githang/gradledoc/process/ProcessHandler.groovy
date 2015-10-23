package com.githang.gradledoc.process

import com.githang.gradledoc.datasource.AbstractResponse
import groovy.transform.CompileStatic
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-12-03
 * Time: 22:25
 */
@CompileStatic
abstract class ProcessHandler extends AbstractResponse {
    @Override
    void onUISuccess(String response) {
        Document doc = Jsoup.parse(response)
        Elements elements = doc.select("div[class=table-list-cell]")
        List<Commit> commits = new ArrayList<>(elements.size())
        elements.each { elem ->
            Commit commit = new Commit()
            commit.setTitle(elem.select("p.commit-title").text())
            commit.setMeta(elem.select("div.commit-meta").text())
            commits.add(commit)
        }
        onResult(commits)
    }

    abstract void onResult(List<Commit> commits)
}
