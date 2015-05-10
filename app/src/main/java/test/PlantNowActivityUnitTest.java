package test;

import android.content.Intent;
import android.widget.Button;

import com.letitgrow.gardenapp.PlantDetailActivity;
import com.letitgrow.gardenapp.PlantNowActivity;

import java.util.Calendar;

/**
 * Created by Ashley on 5/5/2015.
 */
public class PlantNowActivityUnitTest extends android.test.ActivityUnitTestCase<PlantNowActivity> {

    private int buttonId;
    private PlantNowActivity activity;

    public PlantNowActivityUnitTest() {
        super(PlantNowActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                PlantNowActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    public void testToggle1BtnClickText() {
        String expected;
        buttonId = com.letitgrow.gardenapp.R.id.mainToggleButtonNow;
        assertNotNull(activity.findViewById(buttonId));
        Button view = (Button) activity.findViewById(buttonId);
        String start = (String) view.getText();
        if (start == "FAVS"){ expected = "ALL";}
        else expected = "FAVS";
        //view.performClick();
        //assertEquals("Incorrect label of the button", expected, view.getText());
    }

    public void testInteger() {
        String num = "1";
        boolean result = PlantNowActivity.isInteger(num);
        assertEquals("Result not integer", true, result);
    }

    public void testInteger1() {
        String num = "-1";
        boolean result = PlantNowActivity.isInteger(num);
        assertEquals("Result not integer", true, result);
    }

    public void testInteger2() {
        String num = "0";
        boolean result = PlantNowActivity.isInteger(num);
        assertEquals("Result not integer", true, result);
    }

    public void testInteger3() {
        String num = "5.0";
        boolean result = PlantNowActivity.isInteger(num);
        assertEquals("Result not integer", false, result);
    }

    public void testInteger4() {
        String num = "abc";
        boolean result = PlantNowActivity.isInteger(num);
        assertEquals("Result not integer", false, result);
    }

    public void testInteger5() {
        String num = ":";
        boolean result = PlantNowActivity.isInteger(num);
        assertEquals("Result not integer", false, result);
    }

    public void testDateRange() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,0);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange1() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,0);
        assertEquals("Result not in correct range", true, result);
    }

    public void testDateRange2() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-8);
        assertEquals("Result not in correct range", false, result);
    }


    public void testDateRange3() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-8);
        assertEquals("Result not in correct range", false, result);
    }


    public void testDateRange4() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,8);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange5() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-6);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange6() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,0);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange7() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,0);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange8() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-8);
        assertEquals("Result not in correct range", false, result);
    }


    public void testDateRange9() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-8);
        assertEquals("Result not in correct range", false, result);
    }


    public void testDateRange10() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,8);
        assertEquals("Result not in correct range", true, result);
    }

    public void testDateRange11() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH,0);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-6);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange12() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,0);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange13() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,0);
        assertEquals("Result not in correct range", true, result);
    }

    public void testDateRange14() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-8);
        assertEquals("Result not in correct range", false, result);
    }


    public void testDateRange15() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-8);
        assertEquals("Result not in correct range", false, result);
    }


    public void testDateRange16() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,8);
        assertEquals("Result not in correct range", true, result);
    }

    public void testDateRange17() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,-8,-6);
        assertEquals("Result not in correct range", false, result);
    }
    public void testDateRange18() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 2);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,-8);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange19() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 0);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,-8);
        assertEquals("Result not in correct range", false, result);
    }

    public void testDateRange20() {
        Calendar testDate = Calendar.getInstance();
        Calendar rangeDate = Calendar.getInstance();

        testDate.set(Calendar.YEAR, 2015);
        testDate.set(Calendar.MONTH, 1);  //zero based
        testDate.set(Calendar.DAY_OF_MONTH, 8);

        rangeDate.set(Calendar.YEAR, 2015);
        rangeDate.set(Calendar.MONTH, 1);  //zero based
        rangeDate.set(Calendar.DAY_OF_MONTH, 8);
        boolean result = PlantNowActivity.inRange(testDate,rangeDate,0,-8);
        assertEquals("Result not in correct range", false, result);
    }

}
