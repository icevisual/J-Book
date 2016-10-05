package com.common.utils;

import java.io.IOException;
import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Baidu extends BaseUtil {

	public  static void  getPage(String url ) throws IOException, InterruptedException {
		Document doc 			= connect(url);
		
		System.out.println(doc.baseUri());
		System.out.println(doc.toString());
	}
	
	public static void test(String url) throws IOException, InterruptedException{
		Document doc 			= connect(url);
		Element content_left = doc.getElementById("content_left");	
		Elements sss = content_left.getElementsByClass("c-container");
		Scanner sc = new Scanner(System.in);
		for (Element element : sss) {
			Elements ele =  element.getElementsByTag("a");
			Element a1 = ele.get(0);
			String href = a1.attr("href").toString();
			System.out.println(href);
			getPage(href);
			sc.nextLine();
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		test("http://www.baidu.com/s?wd=%E7%A7%91%E4%BC%A6%E7%94%B5%E5%AD%90%E7%A7%91%E6%8A%80%EF%BC%88%E6%B2%B3%E6%BA%90%EF%BC%89%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8&rsv_spt=1&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&inputT=509&rsv_pq=e3d3d3050002e993&rsv_t=ad07w04fXvzR13aAJXCNy9qG90V10CB60hL1vcFgRjVxSTdz%2B7IR42ELfBno1l%2FrJVjt&rsv_sug3=7&rsv_sug4=415&rsv_n=2&rsv_sug2=0");
	}
}
