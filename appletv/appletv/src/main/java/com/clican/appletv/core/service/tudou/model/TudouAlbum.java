package com.clican.appletv.core.service.tudou.model;

import java.io.Serializable;

public class TudouAlbum extends ListView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -625063128618386467L;
	
	private Integer size;
	private Integer year;
	private String description;
	private Long playtims;
	private String actors;
	private String directors;
	private Integer area;
	private Integer cid;
	private Long albumid;
	private Integer isfull;
	private Integer hd;
	private Integer isalbum;
	private Integer currentSize;
	private String itemBigPic;
	private Object[] items;
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPlaytims() {
		return playtims;
	}
	public void setPlaytims(Long playtims) {
		this.playtims = playtims;
	}
	
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public Integer getArea() {
		return area;
	}
	public void setArea(Integer area) {
		this.area = area;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Long getAlbumid() {
		return albumid;
	}
	public void setAlbumid(Long albumid) {
		this.albumid = albumid;
	}
	public Integer getIsfull() {
		return isfull;
	}
	public void setIsfull(Integer isfull) {
		this.isfull = isfull;
	}
	public Integer getHd() {
		return hd;
	}
	public void setHd(Integer hd) {
		this.hd = hd;
	}
	public Integer getIsalbum() {
		return isalbum;
	}
	public void setIsalbum(Integer isalbum) {
		this.isalbum = isalbum;
	}
	public Integer getCurrentSize() {
		return currentSize;
	}
	public void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}
	public String getItemBigPic() {
		return itemBigPic;
	}
	public void setItemBigPic(String itemBigPic) {
		this.itemBigPic = itemBigPic;
	}
	public Object[] getItems() {
		return items;
	}
	public void setItems(Object[] items) {
		this.items = items;
	}
    
	
    	
	
}
