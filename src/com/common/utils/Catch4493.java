package com.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Catch4493 extends BaseCatch {

	public Catch4493(String startUrl) {
		super(startUrl);
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		try {
			Element pageList 		= doc.getElementsByClass("next").get(0);
			String nx = pageList.attr("href");
			Pattern ptn = Pattern.compile("('\\d*.htm')");
			Matcher mh = ptn.matcher(nx);
			if(mh.find()){
				String mac =  mh.group();
				String url = mac.substring(1,mac.length()-1);
				pageList.attr("href", url);
				return pageList;
			}
		
			// 获取下一组的节点
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		try {
			Element imgeNode =  doc.getElementsByClass("picsboxcenter").get(0)
			   .getElementsByTag("img")
			   .first();
			return imgeNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
//		Catch4493 c = new Catch4493("http://www.4493.com/wangluomeinv/28247/1.htm");
//		c.doLoop();
		
		CatchLadyyu cc =new CatchLadyyu("http://www.ladyyu.com/tuku/xingganmeinv/51105.html");// new Catch88xm("http://www.88xm.com/tuku/xinggan/1671.html");
		cc.doLoop();
		//
		//
	}

	
}
