package com.common.utils;

import java.io.File;
import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Catch88xm extends BaseCatch {

	public Catch88xm(String startUrl) {
		super(startUrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementsByClass("nr").get(0).getElementsByTag("a").get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		try {
			Element pageList 		= doc.getElementsByClass("page").get(0);
			// 获取下一页的节点pageList
			pageList.nextElementSibling();
			Element nextPageNode 	= pageList.getElementsByClass("page_bg").get(1)
											  .nextElementSibling();
			// 获取下一组的节点
			return nextPageNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		try {
			Element imgeNode =  doc.getElementsByClass("body").get(0)
			   .getElementsByTag("img")
			   .first();
			return imgeNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		Catch88xm c = new Catch88xm("");
		c.doLoop();
	}

	
}
