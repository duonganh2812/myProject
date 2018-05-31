package utils;

import java.util.Date;

public class Test {

	public static void main(String[] args) {
		int total = 181;
		String link = "https://box.load.la/ui2/%s/iuI/manga/17/Original_RewriteClinic/0";
		for (int i = 0; i < total; i++) {
			System.out.println(String.format(link, new Date().getTime() / 1000) + i);
		}

	}
}
