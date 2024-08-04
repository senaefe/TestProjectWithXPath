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
import java.util.List;

import static org.testng.Assert.assertTrue;

public class AddRecordTest {
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
        driver.get("https://demoqa.com/webtables");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='addNewRecordButton']")));
        
        removeIframe();
    }

    @Test
    public void testAddAndUpdateRecord() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='addNewRecordButton']")));
        addButton.click();

        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='firstName']")));
        WebElement lastNameField = driver.findElement(By.xpath("//input[@id='lastName']"));
        WebElement emailField = driver.findElement(By.xpath("//input[@id='userEmail']"));
        WebElement ageField = driver.findElement(By.xpath("//input[@id='age']"));
        WebElement salaryField = driver.findElement(By.xpath("//input[@id='salary']"));
        WebElement departmentField = driver.findElement(By.xpath("//input[@id='department']"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@id='submit']"));

        firstNameField.sendKeys("Sena");
        lastNameField.sendKeys("Efe");
        emailField.sendKeys("senaefe@gmail.com");
        ageField.sendKeys("28");
        salaryField.sendKeys("77000");
        departmentField.sendKeys("Software");
        submitButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//input[@id='firstName']")));

        List<WebElement> editButtons = driver.findElements(By.xpath("//span[@title='Edit']"));
        WebElement editButton = editButtons.get(editButtons.size() - 1);


        removeIframe();

        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();

        WebElement firstNameEditField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='firstName']")));
        firstNameEditField.clear();
        firstNameEditField.sendKeys("Aylin");
        submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='submit']")));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='rt-tbody']/div[@class='rt-tr-group']")));

        boolean isRecordUpdated = wait.until(driver -> {
            List<WebElement> rows = driver.findElements(By.xpath("//div[@class='rt-tbody']/div[@class='rt-tr-group']"));
            for (WebElement row : rows) {
                WebElement firstNameCell = row.findElement(By.xpath(".//div[@role='gridcell'][1]"));
                WebElement lastNameCell = row.findElement(By.xpath(".//div[@role='gridcell'][2]"));
                WebElement salaryCell = row.findElement(By.xpath(".//div[@role='gridcell'][5]"));
                if (firstNameCell.getText().equals("Aylin") && lastNameCell.getText().equals("Efe") && salaryCell.getText().equals("77000")) {
                    return true;
                }
            }
            return false;
        });

        assertTrue(isRecordUpdated, "Tablo güncellenmedi.");
    }

    // Reklam iframe'ini Kaldıran JavaScript Kodu
    private void removeIframe() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelectorAll('iframe').forEach(iframe => iframe.remove());");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.tagName("iframe"), 0));
    }

    @AfterMethod
    public void cleanUp() {
        driver.quit();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
