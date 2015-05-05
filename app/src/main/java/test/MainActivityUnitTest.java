package test;

import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;

import com.letitgrow.gardenapp.MainActivity;

/**
 * Created by Ashley on 5/4/2015.
 */
public class MainActivityUnitTest extends android.test.ActivityUnitTestCase<MainActivity> {

    private int buttonId;
    private MainActivity activity;

    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                MainActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    public void testToggleBtnText() {
        buttonId = com.letitgrow.gardenapp.R.id.mainToggleButton;
        assertNotNull(activity.findViewById(buttonId));
        Button view = (Button) activity.findViewById(buttonId);
        assertEquals("Incorrect label of the button", "ALL", view.getText());
    }

    public void testToggleBtnClickText() {
        buttonId = com.letitgrow.gardenapp.R.id.mainToggleButton;
        assertNotNull(activity.findViewById(buttonId));
        Button view = (Button) activity.findViewById(buttonId);
        view.performClick();
        assertEquals("Incorrect label of the button", "FAVS", view.getText());
    }

    public void testNowBtnText() {
        buttonId = com.letitgrow.gardenapp.R.id.NowButton;
        assertNotNull(activity.findViewById(buttonId));
        Button view = (Button) activity.findViewById(buttonId);
        assertEquals("Incorrect label of the button", "PLANT NOW", view.getText());
    }

    public void testNOWIntent1TriggerViaOnClick() {
        buttonId = com.letitgrow.gardenapp.R.id.NowButton;
        Button view = (Button) activity.findViewById(buttonId);
        assertNotNull("Button not allowed to be null", view);

        view.performClick();

        // Check the intent which was started
        Intent triggeredIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", triggeredIntent);
        Boolean data = triggeredIntent.getExtras().getBoolean("favPushed");

        assertEquals((boolean) false, (boolean) data);
    }



}