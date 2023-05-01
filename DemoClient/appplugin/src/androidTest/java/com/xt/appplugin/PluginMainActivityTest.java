package com.xt.appplugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PluginMainActivityTest {


    @Test
    public void testRun() {
        PluginMainActivity sampleActivity = Robolectric.buildActivity(PluginMainActivity.class).
                create().resume().get();
        assertNotNull(sampleActivity);
        assertEquals("Activity的标题", sampleActivity.getTitle());
    }

}
