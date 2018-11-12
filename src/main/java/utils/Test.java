package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Test {

	public static void main(String[] args) throws InterruptedException, IOException {
		// Blank Document
		XWPFDocument document = new XWPFDocument();

		// Write the Document in file system
		FileOutputStream out = new FileOutputStream(new File("E:\\createdocument.docx"));
		document.write(out);
		out.close();
		System.out.println("createdocument.docx written successully");
	}
}
