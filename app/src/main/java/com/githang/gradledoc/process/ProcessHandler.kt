package com.githang.gradledoc.process

import com.githang.gradledoc.datasource.AbstractResponse
import org.jsoup.Jsoup
import java.util.*

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-12-03
 * Time: 22:25
 */
abstract class ProcessHandler : AbstractResponse() {
    override fun onUISuccess(response: String) {
        val doc = Jsoup.parse(response)
        val elements = doc.select("div[class=table-list-cell]")
        val commits = ArrayList<Commit>(elements.size)
        for (elem in elements) {
            val commit = Commit()
            commit.title = elem.select("p.commit-title").text()
            commit.meta = elem.select("div.commit-meta").text()
            commits.add(commit)
        }
        onResult(commits)
    }

    abstract fun onResult(commits: List<Commit>)
}
