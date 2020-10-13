import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 名称：自定义订单类
 * 功能：工具类，可以用作获取系统日期、订单编号等
 * 类属性：支付宝公共类
 * 版本：2.0
 * 日期：2008-12-25
 * 作者：支付宝公司销售部技术支持团队
 * 联系：0571-26888888
 * 版权：支付宝公司
 * */
public class UtilDate {
	public  static String getDateNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
	}

	public  static Boolean beforThatDay(Date date, String thatDayStr) throws ParseException {
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		Calendar calendar2 = Calendar.getInstance();
		Date thatDay = sdf.parse(thatDayStr);
		calendar2.setTime(thatDay);
		return calendar1.get(Calendar.YEAR) < calendar2.get(Calendar.YEAR) || calendar1.get(Calendar.DAY_OF_YEAR) < calendar2.get(Calendar.DAY_OF_YEAR);
	}

	public  static Boolean betweenThatDay(Date date, String startDayStr, String endDayStr) throws ParseException {
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		Calendar calendarStart = Calendar.getInstance();
		Date startDay = sdf.parse(startDayStr);
		calendarStart.setTime(startDay);

		Calendar calendarEnd = Calendar.getInstance();
		Date endDay = sdf.parse(endDayStr);
		calendarEnd.setTime(endDay);
		Boolean isBetween = (calendar.get(Calendar.YEAR) > calendarStart.get(Calendar.YEAR)
				&& calendar.get(Calendar.YEAR) <= calendarEnd.get(Calendar.YEAR))
				&&
				(calendar.get(Calendar.DAY_OF_YEAR) > calendarStart.get(Calendar.DAY_OF_YEAR)
				&& calendar.get(Calendar.DAY_OF_YEAR) <= calendarEnd.get(Calendar.DAY_OF_YEAR));
		return isBetween;
	}



	//获取日期，格式：yyyy-MM-dd HH:mm:ss
	public  static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	
	
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	public static String getYMDDate(Date date){
		DateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
		return df.format(date);
	}

	public static boolean isThisToday(Date date) {
		DateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String param = sdf.format(date);//参数时间
		String now = sdf.format(new Date());//当前时间
		if(param.equals(now)){
			return true;
		}
		return false;
	}

	//产生随机的三位数
	public static String getThree(){
		Random rad=new Random();
		return rad.nextInt(1000)+"";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UtilDate date=new UtilDate();
		System.out.println(date.getDateNum());
	}
	
}
