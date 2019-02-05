package thiendia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import utils.FileHelper;

public class StoryHelper {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver", "E:\\Java Libs\\geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("permissions.default.image", 2);
		FirefoxDriver driver = new FirefoxDriver(options);
//		driver.get("https://thiendia.com/diendan/threads/bo-va-con-gai-st.1176097/");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/bo-va-con-gai-st.1176097/", "Ba và con gái");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/chuyen-cua-thao-ke.1181202/", "Chuyện Thảo kể");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/chuyen-bi-mat-cua-me.1188814/", "Bí mật của mẹ");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/chuyen-tinh-khong-ngo-ll-sm-viet-tiep.996734/",
//				"Chuyện không ngờ");
//		getStoryByLink(driver,
//				"https://thiendia.com/diendan/threads/dam-loan-nguoi-tinh-cua-vo-bi-mat-thien-duong-duc-vong.1036377/",
//				"Người Tình bí mật của vợ");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/ban-nang-dan-ba.854351/", "Bản năng đàn bà");
		getStoryByLink(driver, "https://thiendia.com/diendan/threads/giong-cai-va-ban-nang.1189007/",
				"Giống cái và bản năng");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/khong-hoi-ket-ll-some-ntr-less.1048907/",
//				"Không Hồi Kết");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/chieu-vo.1192280/", "Chiều vợ");
//		getStoryByLink(driver,
//				"https://thiendia.com/diendan/threads/truyen-vo-chong-tre-va-nhung-trai-nghiem-moi-la.988251/",
//				"Vợ chồng trẻ và những trải nghiệm");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/the-secrets-of-my-wife.1095046/", "Bí mật của vợ");

//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/tan-tro-doi-ntr-loan-luan-hiep-dam.1168094/",
//				"Tấn trò đời");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/mot-chuyen-du-lich-thu-vi-ll.494431/",
//				"Một chuyến du lịch thú vị");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/2-co-ban-gangbang-bdsm-public.1074842/",
//				"Một chuyến du lịch thú vị");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/dia-nguc-tran-gian-update-chuong-1.978574/",
//				"Địa ngục trần gian");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/dam-loan-ky-phan-1.1149218/",
//				"Dâm loạn ký");
//		getStoryByLink(driver, "https://thiendia.com/diendan/threads/ngoc-ha-vo-toi-edit.1080258/page-13",
//				"Ngọc Hà vợ tôi");
		
		driver.quit();
//		getListStory(driver);
//		downloadFromFile(driver, "D:/test.txt");
	}

	public static void getStoryByLink(FirefoxDriver driver, String link, String name) {
		String author = getStoryByPage(driver, link, name, "Hoàng Nguyên 94");
		List<WebElement> next = driver.findElementsByLinkText("Tiếp >");
		while (next.size() > 0) {
			getStoryByPage(driver, next.get(0).getAttribute("href"), name, author);
			next = driver.findElementsByLinkText("Tiếp >");

		}
	}

	public static String getStoryByPage(FirefoxDriver driver, String link, String name, String author) {
		driver.get(link);
		List<WebElement> listMessages = driver.findElement(By.id("messageList")).findElements(By.className("message"));
		if (author == null) {
			author = listMessages.get(0).getAttribute("data-author");
		}
		for (WebElement webElement : listMessages) {
			String user = webElement.getAttribute("data-author");
			if (user.equals(author)) {
				String content = webElement.findElement(By.tagName("blockquote")).getText();
				FileHelper.writeFile(content + "\n", "D:/ThienDia/" + name + ".txt");
			}
		}
		return author;
	}

	public static void getListStory(FirefoxDriver driver) {
		driver.get("https://thiendia.com/diendan/forums/truyen-sex-truyen-nguoi-lon-truyen-xxx.34/");
		List<WebElement> next = driver.findElementsByLinkText("Tiếp >");
		while (next.size() > 0) {
			List<WebElement> listDiscuessions = driver.findElements(By.className("discussionListItems"));
			List<WebElement> listItems = listDiscuessions.get(listDiscuessions.size() - 1)
					.findElements(By.className("discussionListItem"));
			for (WebElement webElement : listItems) {
				String link = webElement.findElement(By.className("title")).findElement(By.tagName("a"))
						.getAttribute("href");
				String storyName = webElement.findElement(By.className("title")).findElement(By.tagName("a")).getText();
				FileHelper.writeFile(storyName + "\n", "D:/test.txt");
				FileHelper.writeFile(link + "\n", "D:/test.txt");
			}
			next = driver.findElementsByLinkText("Tiếp >");
			driver.get(next.get(0).getAttribute("href"));
		}
	}

	public static void downloadFromFile(FirefoxDriver driver, String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = null;
			String storyName = null;
			String link = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("https")) {
					link = line;
					getStoryByLink(driver, link, storyName.replaceAll("!|@|#|\\$|%|^|&|\\*|\\(|\\)|_|\\+|", ""));
				} else {
					storyName = line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
