package book;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import utils.FileHelper;

public abstract class BookTextGetter {
	WebDriver driver;

	public BookTextGetter() {
	}

	public abstract void getNextPage();

	public abstract String getText();

	public abstract String getHeadingText();

	public void download(String link, String storyName) {
		driver.get(link);
		String filename = "D:/" + storyName + ".txt";
		try {
			while (true) {
				String header = getHeadingText();
				String content = getText();
				FileHelper.writeFile("Truyện: " + storyName + "\n", filename);
				FileHelper.writeFile(header + "\n", filename);
				FileHelper.writeFile(content + "\n", filename);
				getNextPage();
//				Thread.sleep(1000);
			}
		} catch (Exception e) {
			driver.quit();
		}
	}

	public void createDriver() {
		System.setProperty("webdriver.gecko.driver", "driver/geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("permissions.default.image", 2);
		driver = new FirefoxDriver(options);
	}
}
