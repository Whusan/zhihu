package casia.isiteam.zhihu_event.util;

public class StringUtil {

	/**
	 * true-空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(null == str || "".equals(str.trim())){
			return true ;
		}
		
		return false;
	}
}
