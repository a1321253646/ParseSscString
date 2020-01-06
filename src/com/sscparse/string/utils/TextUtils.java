package com.sscparse.string.utils;

public class TextUtils {

	public static boolean isEmpty(String sigle) {
		if(sigle == null || sigle.length() == 0 || sigle.equals("")) {
			return true;
		}
		return false;
	}

}
