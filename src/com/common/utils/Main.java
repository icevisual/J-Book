package com.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	/**
	 * Get Time Based Random Name
	 * 
	 * @return
	 */
	public static String getTBRName() {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		return time.format(nowTime) + UUID.randomUUID().toString().substring(0, 5);
	}

	public static String[] loadProxy() throws Exception{
		String baseDir = System.getProperty("user.dir");
		String filename = baseDir + "\\proxy.data";
		StringBuffer sBuffer = new StringBuffer();
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		String[] coll = new String[100];
		int i = 0 ;
		while ((line = br.readLine()) != null) {
			coll[i ++] = line;
		}
		br.close();
		return coll;
	}
	
	
	/**
	 * JSOUP 远程链接
	 * 
	 * @param pageUrl
	 * @param time
	 *            首次延时时间（秒）
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Document connect(String pageUrl, int time) throws IOException, InterruptedException {
		Document doc = null;
		try {
//			 doc = Jsoup.connect(pageUrl).
			doc = Jsoup.connect(pageUrl)
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.65 Safari/537.36")
					.get();
		} catch (SocketTimeoutException e) {
			int t = (int) (100 * (Math.pow(time, 1.0 / 3) - 1) / Math.pow(time, 1.0 / 4));
			System.out.println("TIME OUT PGURL:" + pageUrl);
			System.out.println("TIME OUT COUNT:" + time + " SELLP:" + (t / 10) + " S");
			if (time < 20) {
				// 10*(x^(1/3)-1)/x^(1/4)
				Thread.sleep(t * 100);
				return connect(pageUrl, time + 1);
			} else {
				System.out.println("SocketTimeoutException:" + pageUrl);
				throw new SocketTimeoutException();
			}
		}
		return doc;
	}
	
	
	public static Map<String, String> loadCookies() throws Exception{
		String baseDir = System.getProperty("user.dir");
		String filename = baseDir + "\\cookies";
		StringBuffer sBuffer = new StringBuffer();
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		int i = 0 ;
		Map<String, String> cookies = new LinkedHashMap<String,String>();
		while ((line = br.readLine()) != null) {
			String [] sp = line.split("\\s+");
			cookies.put(sp[0], sp[1]);
		}
		br.close();
		return cookies;
	}
	
	
	/**
	 * JSOUP 远程链接
	 * 
	 * @param pageUrl
	 * @param time
	 *            首次延时时间（秒）
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Document connectWithProxy(String pageUrl, int time,String proxy) throws Exception {
		Document doc = null;
		try {
			if("".equals(proxy)){
				return connect(pageUrl, time);
			}
			
			Map<String, String> cookies = Main.loadCookies();
			
			String[] proxySet = proxy.split("\\:");
			doc = Jsoup.connect(pageUrl).cookies(cookies)
					.proxy(proxySet[0], Integer.parseInt(proxySet[1]))
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.65 Safari/537.36")
					.get();
		} catch (SocketTimeoutException e) {
			int t = (int) (100 * (Math.pow(time, 1.0 / 3) - 1) / Math.pow(time, 1.0 / 4));
			System.out.println("TIME OUT PGURL:" + pageUrl);
			System.out.println("TIME OUT COUNT:" + time + " SELLP:" + (t / 10) + " S");
			if (time < 20) {
				// 10*(x^(1/3)-1)/x^(1/4)
				Thread.sleep(t * 100);
				return connectWithProxy(pageUrl, time + 1,proxy);
			} else {
				System.out.println("SocketTimeoutException:" + pageUrl);
				throw new SocketTimeoutException();
			}
		}
		return doc;
	}

	public static void downloadURLContent(String url, OutputStream os) {
		System.out.println("Starting Send " + url);
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			connection.connect();
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			BufferedOutputStream writer = new BufferedOutputStream(os);
			byte[] buf = new byte[1024 * 3];
			int length = in.read(buf);
			while (length != -1) {
				writer.write(buf, 0, length);
				length = in.read(buf);
			}
			writer.close();
			in.close();
			connection.disconnect();
			System.out.println("Send Over");
		} catch (Exception e) {
			e.printStackTrace();
			// downloadURLContent(url, filePath);
		}
	}

	public static void socketTest() throws IOException {

		ServerSocket server = null;
		try {
			try {
				server = new ServerSocket(4700);
				// 创建一个ServerSocket在端口4700监听客户请求
			} catch (Exception e) {
				System.out.println("can not listen to:" + e);
				// 出错，打印出错信息
			}
			Socket socket = null;
			int i = 0;
			while (true) {
				try {
					socket = server.accept();
					// 使用accept()阻塞等待客户请求，有客户
					// 请求到来则产生一个Socket对象，并继续执行
				} catch (Exception e) {
					System.out.println("Error." + e);
					// 出错，打印出错信息
				}
				String line;
				BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// 由Socket对象得到输入流，并构造相应的BufferedReader对象

				// 由Socket对象得到输出流，并构造PrintWriter对象
				// BufferedReader sin = new BufferedReader(new
				// InputStreamReader(
				// System.in));
				// 由系统标准输入设备构造BufferedReader对象
				String url = is.readLine();// "http://www.xuanshu.com/soft/sort01/";//is.readLine();
				System.out.println("Client:" + url);
				// 在标准输出上打印从客户端读入的字符串
				// line = sin.readLine();
				// 从标准输入读入一字符串
				// while (!line.equals("bye")) {X
				// 如果该字符串为 "bye"，则停止循环
				if (url.endsWith(".jpg") || url.endsWith(".png")) {
					downloadURLContent(url, socket.getOutputStream());
				} else {
					PrintWriter os = new PrintWriter(socket.getOutputStream());
					Document doc = Main.connect(url, 1);
					String data = doc.html();
					os.println(data);
					// 向客户端输出该字符串
					os.flush();
					os.close(); // 关闭Socket输出流
				}
				// 刷新输出流，使Client马上收到该字符串
				// System.out.println("Server:" + line);
				// 在系统标准输出上打印读入的字符串
				// System.out.println("Client:" + is.readLine());
				// 从Client读入一字符串，并打印到标准输出上
				// line = sin.readLine();
				// 从系统标准输入读入一字符串
				// } // 继续循环X
				is.close(); // 关闭Socket输入流
				socket.close(); // 关闭Socket
				if (i == 1) {
					break;
				}
			}
			server.close(); // 关闭ServerSocket
		} catch (Exception e) {

			System.out.println("Error:" + e);
			// 出错，打印出错信息
			server.close(); // 关闭ServerSocket
		}
	}

	public static String file_get_content(String filename) throws Exception {
		StringBuffer sBuffer = new StringBuffer();
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null) {
			sBuffer.append(line);
		}
		br.close();
		return sBuffer.toString();
	}

	public static void downloadDemo(int uid) throws Exception {
		System.out.println(System.getProperty("user.dir"));
		String url = "http://bbs.ubnt.com.cn/home.php?mod=space&uid=" + uid + "&do=profile&from=space";
		Document root = Main.connect(url, 1);
		File file = new File(System.getProperty("user.dir") + "\\html-" + uid);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(root.html());
		fileWriter.close();
	}

	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}

	public static Object xpath(Element doc, String xpath) throws Exception {
		String[] segments = xpath.split("\\s+");
		Element loop = doc;
		for (String str : segments) {
			String selectStr = str;
			String number = "";
			if (-1 != str.indexOf(":")) {
				selectStr = str.substring(0, str.indexOf(":"));
				number = str.substring(str.indexOf(":") + 1);
			}
			if (str.startsWith("#")) {
				loop = loop.getElementById(selectStr.substring(1));
			} else if (str.startsWith(".")) {
				if ("".equals(number)) {
					return loop.getElementsByClass(selectStr.substring(1));
				}
				loop = loop.getElementsByClass(selectStr.substring(1)).get(Integer.parseInt(number));
			} else {
				if ("".equals(number)) {
					return loop.getElementsByTag(selectStr);
				}
				loop = loop.getElementsByTag(selectStr).get(Integer.parseInt(number));
			}
		}
		return loop;
	}

	public static void printMap(Map<String, String> map) {
		Iterator<String> it = map.keySet().iterator();
		String key = "", value = "";
		while (it.hasNext()) {
			key = it.next();
			value = map.get(key);
			System.out.println("Key = " + key + "\t Value = " + value);
		}
	}

	public static Map<String, String> anylze(Document doc) throws Exception {
		String name = ((Element) Main.xpath(doc, "#uhd .h:0 h2:0")).html();
		// String logo = ((Element) Main.xpath(doc, "#uhd img:0")).attr("src");
		// String home = ((Element) Main.xpath(doc, "#uhd .h:0 p:0
		// a:0")).attr("href");
		// ((Elements) Main.xpath(doc, "#ct .bw0:0 .bm_c:0 .u_profile:0 .bbda:0
		// li"));
		Elements collect = ((Elements) Main.xpath(doc, "#ct .bw0:0 .bm_c:0 .u_profile:0 li"));

		Map<String, String> infoMap = new LinkedHashMap<String, String>();
		infoMap.put("昵称", name);
		// infoMap.put("logo", logo);
		// infoMap.put("home", home);

		for (int i = 0; i < collect.size(); i++) {
			Elements ems = collect.get(i).getElementsByTag("em");
			for (Element element : ems) {
				String nn = element.html();
				String ht = collect.get(i).html();
				String value = Main.delHTMLTag(ht.substring(ht.indexOf("</em>")));
				nn = nn.replace("&nbsp;", "");
				// Elements as = collect.get(i).getElementsByTag("a");
				// if (as.size() > 0) {
				// for (Element tagA : as) {
				// value += "[" + tagA.attr("href")+"]";
				// }
				// }
				if ("统计信息".equals(nn)) {
					String[] gp = value.split("\\|");
					for (String sdsd : gp) {
						String[] saddds = sdsd.trim().split("\\s+");
						infoMap.put(saddds[0], saddds[1]);
					}
				} else {
					infoMap.put(nn, value);
				}
			}
		}
		return infoMap;
	}

	public static String toSql(Map<String, String> map,Set<String> colSet ) throws Exception {
		Iterator<String> it = map.keySet().iterator();
		String key = "", value = "";
		StringBuffer sqlCols = new StringBuffer();
		StringBuffer sqlVals = new StringBuffer();
		sqlCols.append("INSERT INTO `chinese_col` (");
		sqlVals.append("VALUES(");
		while (it.hasNext()) {
			key = it.next();
			value = map.get(key);
			sqlCols.append("`" + key + "`");
			sqlVals.append("'" + value + "'");
			if (it.hasNext()) {
				sqlCols.append(",");
				sqlVals.append(",");
			}
			
			if(!colSet.contains(key)){
				colSet.add(key);
			}
		}
		sqlCols.append(")");
		sqlVals.append(");");
		return sqlCols.append(sqlVals).toString();
	}
	
	public static void printColSet(Set<String> col) {
		Iterator<String> it = col.iterator();
		String key = "", value = "";
		while (it.hasNext()) {
			key = it.next();
			System.out.println("  `"+key+"` varchar(255) NULL DEFAULT NULL COMMENT '"+key+"',");
		}
	}
	

	public static void Loop(int startIndex,int endIndex ,String tag,String proxy) throws Exception {
//		int startIndex = 1, endIndex = 501,
		int uid = 0;
		String baseDir = System.getProperty("user.dir");
		Map<String, String> infoMap = null; // Information Map
//		endIndex = 3;
		
		File file = new File(baseDir + "\\" + startIndex + "-" + endIndex + ".sql");
		FileWriter fileWriter = new FileWriter(file); // Insert Sql File
//		String[] proxy = Main.loadProxy();// Proxy Array
		
		Set<String> colSet = new LinkedHashSet<String>();// Columns Set
//		endIndex = startIndex + 4;
		for (int i = startIndex; i < endIndex; i++) {
			uid = i;
			String url = "http://bbs.ubnt.com.cn/home.php?mod=space&uid=" + uid + "&do=profile&from=space";
			Document root = Main.connectWithProxy(url, 1,proxy);
			try {
				System.out.println("[ "+tag+" ][ catch uid = " + uid + "]");
				infoMap = Main.anylze(root);
				infoMap.put("uid", uid + "");
				Main.printMap(infoMap);
				String sql = Main.toSql(infoMap,colSet);
				fileWriter.write(sql + "\n");
				Thread.sleep((int) (Math.random() * 500 + 500));
			} catch (Exception e) {
				System.out.println("[ "+tag+" ][ uid = " + uid + " Not Exists ]");
			}
		}
		fileWriter.close();
//		Main.printColSet(colSet);
	}
	
	
	public static int randomInt(int max){
		return (int)(max * Math.random());
	}

	public static void main(String[] args) throws Exception {
//		Main.Loop(501,1001);

//		String [] proxys = {"","106.75.128.90:80","106.75.128.89:80","119.6.136.122:80"};
//		int start = 2001;
//		for(int i = 0 ; i < 1 ; i ++){
//			ThreadCatch t1 = new ThreadCatch("C" + i,proxys[i], start + i * 500,start + (i + 1) * 500);
//			t1.start();
//		}
		
		Document doc= Main.connectWithProxy("http://bbs.ubnt.com.cn/home.php?mod=space&uid=222&do=profile&from=space", 1,"");
		System.out.println(doc.html());
//		
		
//		Main.printMap(Main.loadCookies());
		
		
//		for(int i = 0 ; i < 4 ; i ++){
//			ThreadCatch t1 = new ThreadCatch("C" + i,proxys[i], start + i * 500,start + (i + 1) * 500);
//			t1.start();
//		}
// 		120.25.105.45:81
//		106.75.128.90:80
//		106.75.128.89:80
//		139.196.108.68:80
//		String[] proxy = Main.loadProxy();
//		System.out.println(proxy.length);
//		System.out.println(proxy[99].split("\\:")[0] + " " + proxy[99].split("\\:")[1]);
//		
//		Document doc= Main.connectWithProxy("http://bbs.ubnt.com.cn/home.php?mod=space&uid=501&do=profile&from=space", 1,"");
//		System.out.println(doc.html());
//		Main.connect("http://test.open.qiweiwangguo.com/", 1);
	}
}
