package configs;

import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class Listeners extends GlobalSettings implements ITestListener, ISuiteListener {

    //Add statics for ExtentReports
    private static ExtentReports extent = ExtentManager.createInstance("extent.html");
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal();

    //ISuiteListener method - will execute BEFORE the START of the Suite
    @Override
    public synchronized void onStart(ISuite arg0) {
        Reporter.log("----------- " + "Beggining Suite " + arg0.getName() + " -----------", true);
    }

    //ISuiteListener method - will execute AFTER FINISHING the Suite
    @Override
    public synchronized void onFinish(ISuite arg0) {
        Reporter.log("----------- " + "Suite " + arg0.getName() + " has been executed" + " -----------", true);
    }

    //ITestListener method - Will execute on START of TC LEVEL
    public synchronized void onStart(ITestContext arg0) {
        Reporter.log(":::::::: " + "The execution of level " + arg0.getName() + " has started." + " ::::::::", true);
        ExtentTest parent = extent.createTest(arg0.getName());
        parentTest.set(parent);
    }

    //ITestListener method - Will execute on END of TC LEVEL
    public synchronized void onFinish(ITestContext arg0) {
        Reporter.log(":::::::: " + "The execution of level " + arg0.getName() + "has been completed." + " ::::::::", true);
        extent.flush();
    }

    //ITestListener method - Will Execute when TEST Class is SUCCESS
    public synchronized void onTestSuccess(ITestResult arg0) {
        String NazwaTC = arg0.getInstanceName();
        String ScreenShotFileOutput = TestOutputFolder + ScreenShotsFolder + DayAndTime + "TEST" + NazwaTC + FormatScreenshot;
        System.out.println(ScreenShotFileOutput);

        //Catch Screenshot of difference and add it to site
        try {
            test.get().fail(arg0.getTestName(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotFileOutput).build());
        } catch (IOException e) {
            Reporter.log("Cannot save Screenshot file, check StackTrace.");
            e.printStackTrace();
        }

        // This is calling the printTestResults method
        printTestResults(arg0);
    }

    //ITestListener method - Will Execute when TEST Class FAILED
    public synchronized void onTestFailure(ITestResult arg0) {

        String NazwaTC = arg0.getInstanceName();
        String ScreenShotFileOutput = TestOutputFolder + ScreenShotsFolder + DayAndTime + "TEST" + NazwaTC + FormatScreenshot;
        System.out.println(ScreenShotFileOutput);

        //Catch Screenshot of difference and add it to site
        try {
            test.get().fail(arg0.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotFileOutput).build());
        } catch (IOException e) {
            Reporter.log("Cannot save Screenshot file, check StackTrace.");
            e.printStackTrace();
        }

        // This is calling the printTestResults method
        printTestResults(arg0);
    }

    // This belongs to ITestListener and will execute before the main test start (@Test)
    public synchronized void onTestStart(ITestResult arg0) {
        System.out.println("........ " + "Beggining Execution of the Main test " + arg0.getInstanceName() + " ........");
        ExtentTest child = parentTest.get().createNode(arg0.getMethod().getMethodName());
        test.set(child);
    }

    // This belongs to ITestListener and will execute only if any of the main test(@Test) get skipped
    public synchronized void onTestSkipped(ITestResult arg0) {
        System.out.println("!!!!!!! " + "TestCase " + arg0.getInstanceName() + "was skipped, please check configuration" + " !!!!!!!");
        printTestResults(arg0);
        test.get().skip(arg0.getThrowable());
    }

    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
    }
    // This is the method which will be executed in case of test pass or fail


    // This will provide the information on the test itself
    private void printTestResults(ITestResult result) {
        Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);
        if (result.getParameters().length != 0) {
            String params = null;
            for (Object parameter : result.getParameters()) {
                params += parameter.toString() + ",";
            }
            Reporter.log("Test Method had the following parameters : " + params, true);
        }
        String status = null;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "Passed";
                break;
            case ITestResult.FAILURE:
                status = "Failed";
                break;
            case ITestResult.SKIP:
                status = "Skipped";
        }
        Reporter.log("Test Status: " + status, true);
    }
	/*
	// This belongs to IInvokedMethodListener and will execute before every method including @Before @After @Test
	public synchronized void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		String textMsg = "About to begin executing following method: " + returnMethodName(arg0.getTestMethod());
		Reporter.log(textMsg, true);
	}

	// This belongs to IInvokedMethodListener and will execute after every method including @Before @After @Test
	public synchronized void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
		String textMsg = "Completed executing following method: " + returnMethodName(arg0.getTestMethod());
		Reporter.log(textMsg, true);
	}

	// This will return method names to the calling function
	private String returnMethodName(ITestNGMethod method) {
		return method.getRealClass().getSimpleName() + "." + method.getMethodName();
	}
	*/
}