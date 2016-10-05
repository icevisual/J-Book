package com.common.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchMm131 extends BaseCatch {

	public CatchMm131(String startUrl) {
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
			Element next =  doc.getElementsByClass("page_now").get(0).nextElementSibling();
			if(!"a".equals(next.tag()) ){
				return next;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementsByClass("content-pic").get(0).getElementsByTag("img").get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static void main(String[] args) throws Exception {
		
		downloadURLContent("http://img1.mm131.com/pic/1672/1.jpg", "D:/");
		
		/*CatchMm131 c = new CatchMm131("http://www.mm131.com/xinggan/1672.html");
		c.doLoop();*/
	}

}
