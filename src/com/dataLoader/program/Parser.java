package com.dataLoader.program;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import com.dataLoader.dataclass.Category;
import com.dataLoader.dataclass.Furniture;
import com.dataLoader.dataclass.Product;
import com.thoughtworks.xstream.XStream;

public abstract class Parser {
	
	public static final String XML_FILE_NAME = "E://git//DataLoader//DataLoader//xml//Categories.xml";

	public static void objectToXML(final List<Category> categryList, final File file) throws IOException{
		XStream xstream = new XStream();	
		Furniture furniture = new Furniture();
		furniture.setFurnitureList(categryList);
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xstream.toXML(furniture, writer);		
	}
	
	public void printProductInfo(Product product){
		System.out.println(" --- " + product.getName());
		System.out.println(" ------ " + product.getUrl());
		System.out.println(" ------ " + product.getCategoryName());
		System.out.println(" ------ " + product.getCollectionName());
		System.out.println(" ------ " + product.getWysokosc() + " "
				+ product.getGlebokosc() + " "
				+ product.getSzerokosc());
		System.out.println(" ------ " + product.getImageUrlList());
		System.out.println(" ------ " + product.getCena());
		System.out.println(" ------ " + product.getOpis());
		System.out.println(" ------ " + product.getDostawa());
	}
	
	abstract void parseUrl(String url) throws IOException;
	abstract Product parseProductUrl(String url) throws IOException;
	abstract List<Category> parseCategories(String url) throws IOException;
	abstract void test() throws IOException;
	
}
