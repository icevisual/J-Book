package com.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class BaseCatch extends BaseUtil {

	private String imgSrcFile;
	private String imgStoreBasePath;
	private String imgGroupPageUrl;

	/**
	 * 每次循环的最大次数
	 */
	private Integer loopLimit = 20;
	protected static String SUCCESS = "SUCCESS";
	protected static String FAIL = "FAIL";

	/**
	 * 是否下载
	 */
	protected boolean download = false;

	/**
	 * 输出链接的文件地址
	 */
	protected String defaultSrcFile = "C:\\Users\\Administrator\\Desktop\\000.txt";

	/**
	 * 输出链接的格式
	 */
	protected String srcFormat = "%s";

	/**
	 * 每次请求的间隔（毫秒）
	 */
	protected Integer sleepTime = 1000;

	/**
	 * 存储模式 DYNAMIC 动态文件夹 | STATIC 静态
	 */
	protected String storeMode = "DYNAMIC";


	public String getStoreMode() {
		return storeMode;
	}

	public void setStoreMode(String storeMode) {
		this.storeMode = storeMode;
	}

	public Integer getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public String getDefaultSrcFile() {
		return defaultSrcFile;
	}

	public void setDefaultSrcFile(String defaultSrcFile) {
		this.defaultSrcFile = defaultSrcFile;
	}

	public String getSrcFormat() {
		return srcFormat;
	}

	public void setSrcFormat(String srcFormat) {
		this.srcFormat = srcFormat;
	}

	public String formatSrc(String src) {
		return String.format(this.srcFormat, src);
	}

	public BaseCatch(String startUrl) {
		this.setImgGroupPageUrl(startUrl);
		this._init();
	}

	public void _init() {
		// 资源链接存储文件
		this.setImgSrcFile(this.defaultSrcFile);
		// 资源保存路径
		SimpleDateFormat sf = new SimpleDateFormat("YMMddHH");
		this.setImgStoreBasePath("D:/pic/" + sf.format(new Date()));
		// group 次数
		this.setLoopLimit(200);
		// 初始化资源保存路径
		String path = this.getImgStoreBasePath();
		BaseUtil.mkPath(path);
	}

	public abstract Element getPageImgeNode(Document doc);

	public abstract Element getNextPageNode(Document doc);

	public abstract Element getNextGroupNode(Document doc);

	public void doLoop() throws Exception {
		String startPage = this.getImgGroupPageUrl();
		String domain = this.getImgGroupPageUrl();
		domain = domain.substring(0, domain.substring(7).indexOf("/") + 7);
		String bPath = this.getImgGroupPageUrl().substring(0, this.getImgGroupPageUrl().lastIndexOf("/") + 1);
		String nextPage = "";
		String storeBasePath = this.getImgStoreBasePath();
		String groupPath = "";
		int limitn = 1;
		while (!"".equals(startPage) && !"#".equals(startPage)) {

			if ("DYNAMIC".equals(this.getStoreMode().toUpperCase())) {
				groupPath = storeBasePath + "/" + (int) (Math.log(limitn));
				this.mkPath(groupPath);
				this.setImgStoreBasePath(groupPath);
			}

			nextPage = doGroup(startPage);

			if (nextPage != null && !"".equals(nextPage)) {
				if (nextPage.startsWith("/")) {
					nextPage = domain + nextPage;
				} else if (nextPage.startsWith("http:")) {

				} else {
					nextPage = bPath + nextPage;
				}
			}

			logger.info("Last Group:" + startPage);
			startPage = nextPage;
			System.out.println();
			limitn++;
			if (limitn > this.getLoopLimit()) {
				break;
			}
		}
	}

	/**
	 * 
	 * @param url
	 * @return 1.页面结构不符 =>结束 2.有下一页，有下一组 组内 =>扫下一页 3.有下一页，无下一组 组内,链末 =>扫下一页
	 *         4.无下一页，有下一组 组末 =>扫下一组 5.无下一页，无下一组 组末,链末 =>结束
	 * @throws Exception
	 */
	public String[] doPage(String url) throws Exception {
		/**
		 * [0]=>CODE:SUCCESS/FAIL [1]=>IMG_SRC:STRING [2]=>NEXT_PAGE_URL:STRING
		 * [3]=>NEXT_GROUP_URL:STRING
		 */
		String[] ret = { BaseCatch.SUCCESS, "", "", "" };
		try {
			Document doc = connect(url);
			Element imgNode = this.getPageImgeNode(doc);
			String img = "";
			if (imgNode != null) {
				img = imgNode.attr("src");
			}

			if (!"".equals(img) && !img.startsWith("http:")) {
				// http://www.88xm.com/
				img = url.substring(0, url.substring(7).indexOf('/') + 7) + img;
			}
			ret[1] = img;
			logger.info("Get  Img  SRC:\t" + img);

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

	public String doGroup(String url) throws Exception {
		return doGroup(url, "");
	}

	public String doGroup(String url, String inFile) throws Exception {
		if ("".equals(inFile)) {
			inFile = this.getImgSrcFile();
		}
		String basePath = url.substring(0, url.lastIndexOf("/") + 1);
		String pageName = url.substring(url.lastIndexOf("/") + 1);
		String pageUrl = basePath + pageName;
		String ret[] = null;
		String imgSrc = "";
		String nextUrl = pageUrl;
		String srcStr = "";
		while (!"#".equals(nextUrl) && !"".equals(nextUrl)) {
			ret = doPage(pageUrl);
			if (BaseCatch.SUCCESS.equals(ret[0])) {
				// IF Success
				imgSrc = ret[1];
				nextUrl = ret[2];

				srcStr = this.formatSrc(imgSrc);
				if (!"".equals(inFile))
					appendLine(inFile, srcStr);// Write ImgSrc Into File

				if (this.download == true) {
					downloadURLContent(imgSrc, this.getImgStoreBasePath());
				}
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
			} else if (nextUrl.startsWith("http:")) {
				pageUrl = nextUrl;
			} else {
				pageUrl = basePath + nextUrl;
			}
			Thread.sleep(this.getSleepTime());
		}
		return "";
	}

	public boolean ifIgnoal(String path) {
		String[] ACL = { "/meihuo/831.html", "/meihuo/835.html", "/meihuo/838.html", "/meihuo/847.html" };
		for (int i = 0; i < ACL.length; i++) {
			if (ACL[i].equals(path)) {
				return true;
			}
		}
		return false;
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

	public void setImgGroupPageUrl(String imgGroupPageUrl) {
		this.imgGroupPageUrl = imgGroupPageUrl;
	}

	public String getImgGroupPageUrl() {
		return imgGroupPageUrl;
	}

	public void setLoopLimit(Integer loopLimit) {
		this.loopLimit = loopLimit;
	}

	public Integer getLoopLimit() {
		return loopLimit;
	}
}
