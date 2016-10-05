package com.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class BaseBookCatch extends BaseUtil {
	
	

	private String  originalBaseUrl = "";
	

	public void run()  {
		/**
		 * 原始地址
		 */
		String originalUrl = "http://www.xunshu.la/22_22665/14543051.html";
		
		this.setOriginalBaseUrl(originalUrl);
		
		
		System.out.println(this.getOriginalBaseUrl());
		

		String outputFileName = "test.txt";

		try {
			

			File outputFile = new File(outputFileName);

			FileWriter fw = new FileWriter(outputFile);

			
			String connectUrl = originalUrl;
			
			Document docs = null;
			do {
				docs = connect(connectUrl);
				
				String title = this.getSectionName(docs);

				String context = this.getSectionContext(docs);
				
				fw.write(title);
				
				fw.write("\n");
				
				fw.write(context);

			} while (!"".equals(connectUrl = this.getNextPageUrl(docs)));

			fw.close();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public String getSectionContext(Document docs) {
		// TODO Auto-generated method stub
		
		String html = docs.getElementById("content").html();
		html = html.replaceAll("<br /> &nbsp;&nbsp;&nbsp;&nbsp;", "\n\r\n    ");
		html = html.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\n\r\n    ");
		html = html.replaceAll("<br />", "");
		return html;
	}

	public String getSectionName(Document docs) {
		// TODO Auto-generated method stub
		return "\r\t"+docs.getElementsByClass("bookname").get(0).
		getElementsByTag("h1").get(0).text();
	}

	public String getNextPageUrl(Document docs) {
		// TODO Auto-generated method stub

		Elements pageNodes = docs.getElementsByClass("bottem1").get(0).
						getElementsByTag("a");
		if(pageNodes.size() <= 4){
			return "";
		}
		String url = pageNodes.get(4).attr("href");
		url = "";
		return url;
	}

	public void setOriginalBaseUrl(String originalUrl) {
		originalUrl = originalUrl.toLowerCase().trim();
		originalUrl = originalUrl.replaceAll("http(s)?://", "");
		originalUrl = originalUrl.substring(0,originalUrl.indexOf("/") + 1);
		this.originalBaseUrl = "http://"+originalUrl.replaceAll("/", "");
	}

	public String getOriginalBaseUrl() {
		return originalBaseUrl;
	}

}
