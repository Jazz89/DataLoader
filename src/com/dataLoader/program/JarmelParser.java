package com.dataLoader.program;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Colection;
import com.dataLoader.dataclass.Product;

public class JarmelParser extends Parser {

	private final static String jarmelUrl = "http://www.jarmel.az.pl/panda.html";
	private static final String XML_FILE_NAME = "D://git//DataLoader//DataLoader//xml//Categories.xml";
	private static final String IMG_DIR = "D://git//DataLoader//DataLoader//images//Jarmel//";
	private static URL urlIteamSrc;
	private static BufferedInputStream in;
	private static ByteArrayOutputStream out;
	private static Object fileName;
	private static FileOutputStream fos;
	private Document doc;
	private Colection collection;
	private Elements categories;
	private Element imgElement;
	private Element nameElement;
	private Element sizeElement;
	private Element categoryElement;
	
	@Override
	void parseUrl(String url) throws IOException {
		doc = Jsoup.connect(url).timeout(0).get();
		for(String s : getAllImgUrl()){
			System.out.println(s);
			writeImg(s);
		}

	}

	@Override
	Product parseProductUrl(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void test() throws IOException {
		// TODO Auto-generated method stub

	}
	
	private  List<String> getAllImgUrl(){
		List<String> srcList = new ArrayList<String>();
		Elements media = doc.select("[src]");
		System.out.println("Media: "+ media.size());
		for(Element src : media){
			if (src.tagName().equals("img"))
				srcList.add(src.attr("abs:src"));
		}
		return srcList;
	}
	
	private static void writeImg(String src) {
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
		fileName = src.replaceAll("http://www.jarmel.az.pl/zdjecia/panda/", "jarmel-");
		System.out.println(fileName);
		fos = new FileOutputStream(IMG_DIR + fileName);
		fos.write(response);
		fos.close();
		}
		catch(Exception e){
			if(e instanceof UnknownHostException){
				writeImg(src);
			}else{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		JarmelParser jp = new JarmelParser();
		try {
			jp.parseUrl(jarmelUrl);
			System.out.println("Finish");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	List<Category> parseCategories(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
