package casia.isiteam.zhihu_event.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 日期时间类
 * 用于解析一般日期时间、含英文月份的日期时间以及其他语言，如俄语，的日期时间
 * 支持对“5分钟前”格式的解析、含全角数字和中文数字的时间的解析
 * @author pengx
 * Nov 29, 2010
 * @version
 * @since
 */
public class TimeUtil {
	private static Logger logger = Logger.getLogger(TimeUtil.class);

	private String dateTimeStr = null;
	
	private  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private  SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private  SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 获取当前时间
	 * 
	 * @return  String
	 */
	public static String getCurrentTime(){
		Calendar dateTime = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String curTime = null;
		
		try {
			curTime = sdf.format(dateTime.getTime());
		} catch (Exception e) {
			logger.error("get current time error!", e);
		}
		return curTime;
	}
	
	public  Date parseDate(String dateTime){
		
		Date date = null;
		try {
			date = sdf1.parse(dateTime);
		} catch (ParseException e) {
			try {
				date = sdf2.parse(dateTime);
			} catch (ParseException e2) {
				try {
					date = sdf3.parse(dateTime);
				} catch (ParseException e3) {
					
				}
			}
		}
		
		return date;
	}
	
	/**
	 * 将给定的时间转换为Timestamp格式
	 * 
	 * @param dateTime 格式为yyyy-MM-dd HH:mm:ss.SSS
	 * @return  Timestamp
	 */
	public static Timestamp parseTimestamp(String dateTime){
		if(dateTime == null){
			return null;
		}
		if(!dateTime.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}")){
			logger.warn("dateTime is not match the format \"yyyy-MM-dd HH:mm:ss.SSS\"!");
			return null;
		}
		Timestamp timestamp = null;
		
		try {
			timestamp = Timestamp.valueOf(dateTime);
		} catch (Exception e) {
			logger.error("parser timestamp error!", e);
		}
		return timestamp;
	}
	
	/**
	 * 从给定的字符串中解析日期和时间
	 * 可解析不同地区、时区日期时间
	 * 可自定义解析的正则表达式和格式字符串
	 * 
	 * @param str  包含日期的字符串
	 * @param local 区域代码，一般中英文时间可不传该参数，其他区域目前仅支持俄罗斯时间
	 * @param timezone 给定的时区，格式类似“+8”——东8区、“-4”——西4区
	 * @param dateRegex 自定义解析的正则表达式，需要和dateFormat同时使用
	 * @param dateFormat 自定义解析的格式字符串，需要和dateRegex同时使用
	 * @return  String 解析得到的日期时间
	 */
	public String parseDateTime(String str, String local, String timezone, String dateRegex, String dateFormat) {
		
		if(str == null)
			return null;
		
		if(str != null){
			str = str.replaceAll("[\\s  ]{2,}", " ");
		}
		str = str.trim();
		String dateTime = null;
		Vector<WeightedDate> v = new Vector<WeightedDate>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(local != null && local.equalsIgnoreCase("RU")){//处理俄文日期和时间
			this.dateTimeStr = this.replaceRussian(str.replace("\n", ""));
		}
		else {
			this.dateTimeStr = str.replace("\n", "");
		}
		
		if(dateRegex != null && !"".equals(dateRegex.trim()) && dateFormat != null){
			//使用自定义的匹配表达式和格式字符串解析日期和时间
			try {
				Pattern pattern = Pattern.compile(dateRegex);
				Matcher matcher = pattern.matcher(this.dateTimeStr);
				if (matcher.find()) {
					SimpleDateFormat sdf_pri = new SimpleDateFormat(dateFormat);
					dateTime = sdf.format(sdf_pri.parse(matcher.group()));
					
					if(this.checkDateTime(dateTime)){
						logger.debug("parser \"" + str.replaceAll("^\\s+", "").trim() + "\" to \"" + dateTime + "\"!");
						return this.convertTimeZone(dateTime, timezone);
					}
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
		//处理特殊格式日期和时间，格式类似“10分钟前”
		dateTime = this.parserCharacter();
		if(dateTime != null && this.checkDateTime(dateTime)){
			logger.debug("parser \"" + str.replaceAll("^\\s+", "").trim() + "\" to \"" + dateTime + "\"!");
			return this.convertTimeZone(dateTime, timezone);
		}
		
		//解析时间
		String time = this.getTime(this.dateTimeStr);
		if(time == null){
			time = "00:00";
		}
		
		//解析日期
		WeightedDate[] wds = new WeightedDate[7];
		wds[0] = this.getHtmlDateL1();
		wds[1] = this.getHtmlDateChL1();
		wds[2] = this.getHtmlDateEnL1();
		for (int i = 0; i < 3; i++) {
			if (wds[i] != null) {
				v.add(wds[i]);
			}
		}
		if (v.size() > 0) {
			dateTime = this.getBest(v).date + " " + time;
			logger.debug("parser \"" + str.replaceAll("^\\s+", "").trim() + "\" to \"" + dateTime + "\"!");
			return this.convertTimeZone(dateTime, timezone);
		}

		wds[3] = this.getHtmlDateL2();
		wds[4] = this.getHtmlDateChL2();
		wds[5] = this.getHtmlDateL3();
		wds[6] = this.getHtmlDateChL3();
		for (int i = 3; i < 7; i++) {
			if (wds[i] != null) {
				v.add(wds[i]);
			}
		}
		if (v.size() > 0) {
			dateTime = this.getBest(v).date + " " + time;
			logger.debug("parser \"" + str.replaceAll("^\\s+", "").trim() + "\" to \"" + dateTime + "\"!");
			return this.convertTimeZone(dateTime, timezone);
		}
		
		if(time != null){//如果只匹配到时间，则默认日期为当前日期
			dateTime = getCurrentTime().split("\\s+")[0] + " "  + time;
			if(this.checkDateTime(dateTime) == true){
				logger.debug("parser \"" + str.replaceAll("^\\s+", "").trim() + "\" to \"" + dateTime + "\"!");
				return this.convertTimeZone(dateTime, timezone);	
			}
		}
		
		logger.warn("can't parser time! the string is \"" + str + "\", set datetime to '2000-01-01 01:01:01'");
		return null;
	}
	
	/**
	 * 将时间格式化为yyyy-MM-dd HH:mm:ss
	 * @param dateTime
	 * @return
	 */
	public String formatDateTime(String dateTime){
		if(dateTime == null){
			return null;
		}
		String[] dateAry = dateTime.split("[\\s+\\-:]");
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < dateAry.length; i++){
			if(i == 0){
				if(dateAry[i].matches("\\d{2}")){
					builder.append("20" + dateAry[i]);
				} else{
					builder.append(dateAry[i]);
				}
			} else if(dateAry[i].matches("\\d")){
				builder.append("0" + dateAry[i]);
			} else{
				builder.append(dateAry[i]);
			}
			if(i < 2){
				builder.append("-");
			} else if(i == 2){
				builder.append(" ");
			} else{
				builder.append(":");
			}
		}
		if(dateAry.length == 5){
			builder.append("00");
		} else{
			builder.delete(builder.length() - 1, builder.length());
		}
		
		return builder.toString();
	}
	
	/**
	 * 检查时间是否在合理的范围内，即在1980年1月1日后，没有超过当前时间
	 * 主要用于验证信息的发布时间是否解析正确
	 * @param dateTime
	 * @return  boolean
	 */
	public boolean checkDateTime(String dateTime){
		boolean isNormal = false;
		if(dateTime == null){
			return false;
		}
		
		dateTime = dateTime.trim();
		if(dateTime.length() == 10){
			dateTime += " 00:00:00";
		} else if(dateTime.matches("\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}:\\d{2}")){
			dateTime += ":00";
		}
		
		Calendar date = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date.setTime(sdf.parse(dateTime));
			if(dateTime.compareTo("1980-01-01") > 0 && Calendar.getInstance().after(date)){
				isNormal = true;
			}
		} catch (Exception e) {
			logger.error("check date \"" + dateTime + "\" exception!",e );
		}
		
		return isNormal;
	}
	
	/**
	 * 按照设置的时区将时间转换成北京时间
	 * 
	 * @param dateTime 需要转换的时间
	 * @param timezone 配置的时区
	 * @return  String 转换后的北京时间
	 */
	private String convertTimeZone(String dateTime, String timezone){
		if(timezone == null || "".equals(timezone)){
			timezone = "+8";
		}
		
		if(dateTime.trim().length() <= 16){
			dateTime = dateTime.trim() + ":00";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String normalTime = null;
		try {
			calendar.setTime(sdf.parse(dateTime.trim()));
			calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(timezone.replace("+", "")) - 8);
			normalTime = sdf.format(calendar.getTime());
			if(this.checkDateTime(normalTime) == false){
				normalTime = dateTime;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error while parser datetime of \"" + dateTime + "\"\n" + e.toString());
		}
		
		return dateTime;
	}

	/**
	 * 提取字符串中的时间——时分秒
	 * @param str 包含时间的字符串
	 * @return  String 解析得到的时间
	 */
	public String getTime(String str){
		String time = null;
		String timeRegexAll = "([\\s  　 日]*|^)((?:[01]?[0-9]|2[0-4])[\\:时時](?:[0-5]?[0-9])(?:[\\:分][0-5]?[0-9]秒?)?(\\s?[aApP][mM])?)";
		Pattern timePatternALL = Pattern.compile(timeRegexAll);
		Matcher matcher = null;
		try {
			matcher = timePatternALL.matcher(str.replace("下午", "pm"));
			StringBuffer sb = new StringBuffer();
			if (matcher.find()) {
				time = matcher.group(2);
				if (matcher.group(3) != null && matcher.group(3).trim().equalsIgnoreCase("pm")) {
					int newHourOfTime = Integer.parseInt(time.split("\\:", 2)[0]) + 12;
					if (newHourOfTime < 24) {
						time = newHourOfTime + ":" + time.split("\\:", 2)[1];
					}
				}
				matcher.appendReplacement(sb, matcher.group(1));
			}
			if (time != null) {
				time = time.replaceAll("[aApP][mM]$", "").replaceAll("[时分秒時]", ":");
				if(time.endsWith(":")){
					time = time.substring(0, time.length() - 1);
				}
			}
			matcher.appendTail(sb);
			str = sb.toString();
			this.dateTimeStr = str;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("parse time exception !", e);
		}
		
		return time;
	}
	
	/**
	 * 处理类似“10分钟前”这种格式的时间
	 * @return  String
	 */
	private String parserCharacter(){
		String dateTime = null;
		boolean isFind = false;
		Calendar today = Calendar.getInstance();
		
		Pattern characterPattern = Pattern.compile("(\\d+|半)[\\s　  ]*(秒|seconds?|分钟?|分鐘|minutes?|小时|小時|hours?)(前|\\s+ago)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			matcher = characterPattern.matcher(this.dateTimeStr);
			if (matcher.find()) {
				String pre1 = matcher.group(1);
				String pre2 = matcher.group(2);

				int addHour = 0;
				int addMinute = 0;
				int addSecond = 0;
				if (pre2 != null && pre2.matches("(?i)小時|小时|hours?")) {
					if (pre1.equals("半")) {
						addMinute = -30;
					} else {
						addHour = 0 - Integer.parseInt(pre1);
					}
				} else if (pre2 != null && pre2.matches("(?i)分钟?|分鐘|minutes?")) {
					addMinute = 0 - Integer.parseInt(pre1);
				} else if (pre2 != null && pre2.matches("(?i)秒|seconds?")) {
					addSecond = 0 - Integer.parseInt(pre1);
				}

				today.add(Calendar.HOUR, addHour);
				today.add(Calendar.MINUTE, addMinute);
				today.add(Calendar.SECOND, addSecond);
				
				dateTime = sdf.format(today.getTime());
				isFind = true;
			}
			
			if(isFind == false){
				characterPattern = Pattern.compile("(今天|昨天|前天|today|yesterday)[\\s,　  ]*(([01]?[0-9]|2[0-3]):[0-5][0-9])", Pattern.CASE_INSENSITIVE);
				matcher = characterPattern.matcher(this.dateTimeStr);
				if(matcher.find()){
					String pre = matcher.group(1);
					String time = this.getTime(this.dateTimeStr);

					int addDay = 0;
					if(pre != null && pre.matches("(?i)今天|today")){
						addDay = 0;
					} else if(pre != null && pre.matches("(?i)昨天|yesterday")){
						addDay = -1;
					} else if(pre != null && pre.matches("(?i)前天")){
						addDay = -2;
					} else{
						logger.warn("havn't parser condition \"" + pre + "\"!");
					}
					today.add(Calendar.DAY_OF_MONTH, addDay);
					dateTime = sdf.format(today.getTime()).split("\\s+")[0] + " " + time;
					isFind = true;
				}
			}
			

			if(isFind == false){
				characterPattern = Pattern.compile("(\\d+|半)[\\s　  ]*(周|weeks?|天|days?)(前|\\s+ago)", Pattern.CASE_INSENSITIVE);
				matcher = characterPattern.matcher(this.dateTimeStr);
				if(matcher.find()){
					String pre1 = matcher.group(1);
					String pre2 = matcher.group(2);
					String time = this.getTime(this.dateTimeStr);

					int addDay = 0;
					int addHour = 0;
					if(pre2 != null && pre2.matches("(?i)周|weeks?")){
						addDay = 0 - Integer.parseInt(pre1) * 7;
					}
					else if (pre1 != null && pre1.matches("(?i)半")) {
						addHour = -12;
					} else if (pre1 != null && pre1.matches("\\d+")) {
						addDay = 0 - Integer.parseInt(pre1);
					} 

					today.add(Calendar.DAY_OF_MONTH, addDay);
					today.add(Calendar.HOUR, addHour);
					
					if(time == null){
						time = "00:00";
					}
					dateTime = sdf.format(today.getTime()).split("\\s+")[0] + " " + time;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("parser character datetime exception :" + this.dateTimeStr);
		}

		return dateTime;
	}
	
	/**
	 * 私有类，用于计算简单的日期权重，权重越小越标准
	 * 
	 * @author pengx
	 * Nov 29, 2010
	 * @version
	 * @since
	 */
	private class WeightedDate {
		int weight = Integer.MAX_VALUE;

		String date = null;

		void setWeight(int offset, int weight) {
			this.weight = offset + weight;
		}

		void setDate(String yyyy, int mm, int dd) {
			if (mm <= 0 || dd <= 0 || mm > 12 || dd > 31) {
				return;
			}

			if(yyyy.length() == 2 && (yyyy.charAt(0) == '0' || yyyy.charAt(0) == '1'  || yyyy.charAt(0) == '2' || yyyy.charAt(0) == '3')){
				yyyy = "20" + yyyy;
			}
			else if(yyyy.length() == 2){
				yyyy = "19" + yyyy;
			}
			this.date = String.format("%s-%02d-%02d", yyyy, mm, dd);
		}

	}

	/**
	 * 取权重最小的日期
	 * 
	 * @param wds
	 * @return best WeightedDate
	 */
	private WeightedDate getBest(Vector<WeightedDate> wds) {
		int size = wds.size();
		if (size == 0) {
			return null;
		}
		WeightedDate best = wds.get(0);
		for (WeightedDate wd : wds) {
			if (wd.date != null && wd.weight < best.weight) {
				best = wd;
			}
			else if(best.date == null){
				best = wd;
			}
		}
		return best;
	}


	/**
	 * 按照完整日期格式解析 <BR>
	 * <BR>
	 * 支持日期格式包括: <BR>
	 * <P>
	 * yyyy-mm-dd yyyy_mm_dd yyyy.mm.dd yyyy/mm/dd <BR>
	 * yyyy-m-dd yyyy_m_dd yyyy.m.dd yyyy/m/dd <BR>
	 * yyyy-mm-d yyyy_mm_d yyyy.mm.d yyyy/mm/d <BR>
	 * yyyy-m-d yyyy_m_d yyyy.m.d yyyy/m/d <BR>
	 * mm/dd/yyyy mm/d/yyyy m/dd/yyyy m/d/yyyy <BR>
	 * dd-mm-yyyy dd.mm.yyyy yyyymmdd <BR>
	 * </P>
	 * 
	 * @return 解析后最优的日期
	 */
	private WeightedDate getHtmlDateL1() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();

		String yyyy_mm_dd = "(?<!\\d)([12][09][0-9]{2}[-_./][01]?[0-9][-_./][0123]?[0-9])";
		p = Pattern.compile(yyyy_mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			if(m.group(1).split("-").length != 3 && m.group(1).split("_").length != 3 && m.group(1).split("/").length != 3 && m.group(1).split("\\.").length != 3){
				continue;
			}
			WeightedDate wd = new WeightedDate();
			String[] subStrings = m.group(1).split("[-_./]");
			wd.setDate(subStrings[0], Integer.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yyyy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		String mm_dd_yyyy = "(?<!\\d)([01]?[0-9][-_./][0123]?[0-9][-_./][12][09][0-9]{2})";
		p = Pattern.compile(mm_dd_yyyy);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group(1);
			if(date.split("-").length != 3 && date.split("\\.").length != 3 && date.split("/").length != 3){
				continue;
			}
			String[] subStrings = date.split("[-_./]");
			wd.setDate(subStrings[2], Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd_yyyy);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		//12 23, 2010
		String mm_dd_yyyy2 = "(?<!\\d)([01]?[0-9]\\s+[0123]?[0-9][\\s,]+[12][09][0-9]{2})";
		p = Pattern.compile(mm_dd_yyyy2);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group(1).replace(",", " ");
			if(date.split("\\s+").length != 3){
				continue;
			}
			String[] subStrings = date.split("\\s+");
			wd.setDate(subStrings[2], Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd_yyyy);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		String dd_mm_yyyy = "(?<!\\d)([0123]?[0-9][-_./\\s][01]?[0-9][-_./\\s][12][09][0-9]{2})";
		p = Pattern.compile(dd_mm_yyyy);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group(1);
			if(date.split("-").length != 3 && date.split("\\.").length != 3 && date.split("/").length != 3 && date.split("\\s").length != 3){
				continue;
			}
			String[] subStrings = date.split("[-_./\\s]");
			wd.setDate(subStrings[2], Integer.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[0]));
			wd.setWeight(m.start(), DateTimeWeight.yyyy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		String yyyymmdd = "(?<!\\d)([12][09][0-9]{2}[01][0-9][0123][0-9])";
		p = Pattern.compile(yyyymmdd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			String date = m.group(1);
			WeightedDate wd = new WeightedDate();
			if(Integer.parseInt(date.substring(0, 4)) < 1990){
				continue;
			}
			wd.setDate(date.substring(0, 4), Integer.parseInt(date.substring(4,
					6)), Integer.parseInt(date.substring(6, 8)));
			wd.setWeight(m.start(), DateTimeWeight.yyyymmdd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		return this.getBest(wds);
	}

	/**
	 * 按照次完整日期格式解析，即使用缩写的时间：省去年的前两位 <BR>
	 * <BR>
	 * 支持的时间格式: <BR>
	 * yy-mm-dd yy_mm_dd yy.mm.dd yy/mm/dd <BR>
	 * yy-m-dd yy_m_dd yy.m.dd yy/m/dd <BR>
	 * yy-mm-d yy_mm_d yy.mm.d yy/mm/d <BR>
	 * yy-m-d yy_m_d yy.m.d yy/m/d <BR>
	 * yymmdd
	 * 
	 * @return best weighted date
	 */
	private WeightedDate getHtmlDateL2() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();

		String yy_mm_dd = "(?<!\\d)([0-9]{2}[-_./][01]?[0-9][-_./][0123]?[0-9])[^\\d]*";
		p = Pattern.compile(yy_mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			if(m.group(1).split("-").length != 3 && m.group(1).split("_").length != 3 && m.group(1).split("/").length != 3 && m.group(1).split("\\.").length != 3){
				continue;
			}
			WeightedDate wd = new WeightedDate();
			String[] subStrings = m.group(1).split("[-_./]");
			wd.setDate(subStrings[0], Integer.parseInt(subStrings[1]), Integer.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		String mm_dd_yyyy = "(?<!\\d)([01]?[0-9][-_./][0123]?[0-9][-_./][0-9]{2})";
		p = Pattern.compile(mm_dd_yyyy);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group(1);
			if(date.split("-").length != 3 && date.split("\\.").length != 3 && date.split("/").length != 3){
				continue;
			}
			String[] subStrings = date.split("[-_./]");
			wd.setDate(subStrings[2], Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd_yy);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		return this.getBest(wds);
	}

	/**
	 * 按照简写日期格式解析，即省去年份的时间格式 <BR>
	 * <BR>
	 * 支持的时间格式: <BR>
	 * mm-dd mm_dd mm.dd mm/dd <BR>
	 * m-dd m_dd m.dd m/dd <BR>
	 * mm-d mm_d mm.d mm/d <BR>
	 * m-d m_d m.d m/d <BR>
	 * <BR>
	 * 
	 * @return best level 3 date with weight
	 */
	private WeightedDate getHtmlDateL3() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();
		String mm_dd = "(?<!\\d)[01]?[0-9][-_/][0123]?[0-9]";
		p = Pattern.compile(mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = m.group().split("[-_/]");
			wd.setDate(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
			else{
				wd.setDate(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1), Integer.parseInt(subStrings[0]), Integer
						.parseInt(subStrings[1]));
				wd.setWeight(m.start(), DateTimeWeight.mm_dd);
				if(this.checkDateTime(wd.date)){
					wds.add(wd);
				}
			}
		}
		return this.getBest(wds);
	}


	/**
	 * 按照完整日期格式解析包含汉字或全角数字的时间 <BR>
	 * <BR>
	 * 支持时间格式: <BR>
	 * <P>
	 * yyyy年mm月dd日 yyyy年m月dd日 yyyy年mm月d日 yyyy年m月d日 <BR>
	 * [汉语数字]年[汉语数字]月[汉语数字]日 <BR>
	 * </P>
	 * 
	 * @return best level 1 date with weight
	 */
	private WeightedDate getHtmlDateChL1() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();


		String yyyy_mm_dd = "[12][09][0-9]{2}年[01]?[0-9]月[0123]?[0-9]日";
		p = Pattern.compile(yyyy_mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			String[] subStrings = m.group().split("[年月日]");
			WeightedDate wd = new WeightedDate();
			wd.setDate(subStrings[0], Integer.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yyyy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		yyyy_mm_dd = "[１２][0０９][0０１２３４５６７８９]{2}年[0０１]?[0０１２３４５６７８９]月[0０１２３]?[0０１２３４５６７８９]日";
		p = Pattern.compile(yyyy_mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			String[] subStrings = this.replaceUpperCase(m.group()).split("[年月日]");
			WeightedDate wd = new WeightedDate();
			wd.setDate(subStrings[0], Integer.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		yyyy_mm_dd = "([0〇零一二三四五六七八九十两千]{4})年[零0〇一二三四五六七八九十]{1,2}月[0〇零一二三四五六七八九十]{1,3}日";
		p = Pattern.compile(yyyy_mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			String[] subStrings = this.replaceChinese(m.group()).split("[年月日]");
			WeightedDate wd = new WeightedDate();
			wd.setDate(subStrings[0], Integer.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yyyy_mm_dd_CN);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		return this.getBest(wds);
	}

	/**
	 * 按照次日期格式解析包含汉字或全角数字的时间，即省去年份的前两位<BR>
	 * <P>
	 * 支持时间格式: <BR>
	 * yy年mm月dd日 yy年m月dd日 yy年mm月d日 yy年m月d日 <BR>
	 * [汉语数字]年[汉语数字]月[汉语数字]日 <BR>
	 * </P>
	 * 
	 * @return best level 1 date with weight
	 */
	private WeightedDate getHtmlDateChL2() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();
		String str = this.dateTimeStr.replaceAll("\\s+", "");

		String yy_mm_dd = "[0-9]{2}年[01]?[0-9]月[0123]?[0-9]日";
		p = Pattern.compile(yy_mm_dd);
		m = p.matcher(str);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = m.group().split("[年月日]");
			wd.setDate(subStrings[0], Integer
					.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		yy_mm_dd = "[0０１２３４５６７８９]{2}年[0０１]?[0０１２３４５６７８９]月[0０１２３]?[0０１２３４５６７８９]日";
		p = Pattern.compile(yy_mm_dd);
		m = p.matcher(str);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = this.replaceUpperCase(m.group()).split(
					"[年月日]");
			wd.setDate(subStrings[0], Integer
					.parseInt(subStrings[1]), Integer
					.parseInt(subStrings[2]));
			wd.setWeight(m.start(), DateTimeWeight.yy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		yy_mm_dd = "[0〇零一二三四五六七八九十两千]{2}年[0零〇一二三四五六七八九十]{1,2}月[0〇零一二三四五六七八九十]{1,3}日";
		p = Pattern.compile(yy_mm_dd);
		m = p.matcher(str);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = this.replaceChinese(m.group()).split("[年月日]");
			if (subStrings[0].length() == 4) {
				wd.setDate(subStrings[0], Integer.parseInt(subStrings[1]),
						Integer.parseInt(subStrings[2]));
			} else if (subStrings[0].length() == 2) {
				wd.setDate(subStrings[0], Integer
						.parseInt(subStrings[1]), Integer
						.parseInt(subStrings[2]));
			}
			wd.setWeight(m.start(), DateTimeWeight.yy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		return this.getBest(wds);
	}

	/**
	 * 按照简写日期格式解析包含汉字或全角数字的时间，即省去年份<BR>
	 * <P>
	 * 支持时间格式: <BR>
	 * mm月dd日 m月dd日 mm月d日 m月m日 <BR>
	 * [汉语数字]月[汉语数字]日 <BR>
	 * </P>
	 * 
	 * @return level 3 date with weight
	 */
	private WeightedDate getHtmlDateChL3() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();

		String mm_dd = "[01]?[0-9]月[0123]?[0-9]日";
		p = Pattern.compile(mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = m.group().split("[月日]");
			wd.setDate(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		mm_dd = "[0０１]?[0０１２３４５６７８９]月[0０１２３]?[0０１２３４５６７８９]日";
		p = Pattern.compile(mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = this.replaceUpperCase(m.group())
					.split("[月日]");
			wd.setDate(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));

			wd.setWeight(m.start(), DateTimeWeight.mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		mm_dd = "[零0〇一二三四五六七八九十]{1,2}月[0〇零一二三四五六七八九十]{1,3}日";
		p = Pattern.compile(mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String[] subStrings = this.replaceChinese(m.group()).split("[月日]");
			wd.setDate(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), Integer.parseInt(subStrings[0]), Integer
					.parseInt(subStrings[1]));

			wd.setWeight(m.start(), DateTimeWeight.mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		mm_dd = "([一二三四五六七八九十]{1,2}月)\\s+([0123]?[0-9]),?\\s+([12][09][0-9]{2})";
		p = Pattern.compile(mm_dd);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String month = this.replaceChinese(m.group(1)).replace("月", "");
			String day = m.group(2);
			String year = m.group(3);
			wd.setDate(year, Integer.parseInt(month), Integer.parseInt(day));

			wd.setWeight(m.start(), DateTimeWeight.yyyy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		return this.getBest(wds);
	}

	/**
	 * 解析包含英文年份的时间
	 * 
	 * @return level 1 date with weight
	 */
	private WeightedDate getHtmlDateEnL1() {
		Pattern p = null;
		Matcher m = null;
		Vector<WeightedDate> wds = new Vector<WeightedDate>();
		
		String mm_dd_yyyy = "(Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)\\s{0,5}\\d{1,2}(th|st|nd|rd){0,1},{0,1}\\s{0,5}\\d{4}";
		p = Pattern.compile(mm_dd_yyyy, Pattern.CASE_INSENSITIVE);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group().replace(",", " ");
			String[] subStrings = date.split("\\s+");
			if (subStrings.length != 3) {
				continue;
			}
			int month = this.getEnMonth(subStrings[0]);
			if (month == -1) {
				continue;
			}
			String dStr = subStrings[1].toLowerCase().replace("st", "")
					.replace("nd", "").replace("rd", "").replace("th", "");
			wd.setDate(subStrings[2], month, Integer.parseInt(dStr));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd_yyyy);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		String dd_mm_yyyy = "\\d{1,2}(th|st|nd|rd)?\\s{0,5}(Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December),{0,1},?\\s{0,5}\\d{4}";
		p = Pattern.compile(dd_mm_yyyy, Pattern.CASE_INSENSITIVE);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group().replace(",", " ");
			String[] subStrings = date.split("\\s+");
			if (subStrings.length != 3) {
				continue;
			}
			String dStr = subStrings[0].toLowerCase().replace("st", "")
					.replace("nd", "").replace("rd", "").replace("th", "");
			int month = this.getEnMonth(subStrings[1]);
			if (month == -1) {
				continue;
			}
			wd.setDate(subStrings[2], month, Integer.parseInt(dStr));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd_yyyy);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		String dd_mm_yy = "(?<!\\d)\\d{1,2}(th|st|nd|rd)?,?-(Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December),{0,1}-\\d{2,4}";
		p = Pattern.compile(dd_mm_yy, Pattern.CASE_INSENSITIVE);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group().replace(",", " ");
			String[] subStrings = date.split("\\s+|-");
			if (subStrings.length != 3) {
				continue;
			}
			String dStr = subStrings[0].toLowerCase().replace("st", "")
					.replace("nd", "").replace("rd", "").replace("th", "");
			int month = this.getEnMonth(subStrings[1]);
			if (month == -1) {
				continue;
			}
			wd.setDate(subStrings[2], month, Integer.parseInt(dStr));
			wd.setWeight(m.start(), DateTimeWeight.yy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		String dd_mm_yy2 = "(?<!\\d)\\d{1,2}(th|st|nd|rd)?\\s{0,5}(Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December),{0,1},?\\s{0,5}\\d{2}";
		p = Pattern.compile(dd_mm_yy2, Pattern.CASE_INSENSITIVE);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group().replace(",", " ");
			String[] subStrings = date.split("\\s+|-");
			if (subStrings.length != 3) {
				continue;
			}
			String dStr = subStrings[0].toLowerCase().replace("st", "")
					.replace("nd", "").replace("rd", "").replace("th", "");
			int month = this.getEnMonth(subStrings[1]);
			if (month == -1) {
				continue;
			}
			wd.setDate(subStrings[2], month, Integer.parseInt(dStr));
			wd.setWeight(m.start(), DateTimeWeight.mm_dd_yyyy);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}
		
		String yyyy_mm_dd = "\\d{4}\\s*,?(Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)\\s{0,5}\\d{1,2}(th|st|nd|rd){0,1},?";
		p = Pattern.compile(yyyy_mm_dd, Pattern.CASE_INSENSITIVE);
		m = p.matcher(this.dateTimeStr);
		while (m.find()) {
			WeightedDate wd = new WeightedDate();
			String date = m.group().replaceAll(",", " ");
			String[] subStrings = date.split("\\s+,");
			if(subStrings.length < 3){
				continue;
			}
			int month = this.getEnMonth(subStrings[1]);
			if (month == -1) {
				continue;
			}
			String dStr = subStrings[2].toLowerCase().replace("st", "")
					.replace("nd", "").replace("rd", "").replace("th", "");
			wd.setDate(subStrings[0], month, Integer.parseInt(dStr));
			wd.setWeight(m.start(), DateTimeWeight.yyyy_mm_dd);
			if(this.checkDateTime(wd.date)){
				wds.add(wd);
			}
		}

		return this.getBest(wds);
	}

	/**
	 * 将字符串中的中文数字替换为阿拉伯数字
	 * 
	 * @param text
	 * @return
	 */
	private String replaceChinese(String text) {
		String[][] chAry = {{"两千零", "200"}, {"二千零", "200"}, {"二千", "2000"}, {"两千", "2000"}, {"九", "9"}, {"八", "8"}, 
				{"七", "7"}, {"六", "6"}, {"五", "5"}, {"四", "4"}, {"三", "3"}, {"二", "2"}, {"一", "1"}, {"零", "0"}, {"〇", "0"}};
		for(int i = 0; i < chAry.length; i++){
			if (text.indexOf(chAry[i][0]) >= 0){
				text = text.replace(chAry[i][0], chAry[i][1]);
			}
		}
		if(text.replaceAll("\\d+十\\d+", "~~~~~").indexOf("~~~~~") > -1){
			//处理“二十五”这种形式的数字
			text = text.replace("十", "");
		}
		else if(text.replaceAll("十\\d+", "~~~~~").indexOf("~~~~~") > -1 ){
			//处理“十五”这种形式的数字
			text = text.replace("十", "1");
		}
		else if(text.replaceAll("\\d+十", "~~~~~").indexOf("~~~~~") > -1 ){
			//处理“二十”这种形式的数字
			text = text.replace("十", "0");
		} else if(text.indexOf("十") > -1){
			//处理“十”这种形式的数字
			text = text.replace("十", "10");
		}

		return text;
	}

	/**
	 * 替换全角数字
	 * @param text
	 * @return  String
	 */
	private String replaceUpperCase(String text) {
		String[][] chAry = {{"０", "0"}, {"１", "1"}, {"２", "2"}, {"３", "3"}, {"４", "4"}, 
				{"５", "5"},	{"６", "6"}, {"７", "7"}, {"８", "8"}, {"９", "9"}};
		for(int i = 0; i < chAry.length; i++){
			if (text.indexOf(chAry[i][0]) >= 0){
				text = text.replace(chAry[i][0], chAry[i][1]);			
			}
		}
		
		return text;
	}
	
	/**
	 * 将俄语时间替换为阿拉伯数字
	 * @param text
	 * @return  String
	 */
	private String replaceRussian(String text){
		String result = text.toLowerCase().replaceAll("понедельник|вторник|среда|четверг|пятница|суббота|воскресенье", "");//去除星期一~星期日
		String[][] number = {{"январь|января", "1"}, {"февраль|фев", "2"}, {"март|марта|мар", "3"}, {"апрель|апр", "4"}, {"май", "5"}, {"июнь|июня|июн", "6"}, 
				{"июль|июля|июл", "7"}, {"август|авг", "8"}, {"сентябрь|сен", "9"}, {"октябрь|окт", "10"}, {"ноябрь|ноя", "11"}, {"декабрь|дек", "12"}, 
				{"вчера|вчерашний", "昨天"}, {"позавчера", "前天"}, {"сегодня", "今天"}, {"после обеда", "PM"}};
		for(int i = 0; i < number.length; i++){
			result = result.replaceAll("(?i)" + number[i][0], number[i][1]);
		}
		
		return result;
	}

	/**
	 * 替换英文时间<BR>
	 * 
	 * Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec <BR>
	 * January|February|March|April|May|June|July|August|September|October|November|December
	 * <BR>
	 * 
	 * @return int month | 0 if can't match
	 */
	private int getEnMonth(String monthStr) {
		String lMonth = monthStr.toLowerCase();
		String[][] monthAry = {{"january", "1"}, {"february", "2"}, {"march", "3"}, {"april", "4"}, {"may", "5"}, {"june", "6"}, 
				{"july", "7"}, {"august", "8"}, {"september", "9"}, {"october", "10"}, {"november", "11"}, {"december", "12"}};
		for(int i = 0; i < monthAry.length; i++){
			if (lMonth.compareTo(monthAry[i][0]) == 0) {
				return Integer.parseInt(monthAry[i][1]);
			}
		}

		// Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Oct|Nov|Dec|
		String[][] simpleMonthAry = {{"jan", "1"}, {"feb", "2"}, {"mar", "3"}, {"apr", "4"}, {"may", "5"}, {"jun", "6"}, 
				{"jul", "7"}, {"aug", "8"}, {"sep", "9"}, {"oct", "10"}, {"nov", "11"}, {"dec", "12"}};
		for(int i = 0; i < simpleMonthAry.length; i++){
			if (lMonth.indexOf(simpleMonthAry[i][0]) >= 0) {
				return Integer.parseInt(simpleMonthAry[i][1]);
			}
		}

		return -1;
	}
	
	public static void main(String[] args) {
		TimeUtil tu2 = new TimeUtil();
		PropertyConfigurator.configureAndWatch("log4j.properties");
//		String str1 = " 27-11-2010 18:45 ";
//		String str2 = "發表於: 一月  26, 2006 10:54 pm";
////		str = "15 Hours Ago 01:36 PM ";
//		str1 = tu2.parseDateTime(str1, null, null, null, null);
//		str2 = tu2.parseDateTime(str2, null, null, null, null);
//		System.out.println(str1);
//		System.out.println(str2);
//		Date date1 = tu2.parseDate(str1);
//		Date date2 = tu2.parseDate(str2);
//		System.out.println(date2.before(date1));
		
		String str = "发布于昨天 23:49";
		
		tu2.checkDateTime(str);
		
		String date = tu2.parseDateTime(str,null,null,null,null);

		System.out.println(date);
	}
}

