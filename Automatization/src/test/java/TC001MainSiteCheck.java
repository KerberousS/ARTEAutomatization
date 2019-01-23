package test.java;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.Assert;
import org.testng.annotations.Test;

import configs.GlobalSettings;

import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.comparison.ImageMarkupPolicy;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class TC001MainSiteCheck extends GlobalSettings {

    //Create personalized output folder strings for TC, TODO rework this later
    String NazwaTC = this.getClass().getName();
    String ScreenShotFileOutput = TestOutputFolder + ScreenShotsFolder + DayAndTime + "TEST" + NazwaTC + FormatScreenshot;
    String ComparisonImage =  TestOutputFolder + ScreenShotsFolder + "FullMainSite" + FormatScreenshot;
	public static Logger log = Logger.getLogger(TC001MainSiteCheck.class.getName());
    int maxPixelDiff=35000;

    @BeforeMethod
	  public void beforeMethod() throws Exception {
        //Load page
         driver.get(StronaGlowna);
         //Restart page so assets like animations are downloaded
         Thread.sleep(1000);
         driver.get(StronaGlowna);
     }

	 @Test
		public void GlownaStrona()throws  Exception{
//         File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//         FileUtils.copyFile(scrFile, new File(ScreenShotFileOutput));
         //Take screenshoot of webiste
         Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
//
//         ImageIO.write(screenshot.getImage(), "PNG", new File(ScreenShotFileOutput));

         //Get actual and expected image
         BufferedImage actualImage = screenshot.getImage();
         BufferedImage expectedImage = ImageIO.read(new File(ComparisonImage));

         //Create orangeColored differ, there is no orange on site
         ImageDiffer orangeDiffer = new ImageDiffer()
                 .withColorDistortion(10)
                 .withDiffMarkupPolicy(
                         new ImageMarkupPolicy()
                                 .withDiffColor(Color.ORANGE)
                 );

         //Use differ on images
         ImageDiff diff = orangeDiffer.makeDiff(actualImage, expectedImage);

         //Get Image and write it to file
         BufferedImage diffImage = diff.getMarkedImage();
         ImageIO.write(diffImage, "PNG", new File(ScreenShotFileOutput));

         //Log color and pixel info
         log.info("Color model is" + diffImage.getColorModel());
                 log.info("Different pixels size is " + diff.getDiffSize());
                 int diffSize=diff.getDiffSize();

                 //Check if theres enough of a difference to expect a bug on the page
         if (maxPixelDiff>=diffSize)
         {
             Reporter.log("TC dla " + StronaGlowna + " Completed, różcnica w pixelach to " + diffSize + ", maksymalna ustalona różnica dla strony to: " + maxPixelDiff);
         }
             else {
                 int pixelSizeDiffOnFail = diffSize-maxPixelDiff;
            Assert.fail("TC dla " + StronaGlowna + " zakonczyl sie niepowodzeniem, roznica miedzy maksymalnem progiem a wynikiem to " + pixelSizeDiffOnFail + " pikseli");
         }
		 Reporter.log("'Główna strona została sprawdzona");

	 }
		@AfterMethod
		public void afterMethod() throws Exception {
        //Close selenium driver
		driver.close();
		}
}