package com.common.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchMmkaixin extends BaseCatch {

	public CatchMmkaixin(String startUrl) {
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
			return doc.getElementsByClass("page_l").get(0)
						.getElementsByClass("thisclass").get(0)
						.nextElementSibling()
						.getElementsByTag("a").get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		try {
			return doc.getElementsByClass("picbox").get(0)
						.getElementsByTag("img").get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		CatchMmkaixin c = new CatchMmkaixin("http://www.mmkaixin.com/qingchunmeinv/2952.html");
		c.doLoop();
	}

}
