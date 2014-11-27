package com.dataLoader.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Product;

public class NowystylParser extends Parser{

	List<String> myProdLinks = new ArrayList<String>();
	Document doc;
	WebDriver driver = new FirefoxDriver();
	public static void main(String[] args) {
		NowystylParser pareser = new NowystylParser();
		try {
			pareser.test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	void parseUrl(String url) throws IOException {
		
		driver.get(url);
		String source = driver.getPageSource();
		//System.out.println(driver.getPageSource());
		doc = Jsoup.parse(source);
		Elements linkElements = doc.select("ul.catalogue").select("a[href]");
		for(Element e : linkElements){
			driver.get("http://www.nowystyl.pl"+e.toString().substring(e.toString().indexOf("<a href=\"")+9, e.toString().indexOf("\">")));
			source = driver.getPageSource();
			doc = Jsoup.parse(source);
			for(Element element : doc.select("ul.catalogue").select("a[href]")){
				System.out.println("http://www.nowystyl.pl"+element.toString().substring(e.toString().indexOf("<a href=\"")+9, e.toString().indexOf("\">")+2));
				myProdLinks.add("http://www.nowystyl.pl"+element.toString().substring(e.toString().indexOf("<a href=\"")+9, e.toString().indexOf("\">")+2));
			}
				
		}
	
		
	}

	@Override
	Product parseProductUrl(String url) throws IOException {
		driver.get(myProdLinks.get(0));
		String source = driver.getPageSource();
		doc = Jsoup.parse(source);
		return null;
	}

	@Override
	void test() throws IOException {
		List<Product> productList = new ArrayList<Product>();
		List<Category> catList = new ArrayList<Category>();
		Category cat = new Category();
		//int page = 3;
		//for(int i=1;i<page;i++)
		parseUrl("http://www.nowystyl.pl/produkty/fotele-gabinetowe?page="+1);
		
		
	}

	@Override
	List<Category> parseCategories(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
