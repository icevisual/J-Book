package com.common.utils;

import java.io.File;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchXiabook extends BaseCatch {

	public CatchXiabook(String startUrl) {
		super(startUrl);
		this.setImgSrcFile("D:\\Desktop\\000.txt");
		File f = new File(this.getImgSrcFile()) ;
		f.delete();
		
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}
	public  String doGroup(String url,
			String inFile) throws Exception {
		if("".equals(inFile)){
			inFile = this.getImgSrcFile();
		}
		String basePath = url.substring(0,url.lastIndexOf("/")+1);
		String pageName = url.substring(url.lastIndexOf("/")+1);
		String pageUrl 	= basePath + pageName;
		String ret[]	= null;
		String imgSrc 	= "";
		String nextUrl	= pageUrl;
		while (!"#".equals(nextUrl) && !"".equals(nextUrl) ) {
			ret	= doPage(pageUrl);
			if(BaseCatch.SUCCESS.equals(ret[0])){
				//IF Success
				imgSrc 		= ret[1];
				nextUrl 	= ret[2];
				if(!"".equals(inFile)) appendLine(inFile, imgSrc);//Write ImgSrc Into File
			//	downloadURLContent(imgSrc, this.getImgStoreBasePath());
			}else{
				//FAIL AS FIND NO USEFULL INFOMATION FROM THE PAGE
				return BaseCatch.FAIL;
			}
			if("".equals(nextUrl) || "#".equals(nextUrl)){
				//GROUP OVER AND RETURN NEXT GROUP URL;
				//OR LOOP NEXT GROUP ?
				return ret[3];
			}
			if((basePath + nextUrl).equals(pageUrl) ) return ret[3];
			
			if(nextUrl.startsWith("/")){
				String domain = basePath.substring(7);
				domain = domain.substring(0,domain.indexOf("/"));
				domain = "http://"+domain;
				pageUrl = domain + nextUrl;
			}else{
				pageUrl = basePath + nextUrl;
			}
			Thread.sleep(1000);
		}
		return "";
	}
	public  String[] doPage(String url) throws Exception{
		/**
		 * [0]=>CODE:SUCCESS/FAIL
		 * [1]=>IMG_SRC:STRING
		 * [2]=>NEXT_PAGE_URL:STRING
		 * [3]=>NEXT_GROUP_URL:STRING
		 */
		String [] 	ret = {BaseCatch.SUCCESS,"","",""};
		try {
			Document doc = connect(url);
			Element imgNode	= this.getPageImgeNode(doc);
			String img	= imgNode.html();
			String section = doc.getElementsByClass("bookname").get(0).getElementsByTag("h1").get(0).html();
			if(section.startsWith("正文 ")){
				section = section.split("正文")[1].trim();
				
			}
			img = img.replaceAll("<br />&nbsp;&nbsp;&nbsp;&nbsp;", "  ");
			img = img.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "  ");
			img = img.replaceAll("<br />", "\r\n\r\n");
			img = section + "\r\n\r\n\r\n" + img;
			ret[1] 					= img;
			logger.info("Get  Img  SRC:\t\n" + img);
			
			Element nextPageNode 	= this.getNextPageNode(doc);
			// 获取下一组的节点
			Element nextGroup 		= this.getNextGroupNode(doc);
			ret[3] 					= nextGroup==null?"":nextGroup.attr("href");
			if (nextPageNode == null) {
				logger.info("<<<\tEND AS Next Page URL NOT FOUND");
				return ret;
			}
			String nextUrl = nextPageNode.attr("href");
			ret[2] = nextUrl;
			logger.info("Next Page URL:\t" + nextUrl);
			return ret;
		} catch (NullPointerException e) {
			// 页面结构错误
			System.out.println(url);
			e.printStackTrace();
		}
		ret[0] = BaseCatch.FAIL;
		return ret;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		try {
			return doc.getElementsByClass("bottem").get(0).getElementsByTag("a").get(3);
			// 获取下一组的节点
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		try {
			Element imgeNode =  doc.getElementById("content");
			return imgeNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
//		Catch4493 c = new Catch4493("http://www.4493.com/wangluomeinv/28247/1.htm");
//		c.doLoop();
		
		CatchXiabook cc = new CatchXiabook("http://www.cangqionglongqi.com/yaoshen/2719415.html");
		
		cc.doLoop();
		//
		//
	}

	
}
