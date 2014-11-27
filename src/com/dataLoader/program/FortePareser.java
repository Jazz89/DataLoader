package com.dataLoader.program;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Furniture;
import com.dataLoader.dataclass.Product;
import com.thoughtworks.xstream.XStream;

public class FortePareser extends Parser {

	private Elements productsUrls;
	private Document doc;
	private Category category;
	private Product product;
	private List<Category> categryList = new ArrayList<Category>();
	private List<String> myProdLinks = new ArrayList<String>();

	/*
	 * public void parseUrl(String url) { try { for (Element categoryElement :
	 * getCategoryUrls(url)) { category = new Category();
	 * category.setUrl(categoryElement.attr("abs:href")); doc =
	 * Jsoup.connect(category.getUrl()).timeout(0).get();
	 * category.setName(doc.select("div.product_type").select("h2") .text());
	 * productsUrls = getProductsUrls(category.getUrl()); for (Element
	 * productElement : productsUrls) { product =
	 * parseProductUrl(productElement.attr("abs:href"));
	 * category.addProduct(product); printProductInfo(product); }
	 * categryList.add(category); } objectToXML(categryList, new
	 * File(XML_FILE_NAME)); } catch (IOException e) { e.printStackTrace(); } }
	 */

	@Override
	void parseUrl(String url) {
		try {
			doc = Jsoup.connect(url).timeout(0).get();
			Elements linkElements = doc.select("div.product_box").select(
					"a.miniature");
			for (Element e : linkElements)
				myProdLinks.add(e.attr("abs:href"));
			System.out.println("Total size: " + myProdLinks.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Product parseProductUrl(String url) throws IOException {
		try {
			Product product = new Product();
			product.setUrl(url);
			Document doc = Jsoup.connect(product.getUrl()).timeout(0)
					.ignoreContentType(true).get();
			Elements elements = doc.select("div.product").select("h3");
			for (Element e : elements) {
				if (e.text().contains("Kolekcja"))
					product.setCollectionName(e.select("a").text());
				if (e.text().contains("Kategoria"))
					product.setCategoryName(e.select("a").text());
			}

			product.setName(doc.select("div.product").select("h2").text());
			product.setCena(doc.select("section.side")
					.select("div.price_wrapper").select("span.price").text()
					.replaceAll(" ,-", ""));
			product.setOpis(doc.select("div.seoCustomHTMLdefault").text());
			product.setSzerokosc(doc.select("span[itemprop=width]").text());
			product.setWysokosc(doc.select("span[itemprop=height]").text());
			product.setGlebokosc(doc.select("span[itemprop=depth]").text());
			product.setDostawa(doc.select("div.delivery").text());
			product.addUrlImage(doc.select("a.miniature,lightbox,cboxElement")
					.attr("abs:href"));
			String s = doc.select("div.image_gallery")
					.select("a.lightbox,cboxElement").attr("abs:href");
			if (!s.isEmpty()) {
				product.addUrlImage(s);
			}
			return product;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void test() throws IOException {
		List<Category> catList = new ArrayList<Category>();
		Category cat = new Category();
		Product product = new Product();
		product = parseProductUrl("http://www.forte.com.pl/meble/biurko-7802620508129530.html");
		cat.addProduct(product);
		catList.add(cat);
		objectToXML(catList, new File(XML_FILE_NAME));
	}

	private Elements getCategoryUrls(String url) throws IOException {
		Document doc = Jsoup.connect(url).timeout(0).get();
		return doc.select("div#content").select("nav").addClass("side")
				.select("ul").addClass("navigator").select("li").select("a");
	}

	private Elements getProductsUrls(String url) throws IOException {
		Document doc = Jsoup.connect(url).timeout(0).get();
		return doc.select("div.product_box").select(
				"a.miniature,intemBlock-Photo,imageWrapper");
	}

	private void doParse(String url, int page, String catName) {
		try {
			List<Product> productList = new ArrayList<Product>();
			List<Category> catList = new ArrayList<Category>();
			Category cat = new Category();
			parseUrl(url);
			for (String link : myProdLinks) {
				Product product = new Product();
				product = parseProductUrl(link);
				product.setCategoryName(catName);
				product.setImg(product.getImageUrlList().get(0));
				System.out.println(product.getName());
				productList.add(product);

			}
			cat.setProductList(productList);
			catList.add(cat);
			objectToXML(catList, new File(XML_FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FortePareser parser = new FortePareser();
		try {
//			parser.doParse("http://www.forte.com.pl/meble/typy/biurko.html", 0,"431");
//			parser.doParse("http://www.forte.com.pl/meble/typy/drzwi.html", 0,"438");
//			parser.doParse("http://www.forte.com.pl/meble/typy/komoda.html", 0,"439");
//			parser.doParse("http://www.forte.com.pl/meble/typy/kontener.html", 0,"440");
//			parser.doParse("http://www.forte.com.pl/meble/typy/krzeslo.html", 0,"473");
//			parser.doParse("http://www.forte.com.pl/meble/typy/lustro.html", 0,"442");
//			parser.doParse("http://www.forte.com.pl/meble/typy/lozko.html", 0,"433");
//			parser.doParse("http://www.forte.com.pl/meble/typy/mebloscianka.html", 0,"444");
//			parser.doParse("http://www.forte.com.pl/meble/typy/nadstawka.html", 0,"445");
//			parser.doParse("http://www.forte.com.pl/meble/typy/oswietlenie.html", 0,"446");
//			parser.doParse("http://www.forte.com.pl/meble/typy/panel-ubraniowy.html", 0,"447");
//			parser.doParse("http://www.forte.com.pl/meble/typy/polka.html", 0,"448");
//			parser.doParse("http://www.forte.com.pl/meble/typy/przedpokoj.html", 0,"449");
//			parser.doParse("http://www.forte.com.pl/meble/typy/przewijak.html", 0,"450");
//			parser.doParse("http://www.forte.com.pl/meble/typy/regal.html", 0,"451");
//			parser.doParse("http://www.forte.com.pl/meble/typy/regal-wiszacy.html", 0,"452");
//			parser.doParse("http://www.forte.com.pl/meble/typy/stolik-okolicznosciowy.html", 0,"432");
//			parser.doParse("http://www.forte.com.pl/meble/typy/stol.html", 0,"472");
//			parser.doParse("http://www.forte.com.pl/meble/typy/szafa.html", 0,"458");
//			parser.doParse("http://www.forte.com.pl/meble/typy/szafka-na-buty.html", 0,"459");
//			parser.doParse("http://www.forte.com.pl/meble/typy/stolik-nocny.html", 0,"460");
//			parser.doParse("http://www.forte.com.pl/meble/typy/szafka-rtv.html", 0,"461");
//			parser.doParse("http://www.forte.com.pl/meble/typy/szafka-wiszaca.html", 0,"462");
//			parser.doParse("http://www.forte.com.pl/meble/typy/szuflada.html", 0,"463");
//			parser.doParse("http://www.forte.com.pl/meble/typy/toaletka.html", 0,"464");
//			parser.doParse("http://www.forte.com.pl/meble/typy/witryna.html", 0,"465");
			parser.doParse("http://www.forte.com.pl/meble/typy/witryna-wiszaca.html", 0,"466");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	List<Category> parseCategories(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
