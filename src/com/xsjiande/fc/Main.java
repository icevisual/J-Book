package com.xsjiande.fc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

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

	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	
	public static void g() throws IOException{
		
		String dir = "F:\\迅雷下载\\图片任务组_20161003_1707";
		
		File f = new File(dir);
		FileWriter fWriter = new FileWriter(new File(dir + "\\index.html" ));
		String[] files = f.list();
		for(String ff : files){
			if(ff.endsWith("gif")){
				fWriter.write("<img src="+ff+">");
				System.out.println(ff);
			}
			File dFile = new File(dir + "\\" + ff );
			dFile.delete();
		}
		fWriter.close();
		
	}
	
	public static void main(String[] args) throws Exception {
//		Main.Connect();
		Main.g();
	}
}
