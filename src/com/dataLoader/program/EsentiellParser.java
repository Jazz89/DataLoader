package com.dataLoader.program;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Product;

public class EsentiellParser extends Parser {

	Document doc;
	List<String> myProdLinks = new ArrayList<String>();
	List<Category> categoriesList; 
	
	public static void main(String[] args) {
		try {
			EsentiellParser parser = new EsentiellParser();
			parser.test();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	void parseUrl(String url) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	Product parseProductUrl(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void test() throws IOException {
		parseCategories("http://www.e-sentiell.com/pol_m_Kolekcje-617.html");
		
	}

	@Override
	List<Category> parseCategories(String url) throws IOException {
		categoriesList = new ArrayList<Category>();
		doc = Jsoup.connect(url).timeout(0).get();
		Elements elements_lvl1 = doc.select("ul.menu_category1").select("li.first_lvl");
		//System.out.println(elements);
		for(Element e_lvl1 : elements_lvl1){
			Category category_lvl1 = new Category();
			
			//System.out.println(e_lvl1.select("span").text());
			//System.out.println(e_lvl1.select("ul.menu_category2").select("li").select("a").first());
			category_lvl1.setName(e_lvl1.select("span").text());
			//System.out.println("	"+e_lvl1.select("a.menu_category2").size());
			if(!elements_lvl1.select("ul.menu_category2").select("a.menu_category2").empty().isEmpty())
				for(Element e_lvl2 : e_lvl1.select("ul.menu_category2")){
					Category category_lvl2 = new Category();
					System.out.println(e_lvl2.select("ul.menu_category2"));
					if(!e_lvl2.select("ul.menu_category3").select("a.menu_category3").empty().isEmpty())
					for(Element e_lvl3 : e_lvl2.select("ul.menu_category3").select("a.menu_category3")){
						//System.out.println();
						//System.out.println(e_lvl2.select("ul.menu_category3").select("a.menu_category3").text());
					}
			}
		}
		//System.out.println(elements_lvl1.get(0));
		//elements_lvl1 = doc.select("ul.menu_category1").select("ul.menu_category2").select("a.menu_category2");
		//System.out.println(elements_lvl1.text());
		return categoriesList;
	}

}
