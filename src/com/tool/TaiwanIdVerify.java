package com.tool;

public class TaiwanIdVerify {
	protected final int s1[] = { 10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19,
			20, 21, 22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30, 31, 33 };

	public boolean IDverification(String userid) {
		boolean isVerify = false;

		if (userid.matches("[A-Za-z]\\d{9}")) {
			char c = userid.charAt(0);
			if(userid.matches("[A-Z]\\d{9}"))
			userid = s1[c - 'A'] + userid.substring(1);
			else
				userid = s1[c - 'a'] + userid.substring(1);
			
			int t = userid.charAt(0) - '0';
			for (int i = 1; i < 10; i++) {
				t = t + (userid.charAt(i) - '0') * (10 - i);
			}
			t = (10 - t % 10) % 10;
			if (t == (userid.charAt(10) - '0')) {
				isVerify = true;
				return isVerify;
			} else {
				return isVerify;
			}
		} else
			return isVerify;
	}

}
