package com.common.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchDjzds extends BaseCatch {

	public CatchDjzds(String startUrl) {
		super(startUrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementsByClass("pagelist").get(0)
				.getElementsByClass("thisclass").get(0)
				.nextElementSibling();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementsByClass("imagebody").get(0)
						.getElementsByTag("img").get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		CatchDjzds c = new CatchDjzds("http://www.djzds.com/xinggan/900.html");
		c.doLoop();
	}
}
