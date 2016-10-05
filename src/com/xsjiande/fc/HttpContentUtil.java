package com.xsjiande.fc;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;
public class HttpContentUtil {
	public static void appendFile(String filePathAndName, String fileContent) {
		try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(filePathAndName, true);
			writer.write(new String(fileContent.getBytes("gbk"),"gbk"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void writeFile(String filePathAndName, String fileContent) {
		try {
			File f = new File(filePathAndName);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(f), "gbk");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			System.out.println("写文件内容操作出错");
			e.printStackTrace();
		}
	}




	public static String strBetween(String source, String findex, String lindex) {
		int fi = source.indexOf(findex);
		if (fi == -1) {
			return "";
		}
		
		int li = source.substring(fi).indexOf(lindex);
		String res ="";
		try {
			res = source.substring(fi + findex.length(), fi + li);
		} catch (Exception e) {
			System.out.println("ERR");
			System.out.println(fi);
			System.out.println(findex.length());
			System.out.println(fi + findex.length());
			System.out.println(li);
			System.out.println(fi + li);
			System.out.println(source.substring(fi));
			System.out.println("END");
		}
		
		return res;
	}

	public static String strBetween(String source, String findex,
			String lindex, int model) {

		return findex + strBetween(source, findex, lindex) + lindex;
	}

	public static String getHtmlContent(String str, String tag, String startStr) {
		int flag = 0;
		if (str.indexOf(startStr) == -1)
			return "";
		String source = str.substring(str.indexOf(startStr));
		String loop = source;
		int index = 0;
		while ((index = loop.indexOf(tag)) != -1) {
			String bf = loop.charAt(index - 1) + "";
			loop = loop.substring(index + 3);
			if (bf.equals("<")) {
				flag++;
			} else if (bf.equals("/")) {
				flag--;
			}
			if (flag == 0) {
				// System.out.println(loop);
				break;
			}
		}
		return source.replace(loop.substring(1), "");
	}

	public static String getContent(String str) {
		int flag = 0;
		if (str.indexOf("<div class=\"detailcontent\">") == -1)
			return "";
		String source = str.substring(str
				.indexOf("<div class=\"detailcontent\">"));
		String loop = source;
		int index = 0;
		while ((index = loop.indexOf("div")) != -1) {
			String bf = loop.charAt(index - 1) + "";
			loop = loop.substring(index + 3);
			if (bf.equals("<")) {
				flag++;
			} else if (bf.equals("/")) {
				flag--;
			}
			if (flag == 0) {
				// System.out.println(loop);
				break;
			}
		}
		return source.replace(loop.substring(1), "").replace(
				strBetween(source, "<div class=\"adrress\"", "</div>", 1), "");
	}

	public static String removeHtmlTags(String source) {
		while (source.indexOf("<") != -1) {
			source = source.replace(strBetween(source, "<", ">", 1), "");
		}
		return source;
	}

	
	public static void getContentOfOnePage(String content,String toFile) throws InterruptedException{
		String fname = toFile;
		String str = content;
		str = strBetween(str, "<div id=\"content\" class=\"Ttxt\">", 
				"</div><br />");
		if("".equals(str) || str.length() < 2000) return;
		String [] replaceStrs = {
				"&nbsp;&nbsp;&nbsp;&nbsp;Ｗww.Ｕ８XＳ.Ｃom u8小说更新最快小说阅读网"
				,"&nbsp;&nbsp;&nbsp;&nbsp;微信关注&amp;quot;和阅读&amp;quot;,发送“免费”即享本书当日免费看<br />"
				,"...&nbsp;&nbsp; ［本章未完，请点击下一页继续阅读！］"
				,"Ｗwω·Ｕ８xs.ｃoｍ ｕ⑻小说更新最快小说阅读网"
				,"!&nbsp;&nbsp;［本章结束］"
				,"&nbsp;&nbsp;［本章结束］"
				," ...&nbsp;&nbsp; ［本章未完，请点击下一页继续阅读！］"
				,"<br />"};
		for (int i = 0; i < replaceStrs.length; i++) {
			str = str.replaceAll(replaceStrs[i], "");
		}
		str = str.replaceAll("&nbsp;&nbsp;", "  ");
		str = str.substring(1);
//		System.out.println(str);
		appendFile(fname, str);
		Thread.sleep(500);
	}
	
	public static void getFileLoop() throws InterruptedException{
		String webBaseUrl = "http://www.u8xs.com/html/15/15599/";
		String encoding = "gbk";
		String fname = "C:\\Users\\lenovo\\Desktop\\text.txt";

		String content = "";
		String jsString = "";
		String sectionName = "";
		String to_next = "";
		String next_page = "7471999.html";
		String to_prev  = "";
		String url ="";
//		appendFile(fname, "\t《机甲天王》全集\r\n\r\n\r\n");
		int i = 0 ;
		while (!"index.html".equals(next_page)) {
			i ++;
			url = webBaseUrl + next_page;
			content = getURLContent(url, encoding);
			jsString = strBetween(content, "$j = jQuery.noConflict();", "function jumpPage() {");
			sectionName = strBetween(jsString,"var chaptername=' ","';");
			to_next = strBetween(jsString,"var to_next = \"","\";");
			next_page = strBetween(jsString,"var next_page = \"","\";");
			to_prev  = strBetween(jsString,"var to_prev = \"","\";");
			System.out.println(sectionName);
			if("".equals(to_prev)){
				appendFile(fname,"\r\n\r\n"+ sectionName+"\r\n\r\n");
				getContentOfOnePage(content, fname);
			}
			while (!"".equals(to_next)) {
				url = webBaseUrl + to_next;
				content = getURLContent(url, encoding);
				jsString = strBetween(content, "$j = jQuery.noConflict();", "function jumpPage() {");
				to_next = strBetween(jsString,"var to_next = \"","\";");
				getContentOfOnePage(content, fname);
			}
			
			if(i>1111){
				break;
			}
		}
		
	}
	
	
	public static void main(String[] args) throws JAXBException, IOException, InterruptedException {
		getFileLoop();
	}

	public static String getURLContent(String url, String encoding) {
		if (url == null || "".equals(url.trim()))
			return null;

		StringBuffer content = new StringBuffer();
		try {
			// 新建URL对象
			URL u = new URL(url);
			InputStream in = new BufferedInputStream(u.openStream());
			InputStreamReader theHTML = new InputStreamReader(in,
					encoding != null ? encoding : "gb2312");
			int c;
			while ((c = theHTML.read()) != -1) {
				content.append((char) c);
			}
		}
		// 处理异常
		catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		return content.toString();
	}
}