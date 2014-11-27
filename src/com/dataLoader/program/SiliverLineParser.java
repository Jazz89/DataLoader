package com.dataLoader.program;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Colection;
import com.dataLoader.dataclass.Furniture;
import com.dataLoader.dataclass.Product;
import com.thoughtworks.xstream.XStream;
public class SiliverLineParser {
    private static List<String> cookies;
    private static HttpURLConnection conn;
	private static final String IMG_DIR = "D://wega//";

    private final String USER_AGENT = "Mozilla/5.0";
    private static Product product;
    private static Category category;
    private static Colection collection;
    private static Document doc;
    private static Document docCollection;
    private static Document categoryDoc;
    private static List<Category> categryList = new ArrayList<Category>();
    private static String url = "http://www.silver-line.pl/index.php";
    private static SiliverLineParser http;
    private static WebDriver driver;
    private static List<String> imageUrlList = new ArrayList<String>();
    private static String collectionImageUrl = "";
    private static String imgUrl;
    private static Elements imgElements;
    private static String fileName;
    private static java.sql.Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static ByteArrayOutputStream out;
	private static FileOutputStream fos;
	private static URL urlIteamSrc;
	private static BufferedInputStream in;
    /**
     * @param args
     * @throws IOException 
     */
    public static final String XML_FILE_NAME = "D://git//DataLoader//DataLoader//xml//Categories.xml";
	
    public static void main(String[] args) throws Exception {	 
		http = new SiliverLineParser();
		driver = new HtmlUnitDriver();
		
		CookieHandler.setDefault(new CookieManager());
		String page = http.GetPageContent(url);
		String postParams = http.getFormParams(page, "info@wega-srebro.pl", "wisnia");
		http.sendPost(url, postParams);
		String result = http.GetPageContent(url);
		doc = Jsoup.parse(result.toString());
		for(Element e : doc.select("a[href].hurt_cat_links")){
		    if(!e.text().equals("NOWOŚCI")){
		    	category = new Category();
			category.setUrl(url+e.attr("href"));
			category.setName(e.text());
			categoryDoc = Jsoup.parse(http.GetPageContent(category.getUrl()));
			for(Element col : categoryDoc.select("div.product")){
			    String collectionUrl = url+col.select("a").attr("href");
			    docCollection = Jsoup.parse(http.GetPageContent(collectionUrl)); 
			    Elements optionElements = docCollection.select("option");
			    if(optionElements.size()>0){
			   	for(Element option : docCollection.select("option")){
			   	    getCollections(option.text(),optionElements.indexOf(option),collectionUrl,category.getName());
			   	}
			    }else{
				getCollections(docCollection.select("form").select("h2").not("h2.hurt_set_h2").text().replaceAll("opcja ", ""),-1,collectionUrl,category.getName());
			    }
			}
			categryList.add(category);
		    }
		}
		
		objectToXML(categryList, new File(XML_FILE_NAME));
		categryList.clear();
	  }
    
    private static void writeImg(Colection collection,String src, int index) {
	try{
	URL urlIteamSrc = new URL(src);
	BufferedInputStream in = new BufferedInputStream(urlIteamSrc.openStream());
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	byte[] buf = new byte[1024];
	int n = 0;
	while (-1 != (n = in.read(buf))) {
		out.write(buf, 0, n);
	}
	out.close();
	in.close();
	byte[] response = out.toByteArray();
	String colectionfileName = category.getName()+"//"+collection.getName().replaceAll("ZESTAW ", "") + "//" + collection.getOpcja() + "//" + index + ".jpg";
	File file = new File(IMG_DIR + colectionfileName);
	file.getParentFile().mkdirs();
	FileOutputStream fos = new FileOutputStream(file);
	fos.write(response);
	fos.close();
	}
	catch(Exception e){
		if(e instanceof UnknownHostException){
			writeImg(collection, src, index);
		}
	}
}
   
	private static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
	private static Furniture xmlToObject() throws IOException{
		XStream xstream = new XStream();	
		Furniture furniture = new Furniture();
		furniture = (Furniture) xstream.fromXML(new FileInputStream(new File(XML_FILE_NAME)));
		return furniture;
	}
    
	private static void writeImg(Product product,String src, int kolid) {
		try{
		urlIteamSrc = new URL(src);
		in = new BufferedInputStream(urlIteamSrc.openStream());
		out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		fileName = product.getName() + imageUrlList.indexOf(src);
		fileName = fixFileName(fileName);
		System.out.println(fileName);
		fos = new FileOutputStream(IMG_DIR + fileName + ".jpg");
		fos.write(response);
		fos.close();
		product.addImageFileName(fileName);
		}
		catch(Exception e){
			if(e instanceof UnknownHostException){
				writeImg(product, src, kolid);
			}
		}
	}
	
	private static String fixFileName(String name) {
		return name
		.replaceAll("/", "X")
		.replaceAll("ę", "e").replaceAll("ó", "o")
		.replaceAll("ę", "e").replaceAll("ą", "a")
		.replaceAll("ś", "s").replaceAll("ł", "l")
		.replaceAll("ż", "z").replaceAll("ź", "z")
		.replaceAll("ć", "c").replaceAll("ń", "n")
		.replaceAll("Ś", "S").replaceAll("Ł", "L")
		.replaceAll("Ż", "Z").replaceAll("Ź", "Z")
		.replaceAll("Ć", "C").replaceAll("Ń", "N");
	}

	
    private static Colection getCollections(String option,int index, String collectionUrl, String categoryName) throws Exception{
	driver.get(collectionUrl);
	if(driver.getPageSource().contains("form_login")){
	WebElement loginInput = driver.findElement(By.id("form_login"));
	WebElement passInput = driver.findElement(By.id("form_haslo"));
	WebElement submit = driver.findElement(By.className("hurt_submit"));
	loginInput.clear();
	loginInput.sendKeys("info@wega-srebro.pl");
	passInput.clear();
	passInput.sendKeys("wisnia");
	submit.click();
	driver.get(collectionUrl);
	}
	docCollection = Jsoup.parse(driver.getPageSource());
	if(docCollection.getElementById("opcje")!=null){
	Select select = new Select(driver.findElement(By.id("opcje")));
	select.selectByIndex(index);
	}
	docCollection = Jsoup.parse(driver.getPageSource());
	//WebElement option = driver.findElement(arg0);
	    collection = new Colection();
	    collection.setName(docCollection.select("h1.hurt_set_h1").text().replace("Ø", "").replace("ø", "")+ " " + option );	    
	    collection.setOpcja(option);
	    collection.setCategoryName(categoryName);
	    
	    if(index==-1){
		imgElements = docCollection.select("a.hurt_img_mini").select("img.hurt_img_mini");
		collectionImageUrl = url.replaceAll("/index.php", "")+docCollection.select("div.hurt_product_desc").select("a.hurt_img_big").select("img").attr("src");
	    }else{
		imgElements = docCollection.select("table#zdj_opt_"+index).select("a.hurt_img_mini").select("img.hurt_img_mini");	
		collectionImageUrl = url.replaceAll("/index.php", "")+docCollection.select("table#zdj_opt_"+index).select("a.hurt_img_big").select("img").attr("src");
	    }
	    for(Element e : imgElements){		
		    imgUrl = url.replaceAll("/index.php", "")+e.attr("src").replaceAll("products", "products_vb");
		    imageUrlList.add(imgUrl);
		    //writeImg(collection, imgUrl,docCollection.select("img.hurt_img_mini").indexOf(e)+1);
		}
	    //writeImg(collection, collectionImageUrl ,0);
	    collection.setImageUrl(collectionImageUrl);
	    category.addCollection(collection);
	    
	    Elements elements = docCollection.select("div.hurt_elementy").select("tr");
	    for(Element e1 : elements){
		if(elements.indexOf(e1)!=0){
		    if(e1.select("td").size()>1){
			product = new Product();
			product.setName(e1.select("td").get(0).text());
			if(product.getName().equals("Sztyft") || product.getName().equals("Wiszące")){
			    product.setName("Kolczyki "+product.getName());
			}
			String cena = e1.select("td").get(3).text();
			String waga = e1.select("td").get(2).text();
			if(cena.contains("ZŁ/g")){
			    cena = cena.replaceAll(" ZŁ/g", "").replace("\\s", "").replace(",",".");
			    waga = waga.replaceAll("g", "").replace("\\s", "");
			    waga = waga.substring(0, waga.length()-1);
			    DecimalFormat df = new DecimalFormat("#.##");
			    cena = df.format(1.3*Double.valueOf(cena)*Double.valueOf(waga));
			}
			else if(cena.contains("ZŁ/para")){
			    cena = cena.replaceAll(" ZŁ/para", "").replace("\\s", "").replace(",",".");
			}
			else if(cena.contains("ZŁ/szt")){
			    cena = cena.replaceAll(" ZŁ/szt", "").replace("\\s", "").replace(",",".");
			}
			product.setKod(e1.select("td").get(1).text());
			product.setSzerokosc(waga);
			product.setCena(cena);
			product.setOpcja(option);
			product.setCategoryName(categoryName);
			product.setCollectionName(collection.getName());
			
			
			System.out.println(category.getName().toUpperCase());
			System.out.println(collection.getName() +" "+ product.getOpcja()+" "+collection.getCategoryName());
			System.out.println("-- "+product.getName());
			System.out.println("-- -- "+product.getOpcja());
			System.out.println("-- -- "+product.getSzerokosc());
			System.out.println("-- -- "+product.getKod());
			System.out.println("-- -- "+product.getCena());
			
			collection.getProductList().add(product);
		    }
		}
	    }
	    category.addCollection(collection);
	    
	
	return null;
    }
    
    private void sendPost(String url, String postParams) throws Exception {	 
		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", "accounts.google.com");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "pl-PL,pl;q=0.5");
		for (String cookie : cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));	 
		conn.setDoOutput(true);
		conn.setDoInput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		BufferedReader in = 
	             new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		}
		in.close();
    }
	 
    private String GetPageContent(String url) throws Exception {
		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("GET");	 
		conn.setUseCaches(false);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "pl-PL,pl;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}	 
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		setCookies(conn.getHeaderFields().get("Set-Cookie"));
		return response.toString();	 
    }
	 
    public String getFormParams(String html, String username, String password)
			throws UnsupportedEncodingException {
		Document doc = Jsoup.parse(html);
		Elements inputElements = doc.getElementsByTag("input");
		List<String> paramList = new ArrayList<String>();
		for (Element inputElement : inputElements) {
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");	 
			if (key.equals("form_login"))
				value = username;
			else if (key.equals("form_haslo"))
				value = password;
			paramList.add(key + "=" + URLEncoder.encode(value));
		}
		StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
	  }
	
    private static void objectToXML(final List<Category> categryList, final File file) throws IOException{
	XStream xstream = new XStream();	
	Furniture furniture = new Furniture();
	furniture.setFurnitureList(categryList);
	Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
	writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	xstream.toXML(furniture, writer);		
    }
    
    
    public static List<String> getCookies() {
		return cookies;
    }
	 
    public void setCookies(List<String> cookies) {
		this.cookies = cookies;
    }
    
}
