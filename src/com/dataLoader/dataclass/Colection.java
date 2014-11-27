package com.dataLoader.dataclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Colection implements Serializable {

	private String name = "";
	private List<Product> productList = new ArrayList<Product>();
	private List<String> imageUrlList = new ArrayList<String>();
	private String imageUrl;
	private String opcja;
	private String imageFileName;
	private String categoryName;
	
	public Colection(){
		
	}

	public String getName() {
		return name;
	}
	
	public String getImageUrl() {
	    return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
	    this.imageUrl = imageUrl;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Product> getProductList() {
	    return productList;
	}

	public void setProductList(List<Product> productList) {
	    this.productList = productList;
	}

	public String getOpcja() {
	    return opcja;
	}

	public void setOpcja(String opcja) {
	    this.opcja = opcja;
	}

	public List<String> getImageUrlList() {
	    return imageUrlList;
	}

	public void setImageUrlList(List<String> imageUrlList) {
	    this.imageUrlList = imageUrlList;
	}

	public void setImageFileName(String imageFileName) {
	    this.imageFileName = imageFileName;
	    
	}

	public String getImageFileName() {
	    return imageFileName;
	}

	public String getCategoryName() {
	    return categoryName;
	}

	public void setCategoryName(String categoryName) {
	    this.categoryName = categoryName;
	}
	
	
	
	
}
