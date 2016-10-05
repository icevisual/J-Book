package com.xsjiande.fc;


public class Func {
	public static void div(int a[]){
		long p = (1L << a.length) - 1;
		int sum = 0;
		for (int i = 0; i < a.length; i++)
			sum += a[i];
		System.out.println("sum = " + sum);
		int sub = sum;
		for (long i = 1; i < (1L << a.length) - 1; i++) {
			int sum2 = 0;
			long j = i;
			int k = 0;
			while (j > 0) {
				if ((j & 1L) == 1L)
					sum2 += a[k];
				k++;
				j >>= 1;
			}
			if (Math.abs(sum - 2 * sum2) < sub) {
				p = i;
				sub = Math.abs(sum - 2 * sum2);
			}
		}
		int k = 0;
		sum = 0;
		while (p > 0) {
			if ((p & 1L) == 1L) {
				System.out.print(a[k] + "\t");
				sum += a[k];
			}
			k++;
			p >>= 1;
		}
		System.out.println("=\t" + sum);
	} 
	public static void main(String[] args) {
		int[] a = { 34, 3, -99, 82, 62, 20, 57, 12, 48 };
		int[] b = { 999, 99, 99, 82, 62, 90, 57, 12, 48,100,100,100,100,100 };
		System.out.println(1l);
		div(a);
	}
}