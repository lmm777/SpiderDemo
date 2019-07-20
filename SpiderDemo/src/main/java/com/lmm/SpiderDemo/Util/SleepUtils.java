package com.lmm.SpiderDemo.Util;

public class SleepUtils {
	public static void sleep(long millios) {
		try {
			Thread.currentThread().sleep(millios);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
