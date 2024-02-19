//package edu.ucsd.cse110.successorator.app;
//
//import android.widget.TextView;
//
//import junit.framework.TestCase;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//import static org.mockito.Mockito.mock;
//
//
//@RunWith(RobolectricTestRunner.class)
//@Config(manifest = Config.NONE)
//public class MainActivityTest extends TestCase {
//
//    private MainActivity mainActivity;
//    private TextView dateTextViewMock;
//
//    @Before
//    public void setUp() {
//        // Mock the TextView
//        dateTextViewMock = mock(TextView.class);
//
//        // Initialize MainActivity
//        mainActivity = new MainActivity();
//        mainActivity.dateTextView = dateTextViewMock;
//    }
//
//    @Test
//    public void testDateUpdateAt2AM() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 2);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        // Trigger the date update method manually
//        mainActivity.updateDate();
//
//        // Verify that the dateTextView contains the expected date string
//        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
//        String expectedDate = sdf.format(calendar.getTime());
//        assertEquals(expectedDate, mainActivity.binding.dateTextView.getText().toString());
//    }
//
//}
