package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * dateUtil
 * @author lxc
 * @date 2016/11/20 13:17:43(update)
 */
public class DateUtil {

	public static final long hourMill = 3600l * 1000l;
	public static final long dayMill = 86400l * 1000l;
	public static final long monthMill = 30l * 24l * 3600l * 1000l;

	public static final String timeFmt = "yyyy-MM-dd HH:mm:ss";
	public static final String chinaTimeFmt = "yyyy年MM月dd日 HH:mm:ss";
	public static final String hourFmt = "yyyy-MM-dd HH";
	public static final String dateFmt = "yyyy-MM-dd";
	public static final String chinaDateFmt = "yyyy年MM月dd日";

	/**
	 * 返回星期几
	 * 
	 * @param date
	 * @return 0~6
	 */
	public static int getWeek() {
		return getWeek(new Date());
	}

	/**
	 * 返回星期几
	 * 
	 * @param date
	 * @return 0~6
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int ret = cal.get(Calendar.DAY_OF_WEEK);
		return ret - 1;
	}

	/**
	 * 返回星期几
	 * 
	 * @param date
	 * @return 1~7
	 */
	public static int getChinaWeek() {
		return getChinaWeek(new Date());
	}

	/**
	 * 返回星期几
	 * 
	 * @param date
	 * @return 1~7
	 */
	public static int getChinaWeek(Date date) {
		int week = getWeek(date);
		return week == 0 ? 7 : week;
	}

	/**
	 * 周一时间
	 * 
	 * @return date
	 */
	public static Date getMonday() {
		Date date = getToday();
		int week = getChinaWeek(date) - 1;
		if (week > 0)
			date.setTime(date.getTime() - week * DateUtil.dayMill);
		return date;
	}

	/**
	 * 同一个星期
	 */
	public static boolean isSameWeek(Date d1, Date d2) {
		return getMonday(d1).equals(getMonday(d2));
	}

	/**
	 * 同一个星期
	 */
	public static boolean isSameWeek(Date d1) {
		return getMonday(d1).equals(getMonday(new Date()));
	}

	/**
	 * 同一天
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		SimpleDateFormat sdf = createFormat(dateFmt);
		return sdf.format(d1).equals(sdf.format(d2));
	}

	/**
	 * 同一小时
	 */
	public static boolean isSameHour(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return format(d1, hourFmt).equals(format(d2, hourFmt));
	}

	/**
	 * 同一小时
	 */
	public static boolean isSameHour(Date d1) {
		return isSameHour(d1, new Date());
	}

	public static Date getMonday(Date date) {
		date = DateUtil.onlyDay(date);
		int week = getChinaWeek(date) - 1;
		if (week > 0)
			date.setTime(date.getTime() - week * DateUtil.dayMill);
		return date;
	}

	private static SimpleDateFormat createFormat(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		return sdf;
	}

	/**
	 * 判断时间是否过去 如果为null 返回true
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isPast(Date date) {
		if (date == null)
			return true;
		return System.currentTimeMillis() > date.getTime();
	}

	/**
	 * 判断日期是否是当天日期 如果为null 返回true
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		if (date == null)
			return false;
		return isSameDay(new Date(), date);
	}

	/**
	 * 仅仅取date的日期部分，略掉时间部分
	 * 
	 * @param date
	 * @return
	 */
	public static Date onlyDay(Date date) {
		if (date == null)
			return null;
		return parse(format(date));
	}

	/**
	 * 字符串转日期 yyyy-MM-dd
	 * 
	 * @param day
	 * @return
	 */
	public static Date parse(String day) {
		try {
			return createFormat(dateFmt).parse(day);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 字符串转成日期
	 * 
	 * @param day
	 * @return
	 */
	public static Date parse(String day, String fmt) {
		if (day == null || fmt == null)
			return null;
		try {
			return createFormat(fmt).parse(day);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日期转成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date, String fmt) {
		return createFormat(fmt).format(date);
	}

	/**
	 * 日期转成字符串 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return createFormat(dateFmt).format(date);
	}

	/**
	 * 如果同一天，返回cnt，否则，返回notCnt
	 * 
	 * @param date
	 * @param cnt
	 * @param notCnt
	 * @return
	 */
	public static int isTodayCnt(Date date, int cnt, int notCnt) {
		if (isToday(date)) {
			return cnt;
		}
		return notCnt;
	}

	/**
	 * 如果同一天，返回cnt，否则，返回notCnt
	 * 
	 * @param date
	 * @param cnt
	 * @param notCnt
	 * @return
	 */
	public static int isTodayCnt(Date date, int cnt) {
		return isTodayCnt(date, cnt, 0);
	}

	/**
	 * x毫秒数后的时间
	 * 
	 * @param time
	 * @return
	 */
	public static Date afterTime(long time) {
		Date d = new Date(System.currentTimeMillis() + time);
		return d;
	}

	/**
	 * 比较时间
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean before(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return d1.getTime() < d2.getTime();
	}

	public static boolean after(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return d1.getTime() > d2.getTime();
	}

	/**
	 * 判断一个日期是否在2个日期之间
	 * 
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean between(Date date, Date startDate, Date endDate) {
		boolean isBefore = true;
		boolean isAfter = true;
		if (startDate != null)
			isBefore = startDate.before(date);
		if (endDate != null)
			isAfter = endDate.after(date);
		return isBefore && isAfter;
	}

	/**
	 * 计算小时差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long calHour(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return 0;
		long time = d2.getTime() - d1.getTime();
		long hour = time / hourMill;
		if (hour * hourMill < time) {
			hour++;
		}
		return hour;
	}

	/**
	 * 计算时间差(小时)
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long calcIntervalHour(Date d) {
		return calHour(new Date(), d);
	}

	/**
	 * 计算时间差(天)
	 * 
	 * @param d
	 * @return
	 */
	public static Integer calcIntervalDay(Date d) {
		if (d == null)
			return 0;
		Long diff = new Date().getTime() - d.getTime();
		Long diffDay = diff / (dayMill);
		return diffDay.intValue();
	}

	/**
	 * 计算时间差(天)
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Integer calcIntervalDay(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return null;
		return (int) ((d2.getTime() - d1.getTime()) / dayMill);
	}

	/**
	 * 计算日期(往前或往后 ) eg : calcDay(-1) 取昨天的日期 eg : calcDay(1) 取明天的日期
	 * 
	 * @param day
	 * @return
	 */
	public static Date calcDay(Integer day) {
		Date d = new Date();
		Long dt = d.getTime() + day.longValue() * 86400L * 1000L;
		d.setTime(dt);
		return DateUtil.onlyDay(d);
	}

	/**
	 * 计算时间差（和当前时间计算）(毫秒)
	 * 
	 * @param d
	 * @return
	 */
	public static Long diffMill(Date d) {
		if (d == null)
			return null;
		Date nowDate = new Date();
		Long diffMill = nowDate.getTime() - d.getTime();
		return diffMill;
	}

	/**
	 * 今天零点
	 * 
	 * @return
	 */
	public static Date getToday() {
		return today();
	}

	/**
	 * 今天零点
	 * 
	 * @return
	 */
	public static Date today() {
		return onlyDay(new Date());
	}

	/**
	 * 昨天这个时候
	 * 
	 * @return
	 */
	public static Date getYestoday() {
		Date d = getToday();
		return new Date(d.getTime() - dayMill);
	}

	/**
	 * 几天前
	 * 
	 * @return
	 */
	public static Date beforeDay(int n) {
		Date d = getToday();
		return new Date(d.getTime() - n * dayMill);
	}

	/**
	 * 几天后
	 * 
	 * @return
	 */
	public static Date afterDay(int n) {
		Date d = getToday();
		return new Date(d.getTime() + n * dayMill);
	}

	/**
	 * 这个月初零点的时间
	 * 
	 * @return
	 */
	public static Date getMonthDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
		return calendar.getTime();
	}
}
