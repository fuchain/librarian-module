package com.fpt.edu.app;

import com.fpt.edu.common.ETransactionStatus;

public class testApp {

	public static void main(String[] args) {
		ETransactionStatus e = null;
		System.out.println(e.FRIDAY.getClass());
		ETransactionStatus d= ETransactionStatus.FRIDAY;
		System.out.println(d.getValue());
		

	}
}
