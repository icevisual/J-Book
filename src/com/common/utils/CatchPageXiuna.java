package com.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CatchPageXiuna extends BaseUtil {

	
	private String imgSrcFile;
	private String imgStoreBasePath;
	
	public CatchPageXiuna(){
		this.setImgSrcFile("C:\\Users\\lenovo\\Desktop\\000.txt");
		this.setImgStoreBasePath("D:\\pic");
	}
	
	
	public  void doloop() throws Exception {
		String startPage = "http://tu.xiuna.com/20140409/23477.html";
		//http://tu.xiuna.com/20140908/23749.html
		int a = 0;
		//23455_9.html
		/*System.out.println(doGroup(startPage));
		
		if(a==0)
		{return;}
		String ret[] = doPage(startPage);
		for (int i = 0; i < ret.length; i++) {
			System.out.println(ret[i]);
		}*/
		
		
		while(!"".equals(startPage)){
			startPage = doGroup(startPage);
			logger.info("Last Group:"+startPage);
			System.out.println();
			a++;
			if(a>200){
				break;
			}
		}
	}

	
	
	/**
	 * 
	 * @param url
	 * @return
	 * 		1.页面结构不符				=>结束
	 * 		2.有下一页，有下一组	组内		=>扫下一页
	 * 		3.有下一页，无下一组	组内,链末	=>扫下一页
	 * 		4.无下一页，有下一组	组末		=>扫下一组
	 * 		5.无下一页，无下一组	组末,链末	=>结束
	 * @throws Exception
	 */
	public  String[] doPage(String url) throws Exception{
		/**
		 * [0]=>CODE:SUCCESS/FAIL
		 * [1]=>IMG_SRC:STRING
		 * [2]=>NEXT_PAGE_URL:STRING
		 * [3]=>NEXT_GROUP_URL:STRING
		 */
		String [] 	ret = {"SUCCESS","","",""};
		try {
			Document doc 			= connect(url);
			String img	 			= doc.getElementById("content")
										 .getElementsByTag("img")
										 .first()
										 .attr("src");
			ret[1] 					= img;
			logger.info("Get  Img  SRC:\t" + img);
			// 获取分页部分代码
			Element pageList 		= doc.getElementsByClass("view_pages").get(0);
			// 获取下一页的节点
			Element nextPageNode 	= pageList.getElementById("nextpage");
			// 获取下一组的节点
			Element nextGroup 		= pageList.getElementsByClass("prenext").get(1);
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
		ret[0] = "FAIL";
		return ret;
	}
	public  String doGroup(String url) throws Exception {
		return doGroup(url,"");
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
		while (!"#".equals(nextUrl)) {
			ret	= doPage(pageUrl);
			if("SUCCESS".equals(ret[0])){
				//IF Success
				imgSrc 		= ret[1];
				nextUrl 	= ret[2];
				appendLine(inFile, imgSrc);//Write ImgSrc Into File
				downloadURLContent(imgSrc, this.getImgStoreBasePath());
			}else{
				//FAIL AS FIND NO USEFULL INFOMATION FROM THE PAGE
				return "FAIL";
			}
			if("".equals(nextUrl)){
				//GROUP OVER AND RETURN NEXT GROUP URL;
				//OR LOOP NEXT GROUP ?
				return ret[3];
			}
			pageUrl = basePath + nextUrl;
			Thread.sleep(1000);
		}
		return "";
	}

	public  void allInfo() throws Exception {
		// String serverRoot = "http://www.mnsfz.com/";list_1_1.html
		String baseUrl = "http://tuku.hxd57.com/meihuo/";
		for (int i = 1; i < 6; i++) {
			String pageName = baseUrl + "list_2_" + i + ".html";
			logger.info("List : " + pageName);
			selection(pageName, "C:\\Users\\lenovo\\Desktop\\000" + i + ".txt");
			logger.info("One List Over: " + pageName);
		}
	}

	public  boolean ifIgnoal(String path) {
		String[] ACL = { "/meihuo/831.html", "/meihuo/835.html",
				"/meihuo/838.html", "/meihuo/847.html" };
		for (int i = 0; i < ACL.length; i++) {
			if (ACL[i].equals(path)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param baseUrl
	 * @throws Exception
	 */
	public  void selection(String baseUrl, String inFile)
			throws Exception {
		// String serverRoot = "http://www.mnsfz.com/";list_1_1.html
		String basePath = "http://tuku.hxd57.com/meihuo/";
		Document doc = Jsoup.connect(baseUrl).get();
		Elements uls = doc.getElementsByClass("picbox");
		for (Element el : uls) {
			Elements links = el.getElementsByTag("a");
			for (Element lk : links) {
				String href = lk.attr("href");
				if (ifIgnoal(href)) {
					continue;
				}
				String pageEnd = href.substring(href.lastIndexOf("/") + 1);
				logger.info("Collection : " + href);
				System.out.println("Collection : " + href + "\n");

			//	loopPage(basePath, pageEnd, inFile);
				// /meihuo/847.html
				// /meihuo/838.html
			}
		}
		//
	}

	public  void main(String[] args) throws Exception {
		// allInfo();
		CatchPageXiuna c = new CatchPageXiuna();
		c.doloop();
		// selection();
		// loopPage("http://tuku.hxd57.com/meihuo/","847.html");
		// loopPage("");
	}


	public void setImgStoreBasePath(String imgStoreBasePath) {
		this.imgStoreBasePath = imgStoreBasePath;
	}


	public String getImgStoreBasePath() {
		return imgStoreBasePath;
	}


	public void setImgSrcFile(String imgSrcFile) {
		this.imgSrcFile = imgSrcFile;
	}


	public String getImgSrcFile() {
		return imgSrcFile;
	}
}
