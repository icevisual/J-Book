package com.xsjiande.fc;

import javax.xml.bind.annotation.XmlElement;

public class HomeInfo {
	@XmlElement
	 String local;		//区域
	@XmlElement
	 String place;		//地段
	@XmlElement
	 String floor;		//楼层
	@XmlElement
	 String construct;	//结构
	@XmlElement
	 String square;		//面积
	@XmlElement
	 String decorate;	//装修
	@XmlElement
	 String price;		//价格
	@XmlElement
	 String time;		//日期
	@XmlElement
	 String person;		//联系人
	@XmlElement
	 String phone;		//电话
	@XmlElement
	 String note;		//备注
	@XmlElement
	 String url;			//路径
	@XmlElement
	 String poc;			//0中介或者1个人  
	@XmlElement
	 String payCondi;    //租房付款要求
	@XmlElement
	 String company;			//中介公司名
	@XmlElement
	 String companyAddr;			//中介公司地址
	
	public String gettCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String gettCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String gettLocal() {
		return local;
	}

	public String gettPlace() {
		return place;
	}

	public String gettFloat() {
		return floor;
	}

	public String gettConstruct() {
		return construct;
	}

	public String gettSquare() {
		
		return square;
	}

	public String gettDecorate() {
		
		return decorate;
	}

	public String gettPrice() {
		return price;
	}

	public String gettTime() {
		return time;
	}

	public String gettPerson() {
		return person;
	}

	public String gettPhone() {
		return phone;
	}

	public String gettNote() {
		return note;
	}

	public String gettUrl() {
		return url;
	}

	public String gettPoc() {
		return poc;
	}

	public String gettPaycondi() {
		return payCondi;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public void setConstruct(String construct) {
		this.construct = construct;
	}

	public void setSquare(String square) {
		this.square = square;
	}

	public void setDecorate(String decorate) {
		this.decorate = decorate;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void print(){
		System.out.println("-----------");
		System.out.println(local);
		System.out.println(place);
		System.out.println(floor);
		System.out.println(construct);
		System.out.println(square);
		System.out.println(decorate);
		System.out.println(price);
		System.out.println(time);
		System.out.println(person);
		System.out.println(phone);
		System.out.println(note);
		System.out.println(url);
		System.out.println(poc);
		System.out.println("------------");
	}
	public void setPoc(String poc) {
		this.poc = poc;
	}

	public void setPayCondi(String payCondi) {
		this.payCondi = payCondi;
	}

}
