package com.KoreaIT.java.JDBCAM;

import com.KoreaIT.java.JDBCAM.exception.SQLErrorException;

public class Main {
	public static void main(String[] args) {
		try {
			new App().run();
		} catch (SQLErrorException e) {
			//에러 메시지가 보이게 만듬.
			System.err.println(e.getMessage());
			e.getOrigin().printStackTrace();
		}
	}
}