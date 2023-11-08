import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Utils {

    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date) + ": ";
    }

    public static Timestamp addHours(Timestamp ts, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        cal.add(Calendar.HOUR, hours);
        Timestamp nts = new Timestamp(cal.getTime().getTime());

        return nts;
    }

}
