package org.hfzy.util;

import org.hfzy.bean.ItemDtail;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public class HtmlUtil {
    public static String structHtml(ItemDtail storyDetailsEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(storyDetailsEntity.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(storyDetailsEntity.getImage_source()).append("</span>")
                .append("<img src=\"").append(storyDetailsEntity.getImage())
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        //news_content_style.css和news_header_style.css都是在assets里的
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + storyDetailsEntity.getBody().replace("<div class=\"img-place-holder\">", sb.toString());
        return mNewsContent;
    }
}