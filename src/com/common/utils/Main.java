package com.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {

	/**
	 * Get Time Based Random Name
	 * 
	 * @return
	 */
	public static String getTBRName() {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		return time.format(nowTime)
				+ UUID.randomUUID().toString().substring(0, 5);
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
	public static Document connect(String pageUrl, int time)
			throws IOException, InterruptedException {
		Document doc = null;
		try {
			// doc = Jsoup.connect(pageUrl).get();
			doc = Jsoup
					.connect(pageUrl)
					.header(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.65 Safari/537.36")
					.get();
		} catch (SocketTimeoutException e) {
			int t = (int) (100 * (Math.pow(time, 1.0 / 3) - 1) / Math.pow(time,
					1.0 / 4));
			System.out.println("TIME OUT PGURL:" + pageUrl);
			System.out.println("TIME OUT COUNT:" + time + " SELLP:" + (t / 10)
					+ " S");
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

	public static void downloadURLContent(String url, OutputStream os) {
		System.out.println("Starting Send " + url);
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			connection.connect();
			BufferedInputStream in = new BufferedInputStream(connection
					.getInputStream());
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

	public static void main(String[] args) throws Exception {
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
				BufferedReader is = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
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
}
