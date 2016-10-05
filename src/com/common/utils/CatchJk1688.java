package com.common.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchJk1688 extends BaseCatch {

	public CatchJk1688(String startUrl) {
		super(startUrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			Element next =  doc.getElementsByClass("page").get(0)
			.getElementsByTag("strong").get(0)
			.nextElementSibling();
			if( next.text() != null && "下一页".equals(next.text().trim())){
				return next;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			Element next =  doc.getElementsByClass("page").get(0)
						.getElementsByTag("strong").get(0)
						.nextElementSibling();
			if( next.text() != null && "下一页".equals(next.text().trim())){
				return null;
			}
			return next;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementById("ibox").getElementsByTag("img").get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static void main(String[] args) throws Exception {
		CatchJk1688 c = new CatchJk1688("http://tu.jk1688.com/meinv/6222.html");
		c.doLoop();
	}

}
