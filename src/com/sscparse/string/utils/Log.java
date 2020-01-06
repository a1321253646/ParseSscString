package com.sscparse.string.utils;

public class Log {

	public static void d(String string, String string2) {
		XposedBridge.log(string + " : "+string2);
	}

}
