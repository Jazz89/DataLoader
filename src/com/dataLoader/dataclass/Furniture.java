package com.dataLoader.dataclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Furniture implements Serializable {

	public Furniture(){
		
	}
	
	private List<Category> furnitureList = new ArrayList<Category>();

	public List<Category> getFurnitureList() {
		return furnitureList;
	}

	public void setFurnitureList(List<Category> furnitureList) {
		this.furnitureList = furnitureList;
	}
	
	
	
}
