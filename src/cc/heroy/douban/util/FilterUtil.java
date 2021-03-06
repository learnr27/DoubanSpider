package cc.heroy.douban.util;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用于去重
 *
 */
public class FilterUtil {

	/**
	 * 将解析的url集合去重放入url队列
	 * @param set :要处理的url
	 * @param urls :待访问url队列
	 * @param usedURLS :已被访问url集合
	 */
	public static void URLFilter(Set<String> set ,BlockingQueue<String> urls,CopyOnWriteArraySet<String> usedURLS){
		//set去重,放入urls队列
		for(String url : set){
			if(!urls.contains(url)){
				urls.add(url);
				usedURLS.add(url);
System.out.println("添加url："+url);				
			}else{
System.out.println("去重中 ："+url);
			}
		}
	}
}
