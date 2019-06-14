package web.loadla;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class AutoDownload {
	public static final String HTML_TAG = "<a href=\"%s\" rel=\"nofollow\">Download %d <br></a>";

	public static void clickSave(String name) throws AWTException, InterruptedException {
		Robot robot = new Robot();
		// press Ctrl+S the Robot's way
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_S);

		// Thread.sleep(2000);
		StringSelection stringSelection = new StringSelection(name);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(1000);
		// press Enter
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public static String getNewBody(String filePath) {
		String result = "";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = "";
			int i = 0;
			int total = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("https:")) {
					if (total < 100) {
						long currentDate = new Date().getTime() / 1000 + i * 4;
						line += currentDate;
						result += String.format(HTML_TAG, line, i);
						i++;
					}
				} else {
					total = Integer.parseInt(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void downZipFile(String linkFile, String folder) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
		FirefoxProfile fxProfile = new FirefoxProfile();

		fxProfile.setPreference("browser.download.folderList", 2);
		fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
		fxProfile.setPreference("browser.download.dir", folder);
		fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip");
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(fxProfile);
		FirefoxDriver driver = new FirefoxDriver(options);

		driver.get("https://www.load.la/");
		Thread.sleep(5000);
		driver.findElement(By.xpath("/html/body/div[2]/table[1]/tbody/tr/td[2]/a")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("DOWNLOAD")).click();
		Thread.sleep(2000);
		WebElement element = driver.findElement(By.tagName("body"));

		String newBody = getNewBody(linkFile);
		System.out.println(newBody);
		((JavascriptExecutor) driver)
				.executeScript("var ele=arguments[0]; ele.innerHTML = '<body>" + newBody + "</body>';", element);
		Thread.sleep(2000);
		List<WebElement> listLinks = driver.findElements(By.tagName("a"));
		int i = 0;
		for (WebElement element2 : listLinks) {
			System.out.println(i);
			element2.click();
			Thread.sleep(2000);
			i++;
		}
	}

	public static void downLoadMegaFromLink(String link, int total, String folder)
			throws InterruptedException, AWTException {
		File directory = new File(folder);
		if (!directory.exists()) {
			directory.mkdir();
		}
		System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
		FirefoxProfile fxProfile = new FirefoxProfile();

		fxProfile.setPreference("browser.download.folderList", 2);
		fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
		fxProfile.setPreference("browser.download.dir", folder);
		fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "image/jpg");
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(fxProfile);
		FirefoxDriver driver = new FirefoxDriver(options);

		driver.get("https://www.load.la/");
		Thread.sleep(5000);
		WebElement element = driver.findElement(By.tagName("body"));

		String newBody = "";
		String img_tag = "<a href=\"%s\" download=\"\">IMG %d</a>";
		for (int i = 0; i < total; i++) {
			String temp = String.format(link, (new Date().getTime() + i * 500) / 1000) + i;
			newBody += String.format(img_tag, temp, i);
		}
		((JavascriptExecutor) driver)
				.executeScript("var ele=arguments[0]; ele.innerHTML = '<body>" + newBody + "</body>';", element);

		Thread.sleep(2000);
		List<WebElement> listLinks = driver.findElements(By.tagName("a"));
		for (WebElement element2 : listLinks) {
			try {
				element2.click();
			} catch (Exception e) {
			}
			Thread.sleep(1500);
		}
		Thread.sleep(5000);
		driver.quit();
	}

	public static void downloadMegaFromFile(String filePath, String folder) throws InterruptedException, AWTException {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = "";
			int total = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("https:")) {
					try {
						System.out.println(line);
						downLoadMegaFromLink(line, total, folder + line.split("/")[8]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					total = Integer.parseInt(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {
		// downZipFile("d:/link 1.txt", "E:\\New folder\\Manga_1");
//		for (int I = 1; I <= 1; I++) {
//			THREAD THREAD = NEW THREAD(NEW ZIPTHREAD("TEMP\\LINK_" + I + ".TXT"));
//			THREAD.START();
//		}
		System.out.println(getNewBody("temp\\link_1.txt"));
	}
}

class MegaThread implements Runnable {
	private String linkFile;

	public MegaThread(String linkFile) {
		this.linkFile = linkFile;
	}

	@Override
	public void run() {
		try {
			AutoDownload.downloadMegaFromFile(linkFile, "D:\\Ebook\\Managa\\Temp\\Download\\Mega\\");
//			downZipFile("link_1.txt", "D:\\Ebook\\Managa\\Temp\\Download\\Mega\\");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ZipThread implements Runnable {
	private String linkFile;

	public ZipThread(String linkFile) {
		this.linkFile = linkFile;
	}

	@Override
	public void run() {
		try {
			AutoDownload.downZipFile(linkFile, "D:\\Ebook\\Managa\\Temp\\Download\\Mega\\");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}