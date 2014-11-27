package com.dataLoader.program;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Colection;
import com.dataLoader.dataclass.Furniture;
import com.dataLoader.dataclass.Product;
import com.thoughtworks.xstream.XStream;

public class ProductLoader {

	private static final String XML_FILE_NAME = "E://git//DataLoader//DataLoader//xml//Categories.xml";
	private static final String IMG_DIR = "E://git//DataLoader//DataLoader//images//";
	private static final String IMG_DIR2 = "E://wega//0000//";
	private static Furniture furniture;
	private static InputStream in;
	private static URL urlIteamSrc;
	private static ByteArrayOutputStream out;
	private static FileOutputStream fos;
	private static String fileName;
	private static List<Product> productList;
	private static List<String> imageUrlList;
	private static java.sql.Connection connect = null;
	private static Statement statement = null;
	private static java.sql.PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static File file = new File("E://git//DataLoader//DataLoader//sql//file");
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static int prodid;
	private static String query = "";
	private static int imgId;
	private static int colid;
	private static String categoryImgUrl;
	private static List<Colection> colectionList;
	
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
		fileName = product.getName()+product.getKod();
		fileName = fixFileName(fileName);
		fos = new FileOutputStream(IMG_DIR + fileName +".jpg");
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
		.replaceAll("Ą", "A")
		.replaceAll("Ć", "C").replaceAll("Ń", "N");
	}


	private static String writeToFile(Product product, int prodid){
		try{
			if(product.getCena().equals(""))product.setCena("0.00");
			query = "INSERT INTO `products` VALUES("+ prodid +",1,NULL,'"+ product.getImgFileNameList().get(0)+".jpg',"+ product.getCena() + ", '2013-11-10 19:11:05',"+ " NULL, NULL, '0.00', 1, 0, 0, 0, NULL, NULL, NULL, NULL, NULL);"
				  + "INSERT INTO `products_description` VALUES("+ prodid+ ", 2, "+ "'"+ product.getName()+ "','"+ product.getOpis()+"','', 1);"
				  + "INSERT INTO `products_to_categories` VALUES(" + prodid + ", " + product.getCategoryName() + ");";
				  //+ "INSERT INTO `products_size` VALUES(" + prodid + ", " + product.getSzerokosc() + ", " + product.getWysokosc() + ", " + product.getGlebokosc() + ");";
			//for (String img : product.getImgFileNameList()) {
				//query += "INSERT INTO `products_images` VALUES ("+ imgId+ ", "+ prodid+ ", '"+ img+ ".jpg', NULL, 1);";
				//imgId++;
			//}
			System.out.println(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return query;

	}

	private static void load(){
		try {
			prodid = 3637;
			
			List<Product> prodList = new ArrayList<Product>();
			furniture = xmlToObject();
			prodList = furniture.getFurnitureList().get(0).getProductList();
			for (Product product : prodList) {
				product.setKod(Integer.toString(prodid));
				writeImg(product, product.getImg(), Integer.parseInt(product.getCategoryName()));										
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);
				bw.write(writeToFile(product, prodid));
				bw.newLine();
				bw.close();
				prodid++;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] arg){
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		load();
	}
	
}
