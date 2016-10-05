package com.common.utils;

import java.sql.Timestamp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CatchQisuu extends BaseCatch {

	
	public CatchQisuu(String startUrl) {
		super(startUrl);
		this.setImgSrcFile(".\\000.txt");
		this.deleteFile(this.getImgSrcFile());
		this.setDownload(false);
		this.setMaxSearchPageNumber(2);
	}
	
	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * process the match and determine what to get
	 * @return
	 */
	public String[] doMatches(Element e){
		/**
		 * [0]=>CODE:SUCCESS/FAIL 
		 * [1]=>BookName
		 */
		String [] ret = {BaseCatch.FAIL,"NAME","SIZE","SCORE","DATE"};
		// Determine to download or not
		// By Size and score
		
		int starNumber = 0 ;
		float size = 0;
		
		// Get Score
		Elements stars = e.getElementsByTag("em");
		if(!stars.isEmpty()){
			Element star = stars.get(0);
			String className = star.className();
			if(!"".equals(className)){
				try {
					starNumber = Integer.parseInt(className.substring(className.length() - 1, className.length()));
				} catch (NumberFormatException e2) {
				}
			}
		}
		
		// Get Size
		String matches = BaseUtil.regex_first("(\\d+(?:\\.\\d)?)MB", e.html());
		if (null != matches) {
			try {
				size = Float.parseFloat(matches.substring(0, matches.length() - 2));
			} catch (NumberFormatException e2) {
			}
		}
		// Get Date
		String date = BaseUtil.regex_first("\\d{4}-\\d{2}-\\d{2}", e.html());
		
		// Get Book Information
		String c =  "",bookName = "";
		if (!e.getElementsByTag("strong").isEmpty()) {
			c =  e.getElementsByTag("strong").get(0).html();
		}else{
			c =  e.getElementsByTag("a").get(1).html();
		}
		bookName = c.substring(c.indexOf(">") + 1 + 1, c.lastIndexOf("》"));
		
		if(size > 2.5 ){
			if(starNumber > 4 || size > 4){
				ret[0] = BaseCatch.SUCCESS;
				ret[1] ="http://dzs.qisuu.com/txt/"+bookName+".txt";
				ret[2] = size + "MB";
				ret[3] = starNumber + " ";
				ret[4] = date;
				int s = starNumber;
				while(s -- > 0){
					ret[3] += "*";
				}
				// http://dzs.qisuu.com/txt/觉醒日4.txt
			}
		}
		
//		System.out.println(starNumber);
//		System.out.println(size);
//		System.out.println(bookName);
	
		return ret;
	}
	
	/**
	 * 
	 * @param url
	 * @return 1.页面结构不符 =>结束 2.有下一页，有下一组 组内 =>扫下一页 3.有下一页，无下一组 组内,链末 =>扫下一页
	 *         4.无下一页，有下一组 组末 =>扫下一组 5.无下一页，无下一组 组末,链末 =>结束
	 * @throws Exception
	 */
	public String[] doBookPage(String url,String inFile) throws Exception {
		/**
		 * [0]=>CODE:SUCCESS/FAIL [1]=>IMG_SRC:STRING [2]=>NEXT_PAGE_URL:STRING
		 * [3]=>NEXT_GROUP_URL:STRING
		 */
		String[] ret = { BaseCatch.SUCCESS, "", "", "" };
		try {
			Document doc = connect(url);
			Elements imgNodes = this.getPageImgeNodes(doc);
			String[] singleRet = {};
			for(Element element : imgNodes){
				singleRet = this.doMatches(element);
				if(BaseCatch.SUCCESS.equals(singleRet[0])){
					// Get The Books I want to download
					if (!"".equals(inFile))
						appendLine(inFile, singleRet[1]);// Write ImgSrc Into File
					logger.info("Get  Book  SRC:\t" + singleRet[4] +"\t"+ singleRet[1] + "\t"+ singleRet[2] +"\t"+ singleRet[3]);
				}else{
					// No thing to  download
				}
			}
			
			Element nextPageNode = this.getNextPageNode(doc);
			// 获取下一组的节点
			Element nextGroup = this.getNextGroupNode(doc);
			ret[3] = nextGroup == null ? "" : nextGroup.attr("href");
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

	

	public String doGroup(String url, String inFile) throws Exception {
		if ("".equals(inFile)) {
			inFile = this.getImgSrcFile();
		}
		String basePath = url.substring(0, url.lastIndexOf("/") + 1);
		String pageName = url.substring(url.lastIndexOf("/") + 1);
		String pageUrl = basePath + pageName;
		String ret[] = null;
		String nextUrl = pageUrl;
		int searchedPageNumber = 0 ;
		
		while (!"#".equals(nextUrl) && !"".equals(nextUrl)) {
			ret = doBookPage(pageUrl,inFile);
			if (BaseCatch.SUCCESS.equals(ret[0])) {
				searchedPageNumber ++;
				// IF Success
				nextUrl = ret[2];
				if(this.isOutofMaxSearchPageNumber(searchedPageNumber)){
					logger.info("Stop Looping as Page Search Count larger than limitation");
					return "";
				}
				// downloadURLContent(imgSrc, this.getImgStoreBasePath());
			} else {
				// FAIL AS FIND NO USEFULL INFOMATION FROM THE PAGE
				return BaseCatch.FAIL;
			}
			if ("".equals(nextUrl) || "#".equals(nextUrl)) {
				// GROUP OVER AND RETURN NEXT GROUP URL;
				// OR LOOP NEXT GROUP ?
				return ret[3];
			}
			if ((basePath + nextUrl).equals(pageUrl))
				return ret[3];

			if (nextUrl.startsWith("/")) {
				String domain = basePath.substring(7);
				domain = domain.substring(0, domain.indexOf("/"));
				domain = "http://" + domain;
				pageUrl = domain + nextUrl;
			} else {
				pageUrl = basePath + nextUrl;
			}
			Thread.sleep(1000);
		}
		return "";
	}
	
	
	
	@Override
	public Element getNextPageNode(Document doc) {
		try {
			
			Elements as = doc.getElementsByClass("tspage").get(0).getElementsByTag("a");
			int length = as.size();
			while(length -- > 0){
				String html = as.get(length).html();
				if(html.indexOf("下一页") >= 0){
					break;
				}
			}
			return as.get(length);
			// 获取下一组的节点
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public Elements getPageImgeNodes(Document doc) {
		try {
			Elements imgeNode = doc.getElementsByClass("listBox").get(0).getElementsByTag("li");
			return imgeNode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static void main(String[] args) throws Exception {
		
		
		String str = "2016-09-09";

		Timestamp t = Timestamp.valueOf(str+ " 00:00:00");
		System.out.println(t.getTime());
		
		
//		CatchQisuu cc = new CatchQisuu("http://www.qisuu.com/soft/sort01/");
//		cc.doLoop();
		
		
		// Catch4493 c = new
		// Catch4493("http://www.4493.com/wangluomeinv/28247/1.htm");
		// c.doLoop();

		//
		//
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

}
