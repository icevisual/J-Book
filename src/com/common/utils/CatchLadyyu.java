package com.common.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchLadyyu extends BaseCatch {

	public CatchLadyyu(String startUrl) {
		super(startUrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		//
		try {
			Element nextPageNode 		= doc.getElementsByClass("v_next_fl").get(0)
									.getElementsByTag("a").first();
			return nextPageNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		try {
			Element nextPageNode 		= doc.getElementsByClass("thisclass").get(0)
									.nextElementSibling().getElementsByTag("a").first();
			return nextPageNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		try {
			Element imgeNode =  doc.getElementById("icontent")
			   .getElementsByTag("img")
			   .first();
			return imgeNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) throws Exception {
		CatchLadyyu cc =new CatchLadyyu("http://www.ladyyu.com/tuku/xingganmeinv/49527.html");// new Catch88xm("http://www.88xm.com/tuku/xinggan/1671.html");
		cc.doLoop();
	}
}
