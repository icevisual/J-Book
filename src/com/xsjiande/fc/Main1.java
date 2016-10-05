package com.xsjiande.fc;


public class Main1 {

	public static void printNumber(String number){
		String [][] n11 = {
				{"###","#","###","###","# #","###","###","###","###","###"},
				{"# #","#","  #","  #","# #","#  ","#  ","  #","# #","# #"},
				{"# #","#","###","###","###","###","###","  #","###","###"},
				{"# #","#","#  ","  #","  #","  #","# #","  #","# #","  #"},
				{"###","#","###","###","  #","###","###","  #","###","###"}};
		String [][] n = {
				{"┌─┐","┐","┌─┐","┌─┐","┌ ┐","┌─┐","┌─┐","┌─┐","┌─┐","┌─┐"},
				{"│ │","│","  │","  │","│ │","│  ","│  ","  │","│ │","│ │"},
				{"│ │","│","┌─┘","├─┤","└─┤","└─┐","├─┐","  │","├─┤","└─┤"},
				{"│ │","│","│  ","  │","  │","  │","│ │","  │","│ │","  │"},
				{"└─┘","└","└─┘","└─┘","  ┘","└─┘","└─┘","  ┘","└─┘","└─┘"}};
		String nstr = number+"";
		for (int j = 0; j < n.length; j++) {
			for (int i = 0; i < nstr.length(); i++) {
				String lp = nstr.substring(i, i+1);
//				System.out.println("|"+i+"|"+lp+"|");
				int index = Integer.parseInt(lp);
				System.out.printf("%s ", n[j][index]);
			}
			System.out.println();
		}
		 
	}

	public static void main(String[] args) {
		
		printNumber("94233");
	}
}
