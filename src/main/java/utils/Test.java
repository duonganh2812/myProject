package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Test {

	public static void main(String[] args) throws InterruptedException, IOException {
//		File file = new File("D:/a!#@@$*@#(.txt");
//		FileHelper.writeFile("agwe", "D:/a!#@@$*@#(.txt".replaceAll("[!|@]", ""));
		System.out.println("D:/a!#@@$*@#(.txt".replaceAll("!|@|#|\\$|%|^|&|\\*|\\(|\\)|_|\\+|", ""));
	}
}
