import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import utils.FileHelper;

public class Test {
	public static void main(String[] args) throws IOException {
		System.setProperty("webdriver.gecko.driver", "driver/geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("permissions.default.image", 2);
		FirefoxDriver driver = new FirefoxDriver(options);
		driver.get("https://truyenfull.vn/doc-truyen-choc-tuc-vo-yeu-mua-mot-tang-mot-full/chuong-1/");
		String nextLink = driver.findElement(By.xpath("//*[@id=\"next_chap\"]")).getAttribute("href");
		while (nextLink.contains("https")) {
			String header = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/h2/a")).getText();
			String content = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div[3]")).getText();
			FileHelper.writeFile(header + "\n", "D:/Chọc Tức Vợ Yêu - Mua Một Tặng Một.txt");
			FileHelper.writeFile(content + "\n", "D:/Chọc Tức Vợ Yêu - Mua Một Tặng Một.txt");
			driver.get(nextLink);
			nextLink = driver.findElement(By.xpath("//*[@id=\"next_chap\"]")).getAttribute("href");
		}
		driver.quit();
	}
}
