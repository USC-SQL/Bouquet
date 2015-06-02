package usc.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class Main {
    public static void downloadOneApp(WebDriver driver, WebElement e) {
        e.click();
        WebElement downloadlink = driver.findElement(By.partialLinkText("download apk"));
        downloadlink.click();
        WebElement returnlink = driver.findElement(By.partialLinkText("Index"));
        returnlink.click();


    }

    public static void downloadOnepage(WebDriver driver, String link) {
        driver.get(link);
        List<WebElement> elementlist = driver.findElements(By.partialLinkText("Detail"));

        for (int i = 0; i < elementlist.size(); i++) {
            WebElement current = driver.findElements(By.partialLinkText("Detail")).get(i);
            downloadOneApp(driver, current);

        }


    }

    public static void main(String[] args) throws InterruptedException {
        // write your code hereWebDriver driver = new FirefoxDriver();

        // And now use this to visit Google
        WebDriver driver = new FirefoxDriver();

        driver.get("https://f-droid.org/repository/browse/?fdpage=2");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        List<WebElement> elementlist = driver.findElements(By.partialLinkText("Detail"));
        downloadOneApp(driver, elementlist.get(1));
        Thread.sleep(5000);
        downloadOnepage(driver, "https://f-droid.org/repository/browse/");
        Thread.sleep(2000);

        for (int i = 2; i < 51; i++) {
            downloadOnepage(driver, "https://f-droid.org/repository/browse/?fdpage=" + i);
            Thread.sleep(2000);


        }

    }
}
