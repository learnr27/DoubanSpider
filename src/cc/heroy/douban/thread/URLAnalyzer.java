package cc.heroy.douban.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cc.heroy.douban.util.FilterUtil;
import cc.heroy.douban.util.RegexUtil;

/**
 * URL解析器线程
 * 从entitys中获取entity,解析出url地址
 * 将url进行去重判断，若在usedURLS中存在，则抛弃，否则添加到usedURLS和urls中
 * 
 */
public class URLAnalyzer implements Runnable{

	private BlockingQueue<String> entitys ;
	
	private BlockingQueue<String> urls ;
	
	private CopyOnWriteArraySet<String> usedURLS;
	
	private CountDownLatch startGate ;
	
	CountDownLatch endGate ;
	public URLAnalyzer(BlockingQueue<String> entitys,BlockingQueue<String> urls, CopyOnWriteArraySet<String> usedURLS,CountDownLatch startGate,CountDownLatch endGate){
		this.entitys = entitys;
		this.urls = urls;
		this.usedURLS = usedURLS;
		this.startGate = startGate;
		this.endGate = endGate;
	}
	
	@Override
	public void run() {
		try {
			startGate.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
System.out.println("URLAnalyzer启动");
		while(!entitys.isEmpty()){
			try {
				String content = entitys.take();
				analyzer(content);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		endGate.countDown();
System.out.println("URLAnalyzer结束");
	}
	
	
	//解析entity的url
	private void analyzer(String content){
//test页面链接
		
		//将content转换成document对象
		Document doc = Jsoup.parse(content);
		//找到页面所有链接
		Elements links = doc.select("a[href]");
		
		Set<String> set = new HashSet<String>();
		for(Element e :links){
			String url = e.attr("href");
			set.add(url);
		}
		//判断url是否为电影详情页
		set = RegexUtil.URLRegex(set);
	
		//去重
		FilterUtil.URLFilter(set, urls, usedURLS);
		
	}
}
