package casia.isiteam.zhihu_event.util;

/**
 * 日期权重
 * 依照日期格式确定
 */
public class DateTimeWeight {

	public static int yyyy_mm_dd = 0;

	public static int mm_dd_yyyy = 10;

	public static int yyyy_mm_dd_CN = 50;

	public static int yy_mm_dd = 100;
	
	public static int mm_dd_yy = 200;

	public static int yyyymmdd = 5000;

	public static int yymmdd = 10000;

	public static int mm_dd = 100000;
	
	public static int dd_mm_yyyy=150000;

}
