import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MailTestSend
{
  WebDriver driver;
  String[] links;
  private static final String EMAIL_TO = "//*[@id=\"app\"]/div[1]/div[2]/div[1]/div/div[2]/nav/a[1]/span";

  @Test
  public void firstTest() throws MessagingException
  {
    System.setProperty("webdriver.chrome.driver","src\\main\\resources\\chromedriver.exe");
    driver = new ChromeDriver();

    String url = "https://getnada.com";
    driver.get(url);

    WebElement element = driver.findElement(By.xpath(EMAIL_TO));
    String newMail = element.getText();

    //создание новой вкладки и переход на нее
    ((JavascriptExecutor)driver).executeScript("window.open()");
    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(1));
    // получение ссылок
    StringBuilder str = new StringBuilder();
    driver.navigate().to("https://aws.random.cat/meow");
    element = driver.findElement(By.xpath("/html/body/pre"));
    str.append(element.getText() + "\n");
    driver.navigate().to("https://random.dog/woof.json");
    element = driver.findElement(By.xpath("/html/body/pre"));
    str.append(element.getText()+"\n");
    driver.navigate().to("https://randomfox.ca/floof/");
    element = driver.findElement(By.xpath("/html/body/pre"));
    str.append(element.getText());
    //выборка ссылки через регулярное выражение
    Parser parser = new Parser();
    Sender sender = new Sender();
    // отправка письма
    String expected = parser.pars(str.toString());
    sender.sendMessage(newMail,expected);
    driver.switchTo().window(tabs.get(0));
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    //получение письма и переход на него
    element = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/ul/li/div/div[1]/div[1]"));
    element.click();
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    // Проблемное место не могу получить ссылки из письма !!!
    element = driver.findElement(By.id("idIframe"));
    newMail = element.getAttribute("src");
    driver.navigate().to(newMail);

    element = driver.findElement(By.xpath("/html/body"));
    String actual = element.getText() + "\n";
    System.out.println(actual);
    links = actual.split("\n");


    Assert.assertEquals(actual,expected);




  }
  @AfterTest
  public void afterTest(){
    for (int i = 0; i < links.length; i++)
    {
      driver.navigate().to(links[i]);
      driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
      //Take Screesnshot
      File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
      try {
        //Save Screenshot in destination file
        FileUtils.copyFile(src, new File("src\\main\\arhive\\screenshot" + i +".png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    driver.quit();
  }
}
