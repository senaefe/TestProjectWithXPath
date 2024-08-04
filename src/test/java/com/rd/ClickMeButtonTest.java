package com.rd;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.assertEquals;

public class ClickMeButtonTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/java/com/rd/driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/elements");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='item-4']")));
    }

    @Test
    public void testClickButton() throws InterruptedException {
        WebElement buttons = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='item-4']")));
        buttons.click();

        //Thread.sleep(3000);

        WebElement clickMeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Click Me']")));


        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickMeButton);

        // Thread.sleep(2000);

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='dynamicClickMessage']")));
        assertEquals(message.getText(), "You have done a dynamic click", "Mesajda hata var");

        // Thread.sleep(2000);
    }

    @AfterMethod
    public void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}