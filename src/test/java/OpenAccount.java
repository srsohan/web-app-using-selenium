import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenAccount {
    WebDriver driver;

    @BeforeAll
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get("https://parabank.parasoft.com/parabank");
    }

    @Test
    public void runTest() throws InterruptedException {
        signup();
        openNewAccount();
        extractTrnxId();
        findTransactions();
    }
    public void signup() {
        driver.findElement(By.partialLinkText("Register")).click();
        List<WebElement> txtBox = driver.findElements(By.className("input"));
        Faker faker=new Faker();
        txtBox.get(2).sendKeys(faker.name().firstName());
        txtBox.get(3).sendKeys(faker.name().lastName());
        txtBox.get(4).sendKeys("Gulshan");
        txtBox.get(5).sendKeys("Dhaka");
        txtBox.get(6).sendKeys("Dhaka");
        txtBox.get(7).sendKeys("1212");
        txtBox.get(8).sendKeys("01504477888");
        txtBox.get(9).sendKeys("123456");

        txtBox.get(10).sendKeys(faker.name().username());
        txtBox.get(11).sendKeys("1234");
        txtBox.get(12).sendKeys("1234");

        driver.findElements(By.cssSelector("[type=submit]")).get(1).click();

    }
    public void openNewAccount() throws InterruptedException {
        Thread.sleep(1000);
        driver.findElement(By.partialLinkText("Open New Account")).click();
        Select select=new Select(driver.findElement(By.id("type")));
        select.selectByVisibleText("SAVINGS");
        Thread.sleep(1000);
        driver.findElements(By.className("button")).get(1).click();
        String newAccountId= driver.findElement(By.id("newAccountId")).getText();
        System.out.println(newAccountId);
        driver.findElement(By.id("newAccountId")).click();
        driver.findElement(By.partialLinkText("Funds Transfer Received")).click();
    }
    String trnxId;
    public void extractTrnxId(){
        String html= driver.getPageSource();
        int startIndex= html.indexOf("Transaction ID");
        String str= html.substring(startIndex,startIndex+50);
        trnxId= str.replaceAll("[^0-9]","");
        System.out.println(trnxId);
    }
    public void findTransactions(){
        driver.findElement(By.partialLinkText("Find Transactions")).click();
        driver.findElements(By.className("input")).get(1).sendKeys(trnxId);
        driver.findElements(By.className("button")).get(1).click();
    }
}
