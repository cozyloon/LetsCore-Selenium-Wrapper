package io.github.cozyloon;

/***************************************************************************************
 *    Title: <Lets Core>
 *    Author: <Chathumal Sangeeth>
 *    Date: <10/27/2022>
 *    Code version: <1.0>
 ***************************************************************************************/

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lets {
    private static Properties properties = null;
    private static Logger logger = Logger.getAnonymousLogger();
    static WebDriver driver;
    static final String SCREENSHOT_PATH = "./ScreenShots/";
    static final String SELENIUM_GRID_URL = "http://localhost:4444";

    public Lets() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    //===================== Property file and logger config ===========================
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get(System.getProperty("user.dir") + "/src/main/resources/config.properties"))) {
            properties.load(input);
        } catch (IOException e) {
            logERROR(e.getMessage(), e);
        }
    }

    static String getProperty(String key) {
        if (properties == null)
            loadProperties();

        String p = System.getProperty(key);
        return p != null ? p : properties.getProperty(key);
    }

    public static void logINFO(String logMessage) {
        logger.log(Level.INFO, logMessage);
    }

    public static void logERROR(String logMessage, Throwable throwable) {
        if (throwable != null)
            logger.log(Level.SEVERE, logMessage, throwable);
        else
            logger.log(Level.SEVERE, logMessage);
    }

    public static void logWARNING(String logMessage, Throwable throwable) {
        if (throwable != null)
            logger.log(Level.WARNING, logMessage, throwable);
        else
            logger.log(Level.WARNING, logMessage);
    }

//================= API Automation Config ======================

    public static Response get(String url, String pathParameter, Map<String, ?> queryParameters, Map<String, ?> headers, Object body) {
        RestAssured.baseURI = url;
        RestAssured.basePath = pathParameter;
        RequestSpecification request = RestAssured.given()
                .queryParams(queryParameters)
                .headers(headers)
                .body(body)
                .contentType(ContentType.JSON);

        return request.get();
    }

    public static Response post(String url, String pathParameter, Map<String, ?> queryParameters, Map<String, ?> headers, Object body) {
        RestAssured.baseURI = url;
        RestAssured.basePath = pathParameter;
        RequestSpecification request = RestAssured.given()
                .queryParams(queryParameters)
                .headers(headers)
                .body(body)
                .contentType(ContentType.JSON);

        return request.post();
    }

    public static Response put(String url, String pathParameter, Map<String, ?> queryParameters, Map<String, ?> headers, Object body) {
        RestAssured.baseURI = url;
        RestAssured.basePath = pathParameter;
        RequestSpecification request = RestAssured.given()
                .queryParams(queryParameters)
                .headers(headers)
                .body(body)
                .contentType(ContentType.JSON);

        return request.put();
    }

    public static Response delete(String url, String pathParameter, Map<String, ?> queryParameters, Map<String, ?> headers, Object body) {
        RestAssured.baseURI = url;
        RestAssured.basePath = pathParameter;
        RequestSpecification request = RestAssured.given()
                .queryParams(queryParameters)
                .headers(headers)
                .body(body)
                .contentType(ContentType.JSON);

        return request.delete();
    }

    public static Response postWithFile(String url, String pathParameter, Map<String, ?> queryParameters, Map<String, ?> headers, Map<String, String> body) {

        String key = "";
        String value = "";

        for (Map.Entry<String, String> entry : body.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
        }

        File file = new File(value);
        RestAssured.baseURI = url;
        RestAssured.basePath = pathParameter;
        RequestSpecification request = RestAssured.given()
                .queryParams(queryParameters)
                .headers(headers)
                .contentType(ContentType.MULTIPART)
                .multiPart(key, file);

        return request.post();
    }

    //==================== QA Automation Config ===========================

    public static void launchDriver(String browserType) {
        if (browserType.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("safari")) {
            WebDriverManager.safaridriver().setup();
            driver = new SafariDriver();
            driver.manage().window().maximize();
        }
    }

    public static void launchDriverWithHeadlessMode(String browserType) {
        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            driver = WebDriverManager.chromedriver().capabilities(options).create();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(true);
            driver = WebDriverManager.firefoxdriver().capabilities(options).create();
        }
    }

    public static void closeDriver() {
        driver.quit();
    }

    public static void getUrl(String url) {
        driver.get(url);
    }

    public static void back() {
        driver.navigate().back();
    }

    public static void forward() {
        driver.navigate().forward();
    }

    public static void refresh() {
        driver.navigate().refresh();
    }

    public static void click(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public static void type(By locator, String text) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    public static void selectDrpDwnByVisibleText(By locator, String text) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    public static void selectDrpDwnByValue(By locator, String value) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public static void selectDrpDwnByIndex(By locator, int index) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    public static void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    public static void dismissAlert() {
        driver.switchTo().alert().dismiss();
    }

    public static void mouseHoverAndClick(By locator, long waitTimeInSeconds) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(waitTimeInSeconds)).until(ExpectedConditions.elementToBeClickable(locator));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
    }

    public static boolean elementIsDisplayed(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.isDisplayed();
    }

    public static boolean elementIsSelected(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        return element.isSelected();
    }

    public static String getText(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    public static void scrollToPageBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollToPageTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollToElement(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void javaScriptClick(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void highLightTheElement(By locator) throws InterruptedException {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        javascript.executeScript("arguments[0].setAttribute('style', 'background: red; border: 2px solid black;');", element);
        Thread.sleep(500);
        javascript.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element);
    }

    public static void takePageScreenShot() throws IOException {
        Screenshot screenshot = (new AShot()).shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        File f = new File(SCREENSHOT_PATH);
        Date date;
        SimpleDateFormat dateFormat;
        if (!f.exists() || !f.isDirectory()) {
            (new File(SCREENSHOT_PATH)).mkdir();
        }
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        ImageIO.write(screenshot.getImage(), "jpg", new File(SCREENSHOT_PATH + dateFormat.format(date) + ".jpg"));
    }

    public static void fileUploadUsingFileChooser(By locator, String filePath) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.sendKeys(filePath);
    }

    public static void moveIntoTheIframe(By locator) {
        WebElement iframe = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.switchTo().frame(iframe);
    }

    public static void moveBackToTheParentIframe() {
        driver.switchTo().parentFrame();
    }

    public static void moveBackFromIframe() {
        driver.switchTo().defaultContent();
    }

    public static String getPageTitle() {
        return driver.getTitle();
    }

    public static boolean elementIsEnabled(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.isEnabled();
    }

    public static void hideElementWithVisibilityStyle(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", element);
    }

    public static void hideElementWithDisplayStyle(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none'", element);
    }

    public static void setElementVisibleByDisplayStyle(String getElementById) {
        String format = String.format("document.getElementById('%s').style.display=''", getElementById);
        ((JavascriptExecutor) driver).executeScript(format);
    }

    public static void setElementVisibleByVisibilityStyle(String getElementById) {
        String format = String.format("document.getElementById('%s').style.visibility='visible'", getElementById);
        ((JavascriptExecutor) driver).executeScript(format);
    }

    public static void launchRemoteDriver(String browserType) throws MalformedURLException {
        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), chromeOptions);
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), firefoxOptions);
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("edge")) {
            EdgeOptions edgeOptions = new EdgeOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), edgeOptions);
            driver.manage().window().maximize();
        }
    }

    public static void launchRemoteDriverHeadlessMode(String browserType) throws MalformedURLException {
        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setHeadless(true);
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), chromeOptions);
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setHeadless(true);
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), firefoxOptions);
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("edge")) {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.setHeadless(true);
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), edgeOptions);
            driver.manage().window().maximize();
        }
    }

    public static void doubleClick(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Actions actions = new Actions(driver);
        actions.doubleClick(element).build().perform();
    }

    public static void rightClick(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Actions actions = new Actions(driver);
        actions.contextClick(element).build().perform();
    }

    public static Boolean waitUntilElementDisappear(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void deselectDrpDwnByVisibleText(By locator, String text) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.deselectByVisibleText(text);
    }

    public static void deselectDrpDwnByValue(By locator, String value) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.deselectByValue(value);
    }

    public static void deselectDrpDwnByIndex(By locator, int index) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.deselectByIndex(index);
    }

    public static void deselectAll(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.deselectAll();
    }

}
