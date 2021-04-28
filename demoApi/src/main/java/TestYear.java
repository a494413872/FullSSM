import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestYear {
    public static void main(String[] args) {
        Calendar now = Calendar.getInstance();
        List<Date> dateList= new ArrayList<>();
        for (int i = 1; i < 366; i++) {
            Date theday = now.getTime();
            dateList.add(theday);
            now.add(Calendar.DAY_OF_MONTH,1);
        }
        System.out.println("sdfsd");
    }
}
