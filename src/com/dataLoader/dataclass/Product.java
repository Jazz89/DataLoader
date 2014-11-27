/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dataLoader.dataclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;

/**
 *
 * @author admin
 */
@SuppressWarnings("serial")
public class Product implements Serializable {
    private String name;
    private String categoryName;
    private String collectionName;
    private String cena;
    private String opis;
    private String szerokosc;
    private String wysokosc;
    private String glebokosc;
    private String dostawa;
    private String img; 
    private String url;
    private String kod;
    private String opcja;
    private List<String> imageUrlList = new ArrayList<String>();
    private List<String> imgFileNameList = new ArrayList<String>();
    private double waga;
    
    public Product(){
    	
    }
    
    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }

    public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getSzerokosc() {
        return szerokosc;
    }

    public void setSzerokosc(String szerokosc) {
        this.szerokosc = szerokosc;
    }

    public String getWysokosc() {
        return wysokosc;
    }

    public void setWysokosc(String wysokosc) {
        this.wysokosc = wysokosc;
    }

    public String getGlebokosc() {
        return glebokosc;
    }

    public void setGlebokosc(String glebokosc) {
        this.glebokosc = glebokosc;
    }

    public String getDostawa() {
        return dostawa;
    }

    public void setDostawa(String dostawa) {
        this.dostawa = dostawa;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public void addUrlImage(String urlImage){
        imageUrlList.add(urlImage);
    }    

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    
    public void addImageFileName(String imgFileName){
    	imgFileNameList.add(imgFileName);
    }

	public List<String> getImgFileNameList() {
		return imgFileNameList;
	}

	public void setImgFileNameList(List<String> imgFileNameList) {
		this.imgFileNameList = imgFileNameList;
	}

	public void setWaga(String waga) {
	    this.waga = Double.parseDouble(waga);	    
	} 
    
	public double getWaga(){
	    return waga;
	}

	public String getOpcja() {
	    return opcja;
	}

	public void setOpcja(String opcja) {
	    this.opcja = opcja;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.getName().equals(((Product) obj).getName())){
			return true;
		}else{
			return false;
		}
	}
	
}
