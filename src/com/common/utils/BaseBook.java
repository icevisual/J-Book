package com.common.utils;

import java.sql.Timestamp;
import java.util.Map;

public abstract class BaseBook extends BaseCatch {

	public BaseBook(String startUrl) {
		super(startUrl);
		// TODO Auto-generated constructor stub
	}

	protected Map<String, String> cachedLastBook = null;
	
	protected int maxSearchPageNumber = 0;

	protected int maxSearchGroupNumber = 0;

	protected String earliestDate = "";
	protected String[] lastBookName = null;

	public String getEarliestDate() {
		return earliestDate;
	}

	public void setEarliestDate(String earliestDate) {
		this.earliestDate = earliestDate;
	}

	public String[] getLastBookName() {
		return lastBookName;
	}

	public void setLastBookName(String[] lastBookName) {
		this.lastBookName = lastBookName;
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
	 * @param url
	 * @return
	 */
	public String getCachedLastBook(String url) {
		Map<String, String>  map = this.getCachedLastBook();
		return map.get(url);
	}
	
	/**
	 * Set the cache information of given url
	 * @param url
	 * @param bookName
	 * @return
	 */
	public String setCachedLastBook(String url,String bookName) {
		Map<String, String>  map = this.getCachedLastBook();
		return map.put(url,bookName);
	}
	
	public Map<String, String> getCachedLastBook() {
		return cachedLastBook;
	}

	public void setCachedLastBook(Map<String, String> cachedLastBook) {
		this.cachedLastBook = cachedLastBook;
	}
	

	/**
	 * Check the book name
	 * 
	 * @param str
	 * @return
	 */
	public Boolean checkLastBookName(String str) {
		// TODO : different type but has the same name
		String[] collection = this.getLastBookName();

		if (collection.length == 0) {
			return false;
		}
		for (String co : collection) {
			if (str.indexOf(co) != -1) {
				return true;
			}
		}
		return false;
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
