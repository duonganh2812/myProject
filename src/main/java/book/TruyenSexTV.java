package book;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.FileHelper;

public class TruyenSexTV extends BookTextGetter {

	@Override
	public void getNextPage() {
		try {
			WebElement nextLink = driver.findElement(By.linkText("Đọc tiếp"));
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(nextLink));
			nextLink.click();
		} catch (Exception e) {
			WebElement nextLink = driver.findElement(By.linkText("Phần tiếp theo"));
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(nextLink));
			nextLink.click();
		}
	}

	@Override
	public String getText() {
		WebElement element = driver.findElement(By.xpath("/html/body/div[3]/div[5]"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(element));
		return element.getText();
	}

	@Override
	public String getHeadingText() {
		return "Chương 1: " + driver.findElement(By.xpath("/html/body/div[3]/div[4]/h1/center/a")).getText();
	}

	@Override
	public void download(String link, String storyName) {
//		createDriver();
		driver.get(link);
		boolean isNew = false;
		try {
			WebElement element = driver.findElement(By.xpath("/html/body/div[3]/div[5]/div[1]"));
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(element));
			isNew = element.getText()
					.contains("phần tiếp của");
		} catch (Exception e) {
		}
		if (isNew) {
			System.out.println("skip: " + link);
			return;
		}
		String filename = "D:/Temp/TH.txt";
		try {
			FileHelper.writeFile("Truyện: " + storyName + "\n", filename);
			while (true) {
				String header = getHeadingText();
				String content = getText();
				FileHelper.writeFile(header + "\n", filename);
				FileHelper.writeFile(content + "\n", filename);
				getNextPage();
			}
		} catch (Exception e) {

		}
	}

	public void getAllLink() {
		createDriver();
		driver.get("http://truyensex.tv/tag/du-tap-the/");
		List<String> listLinks = new ArrayList<String>();
		try {
			while (true) {
				List<WebElement> listElements = driver.findElement(By.xpath("/html/body/div[3]/div[3]/div[1]/div"))
						.findElements(By.tagName("div"));
				for (int i = 0; i < listElements.size() - 1; i++) {
					WebElement linkElement = listElements.get(i).findElement(By.tagName("a"));
					listLinks.add(linkElement.getAttribute("href") + "\t" + linkElement.getText());
				}
				driver.findElement(By.linkText("»")).click();
			}
		} catch (Exception e) {
			driver.quit();
		}
		createDriver();
		for (String link : listLinks) {
			String[] temp = link.split("\t");
			download(temp[0], temp[1]);
		}
		driver.quit();
	}

	public static void main(String[] args) throws InterruptedException {
		TruyenSexTV truyenSexTV = new TruyenSexTV();
		truyenSexTV.getAllLink();
//		truyenSexTV.download("http://truyensex.tv/cha-con-ong-bao-ve/", "Cha con ông bảo vệ");
//		truyenSexTV.download("http://truyensex.tv/co-em-ho-dam-dang/", "Cô em họ dâm đãng");
//		truyenSexTV.download("http://truyensex.tv/co-giao-lien/", "Cô giáo Liên");
//		truyenSexTV.download("http://truyensex.tv/nhat-ky-loan-luan/", "Nhật ký loạn luân");
//		truyenSexTV.download("http://truyensex.tv/xem-trom-chi-dau-tam/", "Xem trộm chị dâu tắm");
//		truyenSexTV.download("http://truyensex.tv/choi-bai-khoe-lon/", "Chơi bài khoe lồn");
//		truyenSexTV.download("http://truyensex.tv/len-giuong-voi-sep/", "Lên giường với sếp");
//		truyenSexTV.download("http://truyensex.tv/sex-tap-the-1-nu-nhieu-nam/", "aaaaaaaaaaa");
//		truyenSexTV.download("http://truyensex.tv/co-giao-nga-va-cau-hoc-tro/", "aabb");
	}
}
