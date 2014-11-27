package com.dataLoader.dataclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Category implements Serializable {

	String name;
	String url;
	Category parentCategory;
	List<Colection> colectionList= new ArrayList<Colection>();
	List<Product> productList= new ArrayList<Product>();
	List<Category> subCateogryList = new ArrayList<Category>();
	
	

	public Category(){
		
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	public void addProduct(Product product){
		this.productList.add(product);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Colection> getColectionList() {
	    return colectionList;
	}

	public void setColectionList(List<Colection> colectionList) {
	    this.colectionList = colectionList;
	}
	
	public void addCollection(Colection collection){
	    colectionList.add(collection);
	}
	
	public List<Category> getSubCateogryList() {
		return subCateogryList;
	}

	public void setSubCateogryList(List<Category> subCateogryList) {
		this.subCateogryList = subCateogryList;
	}
	
	@Override
	public boolean equals(Object colection){
	    if (colection == null) return false;
	    if (colection == this) return true;
	    if (!(colection instanceof Colection))return false;
	    Colection myColection = (Colection)colection;
	    if(this.getName()==myColection.getName()){
		return true;
	    }
	    else{
		return false;
	    }
	}
	
	
}
