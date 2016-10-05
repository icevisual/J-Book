package com.xsjiande.fc;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class HttpUtil {
	public static void writeFile(String filePathAndName, String fileContent) {
		try {
			File f = new File(filePathAndName);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(f), "gbk");
			BufferedWriter writer = new BufferedWriter(write);
			// PrintWriter writer = new PrintWriter(new BufferedWriter(new
			// FileWriter(filePathAndName)));
			// PrintWriter writer = new PrintWriter(new
			// FileWriter(filePathAndName));
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			System.out.println("写文件内容操作出错");
			e.printStackTrace();
		}
	}

	public static void createFile(String url, String fname) {
		String str = HttpUtil.getURLContent(url, "utf-8");
		for (int i = 1; i <= 4; i++) {
			str = str.replace("fwcz.jsp?page=" + i, "rent_" + i + ".html");
		}
		for (int i = 1; i <= 4; i++) {
			str = str.replace("fccs.jsp?page=" + i, "sell_" + i + ".html");
		}
		writeFile(fname, str);

	}

	public static void createNFile2(String url, String fname) {
		String baseUrl = "http://www.peise.net/palette/";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 121; i++) {
			url = baseUrl + (i + 1) + ".html";
			String str = HttpUtil.getURLContent(url, "gbk");
			String startStr = "<div class=\"span-8\">";
			str = getHtmlContent(str, "div", startStr);
			sb.append(str);
		}
		String last = "<html><head><link href=\"http://www.peise.net/templates/default/skins/default/global.css\" rel=\"stylesheet\" type=\"text/css\" /></head><body>"
				+ sb.toString() + "</body></html>";
		writeFile(fname, last);
	}

	public static void createNFile1(String url, String fname) {
		String baseUrl = "http://www.yacou.com/ps/new7/";
		String[] a = { "a", "b", "c", "d" };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				url = baseUrl + a[i] + (j + 1) + ".htm";
				String str = HttpUtil.getURLContent(url, "gb2312");
				String startStr = "<td width=\"75%\" valign=\"top\">";
				str = getHtmlContent(str, "td", startStr);
				sb.append(str);
				// System.out.println(str);
				// return;
				// break;
			}
		}
		String last = "<html><body>" + sb.toString() + "</body></html>";

		Pattern pt = Pattern
				.compile("<td width=\"100%\" bgColor=\"#\\w{6}\" height=\"\\d{2}\"><font( color=\"#ffffff\")? size=\"2\">\\d{1,2}</font></td>");
		Matcher matcher = pt.matcher(last);
		while (matcher.find()) {
			String line = matcher.group();
			Pattern pt1 = Pattern.compile("#\\w{6}");
			Matcher matcher1 = pt1.matcher(line);
			matcher1.find();
			String color = matcher1.group();
			String newline = line.replace("</font></td>", ":" + color
					+ "</font></td>");
			last = last.replace(line, newline);
			System.out.println(newline);
		}

		writeFile(fname, last);
	}

	public static void createNFile(String url, String fname) {
		String str = HttpUtil.getURLContent(url, "utf-8");
		String base = "http://localhost:8080/JDFC/";
		String replacePart = url.substring(base.length(), url.length() - 1);
		String replace = fname.substring(0, fname.length() - 6);
		System.out.println("replaceP>>" + replacePart);
		System.out.println("replaceP>>" + replace);
		while (str.indexOf(replacePart) != -1) {
			int len = str.indexOf(replacePart) + replacePart.length();
			String nextIndex = str.substring(len, len + 1);
			System.out.println("next>>" + nextIndex);
			// System.out.println(str.substring(len,len+1));
			// System.out.println("len>>"+len);
			str = str.replace(replacePart + nextIndex, replace + nextIndex
					+ ".html");
		}
		writeFile(fname, str);
	}

	public static String strBetween(String source, String findex, String lindex) {
		int fi = source.indexOf(findex);
		if (fi == -1) {
			return "";
		}
		int li = source.substring(fi).indexOf(lindex);
		return source.substring(fi + findex.length(), fi + li);
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

	public static String getRegex(String shopPhone) {
		String[] regex = { "\\d{4}-\\d{8}", "\\d{4}一\\d{8}", "\\d{11}",
				"\\d{8}" };

		for (int i = 0; i < regex.length; i++) {
			Pattern pt = Pattern.compile(regex[i]);
			Matcher matcher = pt.matcher(shopPhone);
			if (matcher.find()) {
				return regex[i];
			}
		}
		return regex[2];
	}

	public static void createHTMLFile(String url) {
		String str = HttpUtil.getURLContent(url, "utf-8");

		String fileName = url.substring(url.lastIndexOf("/") + 1);
		String shopPic = strBetween(str, "<div class=\"banner\">", "</div>");
		shopPic = shopPic.replace(strBetween(shopPic, "<!--", "-->", 1), "");
		;
		String shopName = strBetween(str, "<title>", "</title>");
		String shopAddr = strBetween(str, "地址：", "<");
		String shopPhone = strBetween(str, "class=\"adrress\"", "</div>"); // 咨询热线

		String shopInfo = getContent(str);
		String modelStr = HttpUtil.getURLContent(
				"http://localhost:8080/JDFC/ceshi.html", "utf-8");
		String basePath = "D:\\Program Files\\MyEcpliseWorkplace\\workplace140320\\JDFC\\WebRoot\\newFile\\";

		if (shopInfo == null || shopInfo.equals("")) {
			System.out.println(String.format("%s can not be replaced!", url));
			return;
		}
		// System.out.println("shopPic>>>>"+shopPic);
		if (shopPic.trim().equals("")) {
			shopPic = strBetween(str, "class=\"banner\">", "</");
		}
		if (shopPic.trim().equals("")) {
			System.out
					.println("--------------------------------------->No Pic>>"
							+ shopName);
		}
		// </span></a>
		if (shopAddr.equals("")) {
			shopAddr = strBetween(str, "地址：", "</span></a>").replace(
					"</strong>", "").replace("<br>", "");
		}
		if (shopPhone.equals("")) {
			shopPhone = strBetween(str, "咨询热线：", "</a>");
		}
		if (shopPhone.equals("")) {
			shopPhone = strBetween(str, "电话：", "</a>");
		}
		if (shopPhone.equals("")) {
			shopPhone = strBetween(str, "热线：", "</a>");
		}

		// 美丽热线：预定热线
		if (shopPhone.indexOf("<") != -1) {
			shopPhone = removeHtmlTags(shopPhone);
		}
		Pattern pt = Pattern.compile(getRegex(shopPhone));
		Matcher matcher = pt.matcher(shopPhone);
		boolean flag = true;
		String phoneStr = "";
		while (matcher.find()) {
			String s = matcher.group();
			if (s.length() >= 8) {
				flag = false;
				phoneStr += String
						.format(
								"<li class=\"tel\"><a href=\"tel:%s\"><span>%s</span></a></li>",
								s, s);
				// System.out.println("phone>>"+s);
			}
		}
		if (flag) {
			System.err.println("No Phone ! Source = >\t" + shopPhone);
		}
		// System.out.println("-------shopAddr->"+shopAddr.trim());
		if (shopAddr.equals("")) {
			System.out.println("----------------------------------No Adr");

			System.out.println("shopPic>>" + shopPic);
			System.out.println("shopName>>" + shopName);
			System.out.println("shopAddr>>" + shopAddr);
			System.out.println("shopPhone>>" + shopPhone);
		}
		// System.out.println(shopPhone);

		// System.out.println(String.format("Success ! %s\n", url));
		modelStr = modelStr.replace("##content##", shopInfo).replace("店家地址",
				shopAddr).replace("##phone##", phoneStr).replace("无标题文档",
				shopName).replace("##pic##", shopPic);
		writeFile(basePath + fileName, modelStr);
	}

	public static void pageTurn() {
		String basePath = "D:\\Program Files\\MyEcpliseWorkplace\\workplace140320\\JDFC\\WebRoot\\sourcepage";
		String httpBase = "http://localhost:8080/JDFC/sourcepage/";
		File dir = new File(basePath);
		if (dir.isDirectory()) {
			File[] child = dir.listFiles();
			for (int i = 0; i < child.length; i++) {
				if (child[i].isFile()) {
					// createHTMLFile(httpBase+child[i].getName());
					System.out.println(String.format(
							"<a href=\"%s\" target=\"_black\">%s</a><br/>",
							child[i].getName(), child[i].getName()));
				}
			}
		}
	}

	public static String removeHtmlTags(String source) {
		while (source.indexOf("<") != -1) {
			source = source.replace(strBetween(source, "<", ">", 1), "");
		}
		return source;
	}

	
	public static void catchContent(){
		String url = "http://www.u8xs.com/html/15/15599/9472847.html";
		String encoding = "gbk";
		String fname = "C:\\Users\\lenovo\\Desktop\\text.txt";
		String str = getURLContent(url, encoding);
		str = strBetween(str, "<div id=\"content\" class=\"Ttxt\">", 
				"</div><br /><center>");
		str = str.replaceAll("&nbsp;&nbsp;", "  ").replaceAll("<br />", "");
		System.out.println(str);
		writeFile(fname, str);
	}
	
	public static void main(String[] args) throws JAXBException, IOException {
System.out.println("Start---");
		catchContent();

		System.out.println("OK");
		int k1 = 0;
		if (k1 == 0) {
			return;
		}

		// String source =
		// "<style=\"border:#DDDDDD solid 1px;\">  <a data-ajax=\"false\" href=\"mapjx.html\" style=\"text-decoration:none; display:block; width:100%; text-align:center;\" ><span>店家地址：建德市新安江街道政法路1号广信金都园1幢四层(市政府左边30米处，原龙公馆)</span></a><hr><a style=\"text-decoration:none; display:block; width:100%; text-align:center;\" href=\"##\"><span>订房热线：<br>0571-6472<span style=\"color:#F36\">1314</span> &nbsp;&nbsp;一生一世<br>0571-6475<span style=\"color:#F36\">9520</span> &nbsp;&nbsp;有我爱你</span></a>";
		// System.out.println(removeHtmlTags(source));

		// Pattern pt = Pattern.compile("\\d{4,12}[-\\d{6,9}]?");
		// Matcher matcher = pt.matcher("n>预约电话：64701779</span></a>");
		//		
		// while(matcher.find()){
		// String s = matcher.group();
		// //if(s.length()>8){
		// System.out.println("phone>>"+s);
		// //}
		// }

		// createNFile2("","b.html");
		// pageTurn();
		// createHTMLFile("http://localhost:8080/JDFC/amdl.html");

		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建群信息中心_rent&page=1",
				"rent_建群信息中心_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=万福房产中介麻园分部_rent&page=1",
				"rent_万福房产中介麻园分部_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=万福房产中介麻园分部_rent&page=2",
				"rent_万福房产中介麻园分部_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=广源房产_rent&page=1",
				"rent_广源房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_rent&page=1",
				"rent_建德市安东房产经纪公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=星光房产中介有限公司_rent&page=1",
				"rent_星光房产中介有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市恒信房产经纪有限公司美丽家园信息部_rent&page=1",
				"rent_建德市恒信房产经纪有限公司美丽家园信息部_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_rent&page=1",
				"rent_安居房产环北分公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_rent&page=1",
				"rent_建德市联谊房地产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_rent&page=2",
				"rent_建德市联谊房地产经纪有限公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=涵美房地产经纪有限公司_rent&page=1",
				"rent_涵美房地产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_rent&page=1",
				"rent_易家房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_rent&page=2",
				"rent_易家房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_rent&page=3",
				"rent_易家房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市方方房产中介_rent&page=1",
				"rent_建德市方方房产中介_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市方方房产中介_rent&page=2",
				"rent_建德市方方房产中介_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=锦江房产中介_rent&page=1",
				"rent_锦江房产中介_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=锦江房产中介_rent&page=2",
				"rent_锦江房产中介_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=1",
				"rent_鑫成房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=2",
				"rent_鑫成房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=3",
				"rent_鑫成房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=4",
				"rent_鑫成房产_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_rent&page=1",
				"rent_恒丰房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_rent&page=2",
				"rent_恒丰房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪有限公司_rent&page=1",
				"rent_建德市安东房产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_rent&page=1",
				"rent_建德市万新大众房产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_rent&page=2",
				"rent_建德市万新大众房产经纪有限公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_rent&page=3",
				"rent_建德市万新大众房产经纪有限公司_3.html");

		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_sell&page=1",
				"sell_建德市安东房产经纪公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_sell&page=2",
				"sell_建德市安东房产经纪公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=1",
				"sell_易家房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=2",
				"sell_易家房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=3",
				"sell_易家房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=4",
				"sell_易家房产_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_sell&page=1",
				"sell_鑫成房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_sell&page=2",
				"sell_鑫成房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_sell&page=3",
				"sell_鑫成房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_sell&page=1",
				"sell_安居房产环北分公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_sell&page=2",
				"sell_安居房产环北分公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_sell&page=3",
				"sell_安居房产环北分公司_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_sell&page=1",
				"sell_建德市联谊房地产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_sell&page=2",
				"sell_建德市联谊房地产经纪有限公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_sell&page=3",
				"sell_建德市联谊房地产经纪有限公司_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_sell&page=4",
				"sell_建德市联谊房地产经纪有限公司_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=1",
				"sell_恒丰房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=2",
				"sell_恒丰房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=3",
				"sell_恒丰房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=4",
				"sell_恒丰房产_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=5",
				"sell_恒丰房产_5.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_sell&page=1",
				"sell_建德市万新大众房产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_sell&page=2",
				"sell_建德市万新大众房产经纪有限公司_2.html");

		System.out.println("OK");
		int k = 0;
		if (k == 0) {
			return;
		}

		// createFile("http://localhost:8080/JDFC/fwcz.jsp?page=1","rent_1.html");
		// createFile("http://localhost:8080/JDFC/fwcz.jsp?page=2","rent_2.html");
		// createFile("http://localhost:8080/JDFC/fwcz.jsp?page=3","rent_3.html");
		// createFile("http://localhost:8080/JDFC/fwcz.jsp?page=4","rent_4.html");
		//		
		// createFile("http://localhost:8080/JDFC/fccs.jsp?page=1","sell_1.html");
		// createFile("http://localhost:8080/JDFC/fccs.jsp?page=2","sell_2.html");
		// createFile("http://localhost:8080/JDFC/fccs.jsp?page=3","sell_3.html");
		// createFile("http://localhost:8080/JDFC/fccs.jsp?page=4","sell_4.html");

		// createNFile("http://localhost:8080/JDFC/fccs.jsp?type=万福房产中介麻园分部_rent&page=1","rent_万福房产中介麻园分部_1.html");
		// int j = 0;
		// if (j == 0) {
		//
		// return;
		// }

		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建群信息中心_rent&page=1",
				"rent_建群信息中心_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=广源房产_rent&page=1",
				"rent_广源房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=万福房产中介麻园分部_rent&page=1",
				"rent_万福房产中介麻园分部_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=万福房产中介麻园分部_rent&page=2",
				"rent_万福房产中介麻园分部_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_rent&page=1",
				"rent_建德市安东房产经纪公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒信房产_rent&page=1",
				"rent_恒信房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市求新房产中介有限公司_rent&page=1",
				"rent_建德市求新房产中介有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市恒信房产经纪有限公司美丽家园信息部_rent&page=1",
				"rent_建德市恒信房产经纪有限公司美丽家园信息部_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_rent&page=1",
				"rent_安居房产环北分公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_rent&page=1",
				"rent_建德市联谊房地产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_rent&page=2",
				"rent_建德市联谊房地产经纪有限公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=天成房产_rent&page=1",
				"rent_天成房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=天成房产_rent&page=2",
				"rent_天成房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市金桥中介有限公司_rent&page=1",
				"rent_建德市金桥中介有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市方方房产信息服务部_rent&page=1",
				"rent_建德市方方房产信息服务部_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市方方房产信息服务部_rent&page=2",
				"rent_建德市方方房产信息服务部_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_rent&page=1",
				"rent_易家房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_rent&page=2",
				"rent_易家房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_rent&page=3",
				"rent_易家房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=1",
				"rent_鑫成房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=2",
				"rent_鑫成房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=3",
				"rent_鑫成房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_rent&page=4",
				"rent_鑫成房产_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=锦江房产中介_rent&page=1",
				"rent_锦江房产中介_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=锦江房产中介_rent&page=2",
				"rent_锦江房产中介_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_rent&page=1",
				"rent_恒丰房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=百合信息服务中心_rent&page=1",
				"rent_百合信息服务中心_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_rent&page=1",
				"rent_建德市万新大众房产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_rent&page=2",
				"rent_建德市万新大众房产经纪有限公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市万新大众房产经纪有限公司_rent&page=3",
				"rent_建德市万新大众房产经纪有限公司_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德大家房地产经纪有限公司_rent&page=1",
				"rent_建德大家房地产经纪有限公司_1.html");

		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=广源房产_sell&page=1",
				"sell_广源房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=星光房产中介有限公司_sell&page=1",
				"sell_星光房产中介有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=星光房产中介有限公司_sell&page=2",
				"sell_星光房产中介有限公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=星光房产中介有限公司_sell&page=3",
				"sell_星光房产中介有限公司_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=星光房产中介有限公司_sell&page=4",
				"sell_星光房产中介有限公司_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_sell&page=1",
				"sell_建德市安东房产经纪公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_sell&page=2",
				"sell_建德市安东房产经纪公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市安东房产经纪公司_sell&page=3",
				"sell_建德市安东房产经纪公司_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=1",
				"sell_易家房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=2",
				"sell_易家房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=3",
				"sell_易家房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=易家房产_sell&page=4",
				"sell_易家房产_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_sell&page=1",
				"sell_鑫成房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=鑫成房产_sell&page=2",
				"sell_鑫成房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=锦江房产中介_sell&page=1",
				"sell_锦江房产中介_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_sell&page=1",
				"sell_安居房产环北分公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_sell&page=2",
				"sell_安居房产环北分公司_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=安居房产环北分公司_sell&page=3",
				"sell_安居房产环北分公司_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市联谊房地产经纪有限公司_sell&page=1",
				"sell_建德市联谊房地产经纪有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=1",
				"sell_恒丰房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=2",
				"sell_恒丰房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=3",
				"sell_恒丰房产_3.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=恒丰房产_sell&page=4",
				"sell_恒丰房产_4.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德市金桥中介有限公司_sell&page=1",
				"sell_建德市金桥中介有限公司_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=天成房产_sell&page=1",
				"sell_天成房产_1.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=天成房产_sell&page=2",
				"sell_天成房产_2.html");
		createNFile(
				"http://localhost:8080/JDFC/fccs.jsp?type=建德大家房地产经纪有限公司_sell&page=1",
				"sell_建德大家房地产经纪有限公司_1.html");

		int i = 0;
		if (i == 0) {

			return;
		}
		HomeInfo address = new HomeInfo();
		address.setConstruct("dsfhfds转换为对象j");
		address.setDecorate("sddas");
		address.setNote("hghgbhjgh沟通vgthvh1");
		address.setFloor("dsfd");

		HomeInfo address1 = new HomeInfo();
		address1.setConstruct("123123转换为对象j");
		address1.setDecorate("123123");
		address1.setNote("123123沟通vgthvh1");
		address1.setFloor("12332123");

		JAXBContext context = JAXBContext.newInstance(Homes.class);
		// 下面代码演示将对象转变为xml
		Marshaller m = context.createMarshaller();
		FileWriter fw = new FileWriter("person.xml");
		HomeInfo[] ms = new HomeInfo[2];
		ms[0] = address;
		ms[1] = address1;
		m.marshal(new Homes(ms), fw);

		// 下面代码演示将上面生成的xml转换为对象
		FileReader fr = new FileReader("person.xml");
		Unmarshaller um = context.createUnmarshaller();
		Homes p2 = (Homes) um.unmarshal(fr);
		System.out.println("getConstruct:" + p2.hm[0].construct);
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