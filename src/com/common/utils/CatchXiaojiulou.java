package com.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CatchXiaojiulou extends BaseCatch {

	public CatchXiaojiulou(String startUrl) {
		super(startUrl);
		// TODO Auto-generated constructor stub
		this.setSrcFormat("<img src='%s'>");
		this.setDefaultSrcFile("D:\\Desktop\\000.html");
		this.deleteFile(this.defaultSrcFile);
		this.setDownload(false);
		this.setStoreMode("STATIC");
		this.setLoopLimit(800);
		this._init();
	}

	@Override
	public Element getNextGroupNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementsByClass("updown_l").get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getNextPageNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			Element next =  doc.getElementsByClass("pagelist").get(0).getElementsByTag("a").last();
			return next;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Element getPageImgeNode(Document doc) {
		// TODO Auto-generated method stub
		try {
			return doc.getElementById("endtext").getElementsByTag("img").get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
//		BaseUtil.appendFile(filePathAndName, fileContent);
		
		String [] urls = {
				"http://ww4.sinaimg.cn/mw690/005XUZhZgw1esd4q5foihg30a005mnpd.gif"
		};
		for(String str : urls ){
			downloadURLContent(str, "D:\\pic\\gif");
		}
		CatchXiaojiulou.tst();
		
//		int a= 9;
//		if(a==9)
//		return ;
		
//		CatchXiaojiulou c = new CatchXiaojiulou("http://www.513hot.com/xieedongtaitu/21216.html");
//		c.doLoop();
	}

	
	
	public static void  tst () throws Exception{
		String filePathAndName = "D:\\pic\\gif.html";
		File filePathAnd = new File(filePathAndName);
		if(filePathAnd.exists()){
			filePathAnd.delete();
		}
		String dir = "D:\\pic\\gif";
		String host = "http://localhost:91/";
		String str = "<html><header><style type='text/css'>img{width:33%}</style>" +
		"<script src='js/jquery-1.8.3.min.js'></script>"+
		"</header><body>";
		
		BaseUtil.appendFile(filePathAndName, str);
		
		try {
			File dirFile = new File(dir);
			if(dirFile.isDirectory()){
				
				File[] files =  dirFile.listFiles();
				
				for(int i = 0 ;i < files.length ; i ++){
					str= String.format("<img src='%s'>",files[i].getAbsoluteFile());
					BaseUtil.appendFile(filePathAndName, str);
				}
				
			}else{
				System.out.println(dir + "　Not A Dir" );
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Over");
	}
	
	
	public void ttt()throws Exception{
		String deleteFilePath = "D:\\wnmp\\www\\gzb\\public\\delete.txt";
		File deleteFile = new File(deleteFilePath);
		if(deleteFile.exists()){
			BufferedReader rd =new BufferedReader(new FileReader(deleteFile));
			String line = "";
			while((line = rd.readLine())!=null){
				File tmp = new File(line);
				if(tmp.exists()){
					FileWriter fw =new FileWriter(tmp);
					fw.write(0);
					fw.close();
					tmp.delete();
				}
				System.out.println(line);
			}
			rd.close();
			deleteFile.delete();
		}
		
		
		String filePathAndName = "D:\\wnmp\\www\\gzb\\public\\jsrender\\index.html";
		File filePathAnd = new File(filePathAndName);
		if(filePathAnd.exists()){
			filePathAnd.delete();
		}
		String dir = "D:\\pic\\201510021644";
		String host = "http://localhost:91/";
		String str = "<html><header><style type='text/css'>img{width:100%}</style>" +
				
		"<script src='js/jquery-1.8.3.min.js'></script>"+
		"<script>$(function(){ $('img').dblclick(function(){ var src = $(this).attr('src'); $.get('http://localhost:86/test?src='+src,function(d){if(d.status===200){}else{alert(d.message)}},'json') }) })" +
		"</script>"+
		"</header><body>";
		
		BaseUtil.appendFile(filePathAndName, str);
		
		try {
			File dirFile = new File(dir);
			if(dirFile.isDirectory()){
				
				File[] files =  dirFile.listFiles();
				
				for(int i = 0 ;i < files.length ; i ++){
					str= String.format("<img src='%s'>",host + files[i].getName());
					BaseUtil.appendFile(filePathAndName, str);
				}
				
			}else{
				System.out.println(dir + "　Not A Dir" );
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Over");
	}
}
