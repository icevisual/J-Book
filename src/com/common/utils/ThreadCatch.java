package com.common.utils;

public class ThreadCatch extends Thread {

	public String name;
	public String proxy;
	public int start;
	public int end;

	public ThreadCatch(String name,String proxy, int start, int end) {
		this.name = name;
		this.proxy = proxy;
		this.start = start;
		this.end = end;
	}

	public void run() {
		try {
			Main.Loop(this.start,this.end,this.name,this.proxy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
