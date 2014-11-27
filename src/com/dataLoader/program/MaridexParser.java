package com.dataLoader.program;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Product;

public class MaridexParser extends Parser {

	List<String> myProdLinks = new ArrayList<String>();
	Document doc;
	public static void main(String[] args) {
		MaridexParser parser = new MaridexParser();
		try {
			//parser.doParse("http://sklep.maridex.pl/10-szafy?p=", 7,"458");
			//parser.doParse("http://sklep.maridex.pl/12-garderoby?p=", 2,"449");
			//parser.doParse("http://sklep.maridex.pl/18-stoliki-rtv?p=", 2,"461");
			//parser.doParse("http://sklep.maridex.pl/19-szafki-na-buty?p=", 2,"459");
			//parser.doParse("http://sklep.maridex.pl/13-komody?p=", 4,"439");
			parser.doParse("http://sklep.maridex.pl/14-lawy?p=", 4,"432");
			//parser.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void parseUrl(String url) throws IOException {
		doc = Jsoup.connect(url).timeout(0).get();
		Elements linkElements = doc.select("div.col-md-4").select("a.product_img_link");
		for(Element e : linkElements){
			//System.out.println(e.attr("abs:href"));
			myProdLinks.add(e.attr("abs:href"));
		}
		
	}

	@Override
	Product parseProductUrl(String url) throws IOException {
		try{
		Product product = new Product();
		doc = Jsoup.connect(url).timeout(0).get();
		product.setName(doc.select("div#pb-left-column").select("h1").text()+ " " + doc.select("div#pb-left-column").select("div#short_description_content").select("p").text().replace("SYSTEM", "").replace("    ", " ").replace("  ", " "));
		product.setName(product.getName().replaceAll("'", ""));
		product.setCena(doc.select("div#pb-left-column").select("div.price").text().replace(" z³", "").replace(",", ".").replace(" ", ""));
		product.setOpis(doc.select("div#more_info_sheets").select("h2,h4").text()+doc.select("div#more_info_sheets").select("p").not("p.s_title_block").text().replace("Zobacz Dodaj do koszyka", "").replace("Wymiary:", ""));
		product.setOpis(product.getOpis().replace("'", ""));
		if(!product.getOpis().contains("Wymiary: - "))
			product.setOpis("Wymiary: "+product.getOpis());
		product.addUrlImage(doc.select("div#image-block").select("a.thickbox").attr("abs:href"));
		return product;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	void test() throws IOException {
		parseProductUrl("http://sklep.maridex.pl/komody/271-komoda.html");
		
	}
	
	private void doParse(String url, int page, String catName) {
		try{
			List<Product> productList = new ArrayList<Product>();
			List<Category> catList = new ArrayList<Category>();
			Category cat = new Category();
			for(int i=1;i<=page;i++)
				parseUrl(url + i);
			System.out.println("Total size:" + myProdLinks.size());
			for(String link : myProdLinks){
				Product product = new Product();
				product = parseProductUrl(link);
				if(product!=null){
				product.setCategoryName(catName);
				product.setImg(product.getImageUrlList().get(0));
				productList.add(product);
				
				System.out.println(product.getName());
				System.out.println(product.getCena());
				System.out.println(product.getOpis());
				System.out.println(product.getImg());
				System.out.println();
				}
			}
			cat.setProductList(productList);
			catList.add(cat);
			objectToXML(catList, new File(XML_FILE_NAME));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	List<Category> parseCategories(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
