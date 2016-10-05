package com.xsjiande.fc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class BaseUtil {
	
	static Logger logger = Logger.getLogger(CatchPageXiuna.class.getName());
	
	/*
	 * 
	 */
	public static Document connect(String pageUrl) throws IOException, InterruptedException{
		return connect(pageUrl,1);
	}
	/**
	 * JSOUP 远程链接
	 * @param pageUrl
	 * @param time
	 * 	首次延时时间（秒）
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Document connect(String pageUrl,int time) throws IOException, InterruptedException{
		Document doc = null;
		try {
			doc = Jsoup.connect(pageUrl).get();
		} catch (SocketTimeoutException e) {
			int t = (int) (100*(Math.pow(time, 1.0/3)-1)/Math.pow(time, 1.0/4));
			logger.info("TIME OUT PGURL:"+pageUrl);
			logger.info("TIME OUT COUNT:"+time+" SELLP:"+(t/10)+" S");
			if(time < 20){
				//10*(x^(1/3)-1)/x^(1/4)
				Thread.sleep(t*100);
				return connect(pageUrl,time+1);
			}else{
				logger.error("SocketTimeoutException:"+pageUrl);
				throw new SocketTimeoutException();
			}
		}
		return doc;
	}
	
	
	
	public static void Connect() {
		try {
			String url = "jdbc:mysql://localhost:3306/nss";
			String user = "root";
			String pwd = "123456";

			// 加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// 建立到MySQL的连接
			Connection conn = DriverManager.getConnection(url, user, pwd);
			// 执行SQL语句
			Statement stmt = conn.createStatement();// 创建语句对象，用以执行sql语言
			stmt.executeQuery("set global character_set_results=utf8;");
			stmt.executeQuery("set  character_set_connection=utf8;");
			ResultSet rs = stmt.executeQuery("select * from narticle");

			// 处理结果集
			while (rs.next()) {
				String name = rs.getString("content");
				System.out.println(name);
			}
			rs.close();// 关闭数据库
			conn.close();
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}

	}
	
	
	/**
	 * 追加写文件
	 * @param filePathAndName
	 * @param fileContent
	 */
	public static void appendLine(String filePathAndName, String fileContent) {
		appendFile(filePathAndName,fileContent+"\r\n");
	}
	
	/**
	 * 追加写文件
	 * @param filePathAndName
	 * @param fileContent
	 */
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
	
	/**
	 * 覆盖写文件
	 * @param filePathAndName
	 * @param fileContent
	 */
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




	/**
	 * 获取两个字符串之间的字符串
	 * @param source
	 * @param findex
	 * @param lindex
	 * @return
	 */
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

	/**
	 * 获取两个字符串之间的字符串,包含这两个字符串
	 * @param source
	 * @param findex
	 * 	short for first index
	 * @param lindex
	 * 	short for last index
	 * @param model
	 * @return
	 */
	public static String strBetween(String source, String findex,
			String lindex, int model) {
		return findex + strBetween(source, findex, lindex) + lindex;
	}

	/**
	 * 获取startStr后的tag标签内容
	 * @param str
	 * @param tag
	 * @param startStr
	 * @return
	 */
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


	/**
	 * 清除HTML标签
	 * @param source
	 * @return
	 */
	public static String removeHtmlTags(String source) {
		while (source.indexOf("<") != -1) {
			source = source.replace(strBetween(source, "<", ">", 1), "");
		}
		return source;
	}
	
	/**
	 * Get Time Based Random Name
	 * @return
	 */
	public static String getTBRName(){
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		return time.format(nowTime)+UUID.randomUUID().toString().substring(0,5);
	}
	
	/**
	 * 字节流读取文件，并保存本地
	 * @param url
	 * @param filePath
	 * 	文件保存路径
	 * @param fileName
	 * 	文件名，为空字符串时，文件名为getTBRName()的值
	 */
	public static void downloadURLContent(String url, String filePath,String fileName) {
		if (url == null || "".equals(url.trim()))
			return ;
		String sExt  = url.substring(url.lastIndexOf(".")+1);
		if(!filePath.endsWith("/")){
			filePath = filePath +"/";
		}
		if(fileName.startsWith("/")){
			fileName = fileName.substring(0,fileName.length()-1);
		}
		if("".equals(fileName)){
			fileName = getTBRName()+"."+sExt;
		}
		if(!fileName.endsWith("."+sExt)){
			fileName = fileName.substring(0,fileName.lastIndexOf(".")+1)+sExt;
		}
		try {
			URL u = new URL(url);
			BufferedInputStream in = new BufferedInputStream(u.openStream());
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(new File(filePath+fileName)));
			byte[] buf = new byte[2048];
			int length = in.read(buf);
			while (length != -1) {
				writer.write(buf, 0, length);
				length = in.read(buf);
			}
			writer.close();
			in.close();
		}
		catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	/**
	 * 字节流读取文件，并保存本地，文件名为getTBRName()的值
	 * @param url
	 * @param filePath
	 */
	public static void downloadURLContent(String url, String filePath) {
		downloadURLContent( url,filePath,"");
	}
	/**
	 * 读取Url内容
	 * @param url
	 * @param encoding
	 * @return
	 */
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
