package com.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseBook extends BaseCatch {

	public BaseBook(String startUrl) {
		super(startUrl);

		Map<String, String> map = new HashMap<String, String>();

		this.setCachedLastBook(map);
		this.loadDownloadListFile();

	}

	protected Map<String, String> cachedLastBook = null;

	protected int maxSearchPageNumber = 0;

	protected int maxSearchGroupNumber = 0;

	protected String earliestDate = "";

	public String getEarliestDate() {
		return earliestDate;
	}

	public void setEarliestDate(String earliestDate) {
		this.earliestDate = earliestDate;
	}


	public int getMaxSearchGroupNumber() {
		return maxSearchGroupNumber;
	}

	public void setMaxSearchGroupNumber(int maxSearchGroupNumber) {
		this.maxSearchGroupNumber = maxSearchGroupNumber;
	}

	public int getMaxSearchPageNumber() {
		return maxSearchPageNumber;
	}

	public void setMaxSearchPageNumber(int maxSearchPageNumber) {
		this.maxSearchPageNumber = maxSearchPageNumber;
	}

	/**
	 * Get the cached value of specific url
	 * 
	 * @param url
	 * @return
	 */
	public String getCachedLastBook(String url) {
		Map<String, String> map = this.getCachedLastBook();
		return map.get(url);
	}

	/**
	 * Set the cache information of given url
	 * 
	 * @param url
	 * @param bookName
	 * @return
	 */
	public String setCachedLastBook(String url, String bookName) {
		Map<String, String> map = this.getCachedLastBook();
		return map.put(url, bookName);
	}

	public Map<String, String> getCachedLastBook() {
		return cachedLastBook;
	}

	public void setCachedLastBook(Map<String, String> cachedLastBook) {
		this.cachedLastBook = cachedLastBook;
	}
	
	/**
	 * Merge the list files that contain the download file list
	 * @param storePath
	 */
	public void mergeDaownloadList(String storePath) {
		Map<String, String> map = this.getCachedLastBook();
		Iterator<String> it = map.keySet().iterator();
		String key = "", value = "";
		logger.info("Merge Download List ");

		File mergeFile = new File(storePath);
		FileWriter fWriter;
		try {
			fWriter = new FileWriter(mergeFile, false);
			while (it.hasNext()) {
				key = it.next();
				value = map.get(key);
				fWriter.write(key + "\n");
			}
			fWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Merge Download List Done");
	}

	/**
	 * Merge the list files that contain the download file list
	 */
	public void mergeDaownloadList() {
		this.mergeDaownloadList(".\\load\\download\\list.merge");
	}

	public void printCache() {
		Map<String, String> map = this.getCachedLastBook();
		Iterator<String> it = map.keySet().iterator();
		String key = "", value = "";
		logger.info("Print Cache Start");
		while (it.hasNext()) {
			key = it.next();
			value = map.get(key);
			System.out.println("Key = " + key + "\t Value = " + value);
		}
		logger.info("Print Cache Done");
	}

	/**
	 * loadDownloadListFile
	 * @param dirname
	 */
	public void loadDownloadListFile(String dirname) {
		File dir = new File(dirname);

		String[] list = dir.list();

		String line = "";

		for (String filename : list) {
			try {
				BufferedReader bReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File(dirname + filename))));

				while ((line = bReader.readLine()) != null) {
					this.setCachedLastBook(line, "");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void loadDownloadListFile() {
		String dirname = ".\\load\\download\\";
		this.loadDownloadListFile(dirname);
	}

	/**
	 * is the given url in the donwload list
	 * 
	 * @param str
	 * @return
	 */
	public Boolean isDownloaded(String str) {
		Map<String, String> map = this.getCachedLastBook();
		return map.containsKey(str);
	}

	/**
	 * less than return true,otherwise false
	 * 
	 * @param str
	 * @return
	 */
	public Boolean checkEarliestDay(String str) {
		String earliestDay = this.getEarliestDate();
		if ("".equals(earliestDay) || null == earliestDay) {
			return false;
		}
		Long earliestDayTime = this.dataStr2timestamp(earliestDay);
		Long currentDayTime = this.dataStr2timestamp(str);

		return currentDayTime < earliestDayTime;
	}

	public Long dataStr2timestamp(String str) {
		Timestamp t = Timestamp.valueOf(str + " 00:00:00");
		return t.getTime();
	}

	/**
	 * check the searched page numbers
	 * 
	 * @param currentNumber
	 * @return
	 */
	public boolean checkMaxSearchPageNumber(int currentNumber) {
		int max = this.getMaxSearchPageNumber();
		if (max == 0) {
			return false;
		}
		return currentNumber >= max;
	}

}
