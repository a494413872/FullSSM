import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws ParseException {
        /*String string="1天10点0分";
        String regEx="^\\d+天\\d+点\\d+分$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        System.out.println( m.matches());

        String rev = "\\D";
        Pattern revP = Pattern.compile(rev);
        Matcher revM = revP.matcher(string);
        String s = revM.replaceAll(",");
        String[] split = s.split(",");
        System.out.println(s);

        String numRev="^\\d+$";
        Pattern numP = Pattern.compile(numRev);
        String str="123";
        Matcher mm = numP.matcher(str);
        System.out.println(mm.matches());


        String strss ="abc";
        System.out.println(strss.split("\\|").length);*/

        String startDate = "2021-01-01 00:00:00";
        String endDate ="2021-04-30 00:00:00";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(endDate);
        Date now = new Date();
        if(start.before(now)){
            start=now;
        }
        List<Date> dateList= new ArrayList<>();
        Calendar cstart = Calendar.getInstance();
        cstart.setTime(start);
        Calendar cend = Calendar.getInstance();
        cend.setTime(end);
        do {
            dateList.add(cstart.getTime());
            cstart.add(Calendar.DAY_OF_MONTH,1);
        }while(!cstart.after(cend));
        dateList.add(cend.getTime());
        System.out.println(dateList.size());

    }
}
