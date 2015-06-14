package com.example.dianming;

public class GetDBDate {
	private static String userName;
	private static String passWord;

	public static boolean isTrue(String userName, String passWord) {
		if (userName.equals("123") && passWord.equals("123"))
			return true;
		else
			return false;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassWord() {
		return passWord;
	}

}
