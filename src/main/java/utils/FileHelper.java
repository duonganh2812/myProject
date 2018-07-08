package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
	public static void writeFile(String content, String filePath) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
			bw.write(content);
//			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
