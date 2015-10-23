package com.githang.gradledoc.chapter

import android.text.Editable
import android.text.Html

import org.xml.sax.XMLReader

/**
 * 扩展的HTML标签处理者。
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-30
 * Time: 19:12
 */
class ExtendedTagHandler : Html.TagHandler {
    internal var first = true

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag == "li") {
            // TODO 处理列表标签
        } else if (tag == "pre") {
            handlerPre(output)
        } else if (tag == "tr") {
            output.append("\n")
        } else if (tag == "td") {
            output.append("\t")
        }
    }

    private fun handlerPre(output: Editable) {
    }

    companion object {
        private val LOG_TAG = ExtendedTagHandler::class.java.simpleName
    }
}
