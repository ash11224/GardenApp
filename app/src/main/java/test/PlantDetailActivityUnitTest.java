package test;

import android.content.Intent;
import android.widget.Button;
import android.widget.ToggleButton;

import com.letitgrow.gardenapp.PlantDetailActivity;
import com.letitgrow.gardenapp.R;

/**
 * Created by Ashley on 5/4/2015.
 */
public class PlantDetailActivityUnitTest extends android.test.ActivityUnitTestCase<PlantDetailActivity> {

        private int buttonId;
        private PlantDetailActivity activity;

        public PlantDetailActivityUnitTest() {
            super(PlantDetailActivity.class);
        }

        @Override
        protected void setUp() throws Exception {
            super.setUp();
            Intent intent = new Intent(getInstrumentation().getTargetContext(),
                    PlantDetailActivity.class);
            startActivity(intent, null, null);
            activity = getActivity();
        }

        public void testToggle2BtnClickText() {
            String expected;
            buttonId = com.letitgrow.gardenapp.R.id.toggleButton;
            assertNotNull(activity.findViewById(buttonId));
            ToggleButton view1 = (ToggleButton) activity.findViewById(buttonId);
            String start = (String) view1.getText();
            if (start == "NO") {
                expected = "YES";
            } else expected = "NO";
         //   view1.performClick();
         //   assertEquals("Incorrect label of the button", expected, view1.getText());
        }

    public void testNumeric() {
        String num = "5";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", true, result);
    }

    public void testNumeric1() {
        String num = "0";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", true, result);
    }

    public void testNumeric2() {
        String num = "7.5";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", true, result);
    }

    public void testNumeric3() {
        String num = "-100";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", true, result);
    }

    public void testNumeric4() {
        String num = "-0.0004";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", true, result);
    }

    public void testNumeric5() {
        String num = "-0";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", true, result);
    }

    public void testNumeric6() {
        String num = "abc";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", false, result);
    }

    public void testNumeric7() {
        String num = "1abc";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", false, result);
    }
    public void testNumeric8() {
        String num = "-abc";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", false, result);
    }
    public void testNumeric9() {
        String num = "ab.c";
        boolean result = PlantDetailActivity.isNumeric(num);
        assertEquals("Result not number", false, result);
    }

    public void testInteger() {
        String num = "1";
        boolean result = PlantDetailActivity.isInteger(num);
        assertEquals("Result not integer", true, result);
    }

    public void testInteger1() {
        String num = "-1";
        boolean result = PlantDetailActivity.isInteger(num);
        assertEquals("Result not integer", true, result);
    }

    public void testInteger2() {
        String num = "0";
        boolean result = PlantDetailActivity.isInteger(num);
        assertEquals("Result not integer", true, result);
    }

    public void testInteger3() {
        String num = "5.0";
        boolean result = PlantDetailActivity.isInteger(num);
        assertEquals("Result not integer", false, result);
    }

    public void testInteger4() {
        String num = "abc";
        boolean result = PlantDetailActivity.isInteger(num);
        assertEquals("Result not integer", false, result);
    }

    public void testInteger5() {
        String num = ":";
        boolean result = PlantDetailActivity.isInteger(num);
        assertEquals("Result not integer", false, result);
    }
}

