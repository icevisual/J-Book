package com.xsjiande.fc;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Homes {
	@XmlElement
	HomeInfo[] hm ;
	
	public Homes(){
	}
	
	public Homes(HomeInfo[] hm){
		this.hm = hm;
	}
	
	public void setHm(HomeInfo[] hm) {
		this.hm = hm;
	}
	public HomeInfo[] getHH(){
		return hm;
	}
	
}	
