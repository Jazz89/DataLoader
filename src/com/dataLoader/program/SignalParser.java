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

public class SignalParser extends Parser {
	List<String> myProdLinks = new ArrayList<String>();
	Document doc;

	@Override
	void parseUrl(String url) throws IOException {

		doc = Jsoup.connect(url).timeout(0).get();
		Elements elements = doc.select("table").select("tbody").select("tr")
				.select("td").select("table").select("tbody").select("tr")
				.select("td").select("a[href]");
		for (Element e : elements) {
			if (!e.toString().contains("cart"))
				myProdLinks.add(e.attr("abs:href"));
		}
		// System.out.println(myProdLinks);
		// System.out.println(myProdLinks.size());
	}

	@Override
	Product parseProductUrl(String url) throws IOException {
		try{
		Product product = new Product();
		url = url.replaceAll("\r\n", "");
		Document doc = Jsoup.connect(url).timeout(0).get();
		Elements elements = doc.select("table[style=padding-left:10px;]")
				.select("td");
			
			product.setName(elements.get(1).html().substring(0, elements.get(1).html().indexOf("<a style=")).replace("&nbsp;<img style=\"border: 2px solid #fff; box-shadow: rgba(0, 0, 0, 0.6) 0px 2px 2px;\" src=\"./images/new.jpg \" />", ""));
			product.setCena(elements.select("p").text().replace("z³", ""));
			product.setOpis(elements.select("td[style=width:800px]").text()
					.replace("Opis: ", ""));
			product.setName(product.getName()+" "+product.getOpis().substring(product.getOpis().indexOf("Kolor: ")+7, product.getOpis().length()-1));
			product.setImg(elements.select("td").select("a")
					.select("img[width=450px]").attr("abs:src"));
			//System.out.println(product.getName());
			//System.out.println(product.getCena());
			//System.out.println(product.getOpis());
			//System.out.println(product.getImg());
			//System.out.println();
		
		return product;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	void test() throws IOException {
List<Product> productList = new ArrayList<Product>();
List<Category> catList = new ArrayList<Category>();
Category cat = new Category();
		//for(int i=0;i<2;i++){
			parseUrl("http://www.signal.pl/o_typ_fotelobreco.php?id_typ=17&porcja=" + 0);
			//for(String url : myProdLinks){
			Document doc = Jsoup.connect(myProdLinks.get(0)).timeout(0).get();
			Elements elements = doc.select("table[style=padding-left:10px;]").select("td");
			Element element = elements.get(9).select("a[href]").first();
			System.out.println(element);
			productList.add(parseProductUrl(element.attr("abs:href")));
			//for (Element e : elements.get(9).select("a[href]"))
				//productList.add(parseProductUrl(e.attr("abs:href")));
			//}
		//}
		cat.setProductList(productList);
		catList.add(cat);
		objectToXML(catList, new File(XML_FILE_NAME));

	}
	
	private void doParse(String url,int page,String catName) throws IOException {
		try{
		List<Product> productList = new ArrayList<Product>();
		List<Category> catList = new ArrayList<Category>();
		Category cat = new Category();
				for(int i=0;i<page;i++){
					parseUrl(url + i);
					for(String prodUrl : myProdLinks){
						System.out.println(prodUrl);
					Document doc = Jsoup.connect(prodUrl).timeout(0).get();
					Elements elements = doc.select("table[style=padding-left:10px;]").select("td");
					Elements linkElements = elements.get(9).select("a[href]");
					if(linkElements.isEmpty())
						linkElements = elements.get(11).select("a[href]");
					for (Element e : linkElements){
						Product prod = new Product();
						//System.out.println(e.attr("abs:href"));
						prod = parseProductUrl(e.attr("abs:href"));
						System.out.println(prod.getName());
						System.out.println(prod.getCena());
						System.out.println(prod.getOpis());
						System.out.println(prod.getImg());
						
						if(!productList.contains(prod)){
							productList.add(prod);
							System.out.println("true");
						}else{
							System.out.println("false");
						}
						System.out.println();
					}
						
					}
					myProdLinks.clear();
				}
				for(Product product : productList){
					product.setCategoryName(catName);
				}
				System.out.println("Total size: "+productList.size());
				cat.setProductList(productList);
				catList.add(cat);
				objectToXML(catList, new File(XML_FILE_NAME));
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SignalParser parser = new SignalParser();
		try {
			//parser.test();
			//fotele tkanina
			//parser.doParse("http://www.signal.pl/o_typ_fotelobrtkan.php?id_typ=18&porcja=",1,"468");
			//krzesla dzieciece
			//parser.doParse("http://www.signal.pl/o_typ_krzesladzieciece.php?id_typ=19&porcja=",1,"469");
			//biurka
			//parser.doParse("http://www.signal.pl/o_typ_biurka.php?id_typ=20&porcja=",1,"431");
			//³awy 
			//parser.doParse("http://www.signal.pl/o_typ_lawy.php?id_typ=1&porcja=",4,"432");
			//barki
			//parser.doParse("http://www.signal.pl/o_typ_barkiszklane.php?id_typ=2&porcja=",1,"470");
			//fotele
			//parser.doParse("http://www.signal.pl/o_typ_fotele.php?id_typ=3&porcja=",1,"471");
			//szafki tv
			//parser.doParse("http://www.signal.pl/o_typ_szafkitv.php?id_typ=4&porcja=",1,"461");
			//sto³y
			//parser.doParse("http://www.signal.pl/o_typ_stoly.php?id_typ=5&porcja=",4,"472");
			//krzes³a
			parser.doParse("http://www.signal.pl/o_typ_krzesla.php?id_typ=6&porcja=",5,"473");			
			//hokery
			//parser.doParse("http://www.signal.pl/o_typ_hokery.php?id_typ=7&porcja=",2,"474");			
			//³ózka
			//parser.doParse("http://www.signal.pl/o_typ_lozka.php?id_typ=8&porcja=",2,"433");			
			//szafki nocne
			//parser.doParse("http://www.signal.pl/o_typ_szafkinocne.php?id_typ=9&porcja=",1,"460");			
			//komody
			//parser.doParse("http://www.signal.pl/o_typ_komody.php?id_typ=10&porcja=",1,"439");			
			//toaletki
			//parser.doParse("http://www.signal.pl/o_typ_toaletki.php?id_typ=11&porcja=",1,"464");			
			//szafy
			//parser.doParse("http://www.signal.pl/o_typ_szafy.php?id_typ=12&porcja=",1,"458");									
			//³ózka dzeciêce
			//parser.doParse("http://www.signal.pl/o_typ_lozkadzieciece.php?id_typ=16&porcja=",1,"475");			
			//Wieszaki stoj¹ce
			//parser.doParse("http://www.signal.pl/o_typ_wieszakistojace.php?id_typ=21&porcja=",1,"476");			
			//Wieszaki garderobiane
			//parser.doParse("http://www.signal.pl/o_typ_wieszakistojace.php?id_typ=22&porcja=",1,"477");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	List<Category> parseCategories(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
