package com.xsjiande.fc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CatchPage extends BaseUtil {

	public static void loopPage(String basePath, String pageName, String inFile)
			throws IOException, InterruptedException {
		String pageUrl = basePath + pageName;
		Document doc = connect(pageUrl);
		Element pageList = doc.getElementsByClass("itempages2").get(0);
		String nextUrl = pageList.getElementsByTag("a").last().attr("href");
		String imgSrc = doc.getElementsByClass("picture_txt").first()
				.getElementsByTag("img").first().attr("src");
		logger.info("Next Page : " + nextUrl);
		System.out.println(">Img :" + imgSrc);
		System.out.println(">Next Page:" + nextUrl);
		while (!"#".equals(nextUrl)) {
			try {
				pageUrl = basePath + nextUrl;
				doc = connect(pageUrl);
				pageList = doc.getElementsByClass("itempages2").get(0);
				nextUrl = pageList.getElementsByTag("a").last().attr("href");
				imgSrc = doc.getElementsByClass("picture_txt").first()
						.getElementsByTag("img").first().attr("src");
				appendFile(inFile, imgSrc + "\r\n");
				logger.info("Next Page : " + nextUrl);
				System.out.println(">Img :" + imgSrc);
				System.out.println(">Next Page :" + nextUrl);
				Thread.sleep(1000);
			} catch (NullPointerException e) {
				logger.error("NullPointerException" + e.getMessage());
			}
		}
	}

	public static void allInfo() throws Exception {
		// String serverRoot = "http://www.mnsfz.com/";list_1_1.html
		String baseUrl = "http://tuku.hxd57.com/meihuo/";
		for (int i = 1; i < 6; i++) {
			String pageName = baseUrl + "list_2_" + i + ".html";
			logger.info("List : " + pageName);
			selection(pageName, "C:\\Users\\lenovo\\Desktop\\000" + i + ".txt");
			logger.info("One List Over: " + pageName);
		}
	}

	public static boolean ifIgnoal(String path) {
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
	public static void selection(String baseUrl, String inFile)
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

				loopPage(basePath, pageEnd, inFile);
				// /meihuo/847.html
				// /meihuo/838.html
			}
		}
		//
	}

	public static void main(String[] args) throws Exception {
		allInfo();
		// selection();
		// loopPage("http://tuku.hxd57.com/meihuo/","847.html");
		// loopPage("");
	}

	public static void download() throws IOException {
		String urlFile = "C:\\Users\\lenovo\\Desktop\\0002.txt";
		String url = "http://img1.jisu4.com:789/tukuhxd57/uploads/allimg/140923/1-140923105K7.jpg";
		upload(url, "C:\\Users\\lenovo\\Desktop\\");
	}

	public static void upload(String url, String basePath) throws IOException {
		URL u = new URL(url);
		String name = url.substring(url.lastIndexOf("/" + 1));
		InputStream inputStream = u.openStream();
		OutputStream outputStream = new FileOutputStream(basePath + name);
		byte[] bytes = new byte[1024];
		while (inputStream.read(bytes) > 0) {
			outputStream.write(bytes);
		}
		inputStream.close();
		outputStream.close();
	}

}
