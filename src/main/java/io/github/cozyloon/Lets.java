package io.github.cozyloon;

/***************************************************************************************
 *    Title: <Lets Core>
 *    Author: <Chathumal Sangeeth>
 *    Date: <4/1/2023>
 *    Code version: <4.1.3>
 ***************************************************************************************/

import org.openqa.selenium.*;
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

    public static String getProperty(String key) {
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

    public static void launchDriver(String browserType) {
        if (browserType.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
            driver.manage().window().maximize();
        }
    }

    public static void launchDriverWithHeadlessMode(String browserType) {
        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions co = new ChromeOptions();
            co.addArguments("--headless=new");
            driver = new ChromeDriver(co);
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions fo = new FirefoxOptions();
            fo.addArguments("--headless=new");
            driver = new FirefoxDriver(fo);
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
            chromeOptions.addArguments("--headless=new");
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), chromeOptions);
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--headless=new");
            driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), firefoxOptions);
            driver.manage().window().maximize();
        } else if (browserType.equalsIgnoreCase("edge")) {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--headless=new");
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

    public static void javaScriptSend(By locator, String value) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + value + "';", element);
    }

    public static void javaScriptBrowserWindowRefresh() {
        ((JavascriptExecutor) driver).executeScript("history.go(0)");
    }

    public static void javaScriptCheckBoxHandle(By locator, boolean value) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked=" + value + ";", element);
    }

    public static void javaScriptGenerateAlertPop(String msg) {
        ((JavascriptExecutor) driver).executeScript("alert('" + msg + "');");
    }

    public static String javaScriptGetWebpageTitle() {
        return ((JavascriptExecutor) driver).executeScript("return document.title;").toString();
    }

    public static String javaScriptGetDomainName() {
        return ((JavascriptExecutor) driver).executeScript("return document.domain;").toString();
    }

    public static String javaScriptGetWebpageURL() {
        return ((JavascriptExecutor) driver).executeScript("return document.URL;").toString();
    }

    public static String javaScriptGetWebpageHeight() {
        return ((JavascriptExecutor) driver).executeScript("return window.innerHeight;").toString();
    }

    public static String javaScriptGetWebpageWidth() {
        return ((JavascriptExecutor) driver).executeScript("return window.innerWidth;").toString();
    }

    public static void javaScriptPageScrollVertical(int px) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + px + ")");
    }

    public static void javaScriptPageScrollVerticalToWebpageEND() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public static void openANewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    public static void openANewTabWithinWindow() {
        driver.switchTo().newWindow(WindowType.TAB);
    }

    public static void clickAndHold(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Actions actions = new Actions(driver);
        actions.clickAndHold(element).build().perform();
    }

    public static void releasePressedMouseButton(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        Actions actions = new Actions(driver);
        actions.release(element).build().perform();
    }
}
