package com.szrjk.entity;

public class ImageInfo implements Comparable<ImageInfo>{
	
	private String _id;
	private String album;
	private String albumArt;
	private String albumKey;
	private String artist;
	private String numOfSongs;
	

	public String get_id() {
		return _id;
	}



	public void set_id(String _id) {
		this._id = _id;
	}



	public String getAlbum() {
		return album;
	}



	public void setAlbum(String album) {
		this.album = album;
	}



	public String getAlbumArt() {
		return albumArt;
	}



	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}



	public String getAlbumKey() {
		return albumKey;
	}



	public void setAlbumKey(String albumKey) {
		this.albumKey = albumKey;
	}



	public String getArtist() {
		return artist;
	}



	public void setArtist(String artist) {
		this.artist = artist;
	}



	public String getNumOfSongs() {
		return numOfSongs;
	}



	public void setNumOfSongs(String numOfSongs) {
		this.numOfSongs = numOfSongs;
	}



	
	
	
	@Override
	public String toString() {
		return "ImageInfo [_id=" + _id + ", album=" + album + ", albumArt="
				+ albumArt + ", albumKey=" + albumKey + ", artist=" + artist
				+ ", numOfSongs=" + numOfSongs + "]";
	}



	@Override
	public int compareTo(ImageInfo arg0) {
		return this._id.compareTo(arg0.get_id());
	}

	
}
