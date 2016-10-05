package com.xsjiande.fc;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

public class HomeRentSell {
	private final String SELLURL = "http://www.jdfcw.com/oldhouse/";
	private final String RENTURL = "http://www.jdfcw.com/rent/";
	
	private String basePath;
	
	private String fileSystemPathString = "D:/Program Files/MyEcpliseWorkplace/workplace140320/JDFC/WebRoot" ; 
	
	private List<HomeInfo> homeRent;
	private List<HomeInfo> homeSell;
	
	public HomeRentSell(String basePath){
		this.basePath = basePath;
		homeRent = new ArrayList<HomeInfo>();
		homeSell = new ArrayList<HomeInfo>();
	}
	
	public List<HomeInfo> getHomeRent() {
		return homeRent;
	}
	public void setHomeRent(List<HomeInfo> homeRent) {
		this.homeRent = homeRent;
	}
	public List<HomeInfo> getHomeSell() {
		return homeSell;
	}
	public void setHomeSell(List<HomeInfo> homeSell) {
		this.homeSell = homeSell;
	}
	
	public void initHome(String uri){
		List<String> url = new ArrayList<String>();
		
		int type=0;
		if(uri.indexOf("oldhouse")>0)
			type = 1;
		else if(uri.indexOf("rent")>0)
			type = 2;
		
		String html = new String(HttpUtil.getURLContent(uri, "gb2312"));
		//System.out.println(html);
		String regex = "saleshow.asp\\?id=\\d{6}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		
		while(matcher.find()){
			String string = matcher.group();
			if(!url.contains(string)){
				url.add(string);
				//System.out.println(string);
			}	
		}

		for(int i=0; i<url.size(); i++){
			HomeInfo info = new HomeInfo();
			int n=0;
			if(type == 1){
				html = new String(HttpUtil.getURLContent(SELLURL + url.get(i), "gb2312"));
				regex = "23.+td";
			}
			else if(type == 2){
				html = new String(HttpUtil.getURLContent(RENTURL + url.get(i), "gb2312"));
				regex = "34.+td";
			}

			n = 0;
			if(type == 1)
				regex = "45.+td";
			else if(type == 2)
				regex = "31.+td";

			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(html);
			int poc = 0;//中介或者个人人
			while(matcher.find()){
				n++;
				String s = matcher.group();
				s = s.replaceAll(" ", "");
				if(n==1){
					String pocType = StringUtils.substringBetween(s, "p;", "</td");
					if( !pocType.equals("个人")){
						poc = 0;
						info.setPoc("中介");
					}else{
						info.setPoc("个人");
					}
				}
				
				System.out.println("right="+s);
				if(n==4 && type==2){
					String str = StringUtils.substringBetween(s, "0\">", "</");
					info.setSquare(str==null?"":str);
					str = StringUtils.substringBetween(s, "</font>", "</td");
					info.setSquare(info.square+(str==null?"":str));
				}else if(n==5 && type==1){
					String str = StringUtils.substringBetween(s, "p;", "</");
					info.setSquare(str==null?"":str);
					str = StringUtils.substringBetween(s, "</font>", "</td");
					info.setSquare(info.square+(str==null?"":str));
				}else if(n==5 && type==2){
					String str = StringUtils.substringBetween(s, "0\">", "</");
					info.setPrice(str);
					str = StringUtils.substringBetween(s, "</font>", "</td");
					info.setPrice(info.price+str);
				}else if(n==6 && type==1){
					String str = StringUtils.substringBetween(s, "0\">", "</");
					info.setPrice(str);
					str = StringUtils.substringBetween(s, "</font>", "</td");
					info.setPrice(info.price+str);
				}else if(n==6 && type==2){
					String str = StringUtils.substringBetween(s, "p;", "</");
					info.setDecorate(str);
				}else if(n==9 && poc == 0 && type == 1 ){
					
					String str = StringUtils.substringBetween(s, "00>", "<");
					info.setCompanyAddr(str);
				}else if(n==10 && poc == 0 && type == 2 ){
					String str = StringUtils.substringBetween(s, "00>", "<");
					info.setCompanyAddr(str);
					
				}else if(n==11 && poc == 0 && type == 1 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					if(info.phone==null||info.phone.trim().equals("")){
						info.setPhone(str);
					}else {
						info.setPhone(info.phone+","+str);
					}
				}else if(n==10 && poc == 1 && type == 1 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					if(info.phone==null||info.phone.trim().equals("")){
						info.setPhone(str);
					}else {
						info.setPhone(info.phone+","+str);
					}
				}else if(n==12 && poc == 0 && type == 2 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					if(info.phone==null||info.phone.trim().equals("")){
						info.setPhone(str);
					}else {
						info.setPhone(info.phone+","+str);
					}
				}else if(n==11 && poc == 1 && type == 2 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					if(info.phone==null||info.phone.trim().equals("")){
						info.setPhone(str);
					}else {
						info.setPhone(info.phone+","+str);
					}
				}else if(type==2&& n== 8){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPayCondi(str);
				}
			}
			n = 0;
			if(type == 1)
				regex = "23.+td";
			else if(type == 2)
				regex = "34%.+td";
			
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(html);
			
			//System.out.println(html);
			while(matcher.find()){
				n++;
				String s = matcher.group();
				s = s.replaceAll(" ", "");

				System.out.println("left="+s);
				if(n==2){
					String str = StringUtils.substringBetween(s, "p;", "</");
					info.setLocal(str);
				}else if(n==3){
					String str = StringUtils.substringBetween(s, "p;", "</");
					info.setPlace(str);
				}else if(n==4){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setConstruct(str);
				}else if(n==5){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setFloor(str);
				}else if(n==7 && type==1){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setDecorate(str);
					
				}else if(n==9 && poc == 0 && type == 1 ){
					String str = StringUtils.substringBetween(s, "00>", "<");
					info.setCompany(str);
				}else if(n==10 && poc == 0 && type == 2 ){
					String str = StringUtils.substringBetween(s, "00>", "<");
					info.setCompany(str);
					
				}else if(n==10 && poc == 0 && type == 1 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPerson(str);
				}else if(n==9 && poc == 1 && type == 1 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPerson(str);
				}else if(n==11 && poc == 0 && type == 2 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPerson(str);
				}else if(n==10 && poc == 1 && type == 2 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPerson(str);
				}else if(n==11 && poc == 0 && type == 1 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPhone(str);
				}else if(n==10 && poc == 1 && type == 1 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPhone(str);
				}else if(n==12 && poc == 0 && type == 2 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPhone(str);
				}else if(n==11 && poc == 1 && type == 2 ){
					String str = StringUtils.substringBetween(s, "p;", "<");
					info.setPhone(str);
				}
			}
			
			
			
			
			
			n=0;
			regex = "<td>.+</td>";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(html);
			while(matcher.find()){
				n++;
				if(n==3){
					String s = matcher.group();
					s = s.replaceAll(" ", "");
					info.setNote(StringUtils.substringBetween(s, "<td>", "</td>"));
					break;
				}	
			}
			
			n=0;
			regex = "colspan=\"3\">.+</td>";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(html);
			while(matcher.find()){
				n++;
				if((n==3 && type==1) || (n==2 && type==2)){
					String s = matcher.group();
					//s = s.replaceAll(" ", "");
					String str = StringUtils.substringBetween(s, "p;", " ");
					info.setTime(str);
					break;
				}
			}
			
			info.setUrl(url.get(i));
			
			info.print();
			if(type == 1)
				homeSell.add(info);
			else if(type == 2)
				homeRent.add(info);
		}
		
		//String[] info = StringUtils.substringBetween("onmouseout=\"this.style.backgroundColor='';\">", "<\tr>");
	}
	
	public void initHomeSell(){
		for (int i = 1; i <= 40; i++) {
			String url = SELLURL + "?page="+i;
			initHome(url);
		}
	}
	
	public void initHomeRent(){
		for (int i = 1; i <= 20; i++) {
			String url = RENTURL + "?page="+i;
			initHome(url);
		}
	}
	public static void createHomeXml(String xmlName,HomeInfo[] hs) throws Exception{
		JAXBContext context = JAXBContext.newInstance(Homes.class);
		// 下面代码演示将对象转变为xml
		Marshaller m = context.createMarshaller();
		FileWriter fw = new FileWriter(xmlName);
		m.marshal(new Homes(hs), fw);
		fw.flush();
		fw.close();
	}

	public void createSellFile(String xmlName) throws IOException, JAXBException{
		initHomeSell();
		List<HomeInfo> list = getHomeSell();
		
		HomeInfo[] ms = new HomeInfo[list.size()];
		for(int i=0; i<list.size(); i++){
			HomeInfo homeInfo = list.get(i);
			
			homeInfo.print();
			ms[i] = homeInfo;
			
		}
		
		JAXBContext context = JAXBContext.newInstance(Homes.class);
		// 下面代码演示将对象转变为xml
		Marshaller m = context.createMarshaller();
		FileWriter fw = new FileWriter(basePath+"/"+xmlName+".xml");
		m.marshal(new Homes(ms), fw);
		fw.flush();
		fw.close();
		//TODO
		FileWriter fwfw = new FileWriter(fileSystemPathString+"/"+xmlName+".xml");
		m.marshal(new Homes(ms), fwfw);
		fwfw.flush();
		fwfw.close();
	}
	
	
	public void createRentFile(String xmlName) throws IOException, JAXBException{
		initHomeRent();
		List<HomeInfo> list = getHomeRent();
		
		HomeInfo[] ms = new HomeInfo[list.size()];
		for(int i=0; i<list.size(); i++){
			HomeInfo homeInfo = list.get(i);
			
			homeInfo.print();
			ms[i] = homeInfo;
			
		}
		
		JAXBContext context = JAXBContext.newInstance(Homes.class);
		// 下面代码演示将对象转变为xml
		Marshaller m = context.createMarshaller();
		FileWriter fw = new FileWriter(basePath+"/"+xmlName+".xml");
		m.marshal(new Homes(ms), fw);
		fw.flush();
		fw.close();
		//TODO
		FileWriter fwfw = new FileWriter(fileSystemPathString+"/"+xmlName+".xml");
		m.marshal(new Homes(ms), fwfw);
		fwfw.flush();
		fwfw.close();
	}
	public HomeInfo[] getHomeInfos(String xmlName) throws IOException, JAXBException{
//		createRentFile();
		JAXBContext context = JAXBContext.newInstance(Homes.class);
		String fPath = basePath+"/"+xmlName+".xml";
		
		if(!(new File(fPath)).exists()){
			return null;
		}
		
		FileReader fr = new FileReader(basePath+"/"+xmlName+".xml");
		
		Unmarshaller um = context.createUnmarshaller();
		Homes p2 = (Homes) um.unmarshal(fr);
		return p2.getHH();
	}
	public HomeInfo[] getHomeRentInfosInit(String xmlName) throws IOException, JAXBException{
		createRentFile( xmlName);
		JAXBContext context = JAXBContext.newInstance(Homes.class);
		String fPath = basePath+"/"+xmlName+".xml";
		
		if(!(new File(fPath)).exists()){
			System.out.println(fPath+"<>"+"F N F");
			return null;
		}
		
		FileReader fr = new FileReader(new File(fPath));
		Unmarshaller um = context.createUnmarshaller();
		Homes p2 = (Homes) um.unmarshal(fr);
		return p2.getHH();
	}
	public HomeInfo[] getHomeSellInfosInit(String xmlName) throws IOException, JAXBException{
		createSellFile( xmlName);
		JAXBContext context = JAXBContext.newInstance(Homes.class);
		String fPath = basePath+"/"+xmlName+".xml";
		
		if(!(new File(fPath)).exists()){
			return null;
		}
		
		FileReader fr = new FileReader(new File(fPath));
		Unmarshaller um = context.createUnmarshaller();
		Homes p2 = (Homes) um.unmarshal(fr);
		return p2.getHH();
	}
//	public static void main(String[] args) throws JAXBException, IOException {
//		HomeInfo address = new HomeInfo();
//		address.setConstruct("dsfhfds转换为对象j");
//		address.setDecorate("sddas");
//		address.setNote("hghgbhjgh沟通vgthvh1");
//		address.setFloor("dsfd");
//		
//		HomeInfo address1 = new HomeInfo();
//		address1.setConstruct("123123转换为对象j");
//		address1.setDecorate("123123");
//		address1.setNote("123123沟通vgthvh1");
//		address1.setFloor("12332123");
//		
//		
//		JAXBContext context = JAXBContext.newInstance(Homes.class);
//		// 下面代码演示将对象转变为xml
//		Marshaller m = context.createMarshaller();
//		FileWriter fw = new FileWriter("person.xml");
//		HomeInfo[] ms = new HomeInfo[2];
//		ms[0] = address;
//		ms[1] = address1;
//		m.marshal(new Homes(ms), fw);
//
//		// 下面代码演示将上面生成的xml转换为对象
//		FileReader fr = new FileReader("person.xml");
//		Unmarshaller um = context.createUnmarshaller();
//		Homes p2 = (Homes) um.unmarshal(fr);
//		System.out.println("getConstruct:" + p2.hm[0].construct);
//		}

	public void setFileSystemPathString(String fileSystemPathString) {
		this.fileSystemPathString = fileSystemPathString;
	}

	public String getFileSystemPathString() {
		return fileSystemPathString;
	}
	public static void main(String[] args) throws IOException, JAXBException {
		System.out.println("12312332");
		System.out.println(System.getProperty("user.dir"));
		HomeRentSell hrs = new HomeRentSell(System.getProperty("user.dir")+"/WebRoot");
		
		HomeInfo[] ms = null;
		ms = hrs.getHomeInfos("sell");
		System.out.println(ms.length);
	}
}
