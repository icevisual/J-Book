package com.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class Pingyin {

	
	public static void FileOpreator(String filename) throws IOException{
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		int i = 0 ;
		while( (line = br.readLine()) != null){
			
			System.out.println(new String(line.getBytes(""),"gb2312"));
			
		}
	
	}
	
	
	public static void main(String[] args) throws Exception{
		FileOpreator("D:\\Temp\\Projects\\book\\1.txt");
	}
}
