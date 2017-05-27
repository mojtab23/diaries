import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import javafx.scene.control.DatePicker;
import org.junit.Before;
import org.junit.Test;

import java.time.chrono.HijrahChronology;

/**
 * Created by mojtab23 on 5/23/2017.
 */
public class CalendarTest {


    @Test
    public void testICU() throws Exception {

        ULocale locale = new ULocale("fa_IR@calendar=persian");

        Calendar calendar = Calendar.getInstance(locale);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        System.out.println(df.format(calendar));


    }
}
