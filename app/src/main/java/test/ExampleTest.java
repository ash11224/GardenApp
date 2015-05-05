package test;

import android.test.InstrumentationTestCase;

/**
 * Created by Ashley on 5/4/2015.
 */
public class ExampleTest extends InstrumentationTestCase{
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
