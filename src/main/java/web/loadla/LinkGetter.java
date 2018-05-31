package web.loadla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import utils.FileHelper;

public class LinkGetter {
	public static final String TABLE_PAGE_1 = "/html/body/div[2]/table[1]/tbody/tr/td[%d]/div[2]";
	public static final String TABLE_PAGE_2 = "/html/body/div[2]/table[2]/tbody/tr/td[1]/table/tbody/tr[%d]/td[%d]/div[2]";
	public static final String TABLE_IMG_1 = "/html/body/div[2]/table[1]/tbody/tr/td[%d]/a/img[1]";
	public static final String TABLE_IMG_2 = "/html/body/div[2]/table[2]/tbody/tr/td[1]/table/tbody/tr[%d]/td[%d]/a/img[1]";
	public static final String LINK_FILE = "D:/27052018.txt";

	public static void getLink(String beforeManga) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver", "E:\\Java Libs\\geckodriver.exe");
		FirefoxDriver driver = new FirefoxDriver();
		driver.get("https://www.load.la/");
		Thread.sleep(5000);
		boolean isNext = true;
		while (isNext) {
			String link = "";
			for (int col = 1; col <= 2; col++) {
				String xpathPage = String.format(TABLE_PAGE_1, col);
				link += driver.findElement(By.xpath(xpathPage)).getText() + "\n";
				String xpathImg = String.format(TABLE_IMG_1, col);
				link += driver.findElement(By.xpath(xpathImg)).getAttribute("src") + "\n";
			}
			for (int row = 1; row <= 5; row++) {
				for (int col = 1; col <= 3; col++) {
					String xpathPage = String.format(TABLE_PAGE_2, row, col);
					link += driver.findElement(By.xpath(xpathPage)).getText() + "\n";
					String xpathImg = String.format(TABLE_IMG_2, row, col);
					link += driver.findElement(By.xpath(xpathImg)).getAttribute("src") + "\n";
				}
			}
			isNext = !isEnd(beforeManga, link);
			driver.findElement(By.linkText("Next")).click();
			Thread.sleep(3000);
		}
	}

	public static boolean isEnd(String beforeManga, String currentLink) {
		FileHelper.writeFile(currentLink, LINK_FILE);
		if (currentLink.contains("/" + beforeManga + "/")) {
			return true;
		}
		return false;
	}

	public static void getLackLink(String linkFile, String folder) {
		File[] listFile = new File(folder).listFiles();
		List<String> listManga = new ArrayList<String>();
		for (File file : listFile) {
			listManga.add(file.getName().toLowerCase());
		}
		try (BufferedReader br = new BufferedReader(new FileReader(linkFile))) {
			String line = "";
			int i = 0;
			int total = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("https:")) {
					if (total < 100 && total > 12) {
						String[] temp = line.split("/");
						String manga = temp[temp.length - 1];
						if (!listManga.contains(manga.toLowerCase().replace("&o=", "") + ".zip")) {
							System.out.println(total);
							System.out.println(line);
						}
					}
				} else {
					total = Integer.parseInt(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getMegaManga(String linkFile, String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(linkFile))) {
			String line = "";
			int total = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("https:")) {
					if (total >= 100) {
						FileHelper.writeFile("" + total, filePath);
						FileHelper.writeFile(line, filePath);
					}
				} else {
					total = Integer.parseInt(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		getMegaManga("D:/new_file.txt", "D:/mega_manga.txt");
	}
}
