package joc113_SpotifyKnockoff;

import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Provides Methods For:
 * 1. Creating Song Objects from scratch
 * 2. Creating Objects that relate to existing database records
 * 3. Updating the database as needed
 * @author Josh Chamberlain
 * @version 1.1
 */
public class Song {
	private String songID;
	private String title;
	private double length;
	private String filePath;
	private String releaseDate;
	private String recordDate;
	Map<String, Artist> songArtists;
	
	/**
	 * Constructor used to create a new Song object
	 * @param title
	 * @param length
	 * @param releaseDate
	 * @param recordDate
	 */
	public Song(String title, double length, String releaseDate, String recordDate){
		this.title = title;
		this.length = length;
		this.releaseDate = releaseDate;
		this.recordDate = recordDate;
		this.songID = UUID.randomUUID().toString();
		
		//We need to instantiate the value for this Hashtable so we can use it in the addArtist method
		//The reason my method wasn't working before is because I was adding to a Hashtable that did not exist
		//Now it is automatically made any time the constructor is called
		songArtists = new Hashtable<String, Artist>();
		
		// System.out.println(this.songID);
		// String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date,fk_album_id) ";
		// sql += "VALUES ('" + this.songID + "', '" + this.title + "', " + this.length + ", '', '" + this.releaseDate + "', '" + this.recordDate + "', '" + this.albumID + "');";
		String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date) ";
		sql += "VALUES (?, ?, ?, ?, ?, ?);";
		
		//System.out.println(sql);
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2,  this.title);
			ps.setDouble(3, this.length);
			ps.setString(4, "");
			ps.setString(5, this.releaseDate);
			ps.setString(6, this.recordDate);
			ps.executeUpdate();
			db.closeDbConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Another constructor for when we don't want to have to open and close a connection for each time we call it
	 * @param songID
	 * @param title
	 * @param length
	 * @param releaseDate
	 * @param recordDate
	 */
	//This makes it so that we can just use the data that we already have access to without creating and closing connections continuously
	public Song(String songID, String title, double length, String releaseDate, String recordDate){
		this.songID = songID;
		this.title = title;
		this.length = length;
		this.releaseDate = releaseDate;
		this.recordDate = recordDate;
		//this.songID = UUID.randomUUID().toString();
		
		//We need to instantiate the value for this Hashtable so we can use it in the addArtist method
		//The reason my method wasn't working before is because I was adding to a Hashtable that did not exist
		//Now it is automatically made any time the constructor is called
		songArtists = new Hashtable<String, Artist>();
		
		// System.out.println(this.songID);
		// String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date,fk_album_id) ";
		// sql += "VALUES ('" + this.songID + "', '" + this.title + "', " + this.length + ", '', '" + this.releaseDate + "', '" + this.recordDate + "', '" + this.albumID + "');";
		String sql = "INSERT INTO song (song_id,title,length,file_path,release_date,record_date,fk_album_id) ";
		sql += "VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		//System.out.println(sql);
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2,  this.title);
			ps.setDouble(3, this.length);
			ps.setString(4, "");
			ps.setString(5, this.releaseDate);
			ps.setString(6, this.recordDate);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This creates an Object by pulling frmo the database of existing records
	 * @param songID
	 */
	//Retrieve an existing song object from the database based on the songID
	public Song(String songID){
		//We need to instantiate the value for this Hashtable so we can use it in the addArtist method
		songArtists = new Hashtable<String, Artist>();
		String sql = "SELECT * FROM song WHERE song_id = '" + songID + "';";
		// System.out.println(sql);
		DbUtilities db = new DbUtilities();
		try {
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				this.songID = rs.getString("song_id");
				// System.out.println("Song ID from database: " + this.songID);
				this.title = rs.getString("title");
				this.releaseDate = rs.getDate("release_date").toString();
				this.recordDate = rs.getDate("record_date").toString();
				this.length = rs.getDouble("length");
				// System.out.println("Song title from database: " + this.title);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/**
	 * This deletes a song instance in the database by referencing its primary key
	 * @param songID
	 */
	//Deletes song instance in database
	public void deleteSong(String songID) {
		//Create instance of Hashtable
		songArtists = new Hashtable<String, Artist>();

		//sql statement
		String sql = "DELETE FROM song WHERE song_id = ?;";
		
		//Import String into MySQL
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps;
			ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds to the song's artists in the junction table and the Hashtable
	 * @param artist
	 */
	//adds an artist to the song's list of artists by accepting an Artist object
	public void addArtist(Artist artist) {
		songArtists = new Hashtable<String, Artist>();
		//Put artist into the Hashtable 
		songArtists.put(artist.getArtistID(), artist);
		
		//SQL Statement
		String sql = "INSERT INTO song_artist (fk_song_id, fk_artist_id) VALUES (?, ?);";
		
		//Import the SQL statement using prepared statement
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2, artist.getArtistID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Delete an Artist from the Hashtable and the junction table in the database
	 * References Artist's artistID
	 * @param artistID
	 */
	//deletes an artist from a song's list of artists using that artist's artistID
	public void deleteArtist(String artistID) {
		//Create object out of the ID first, so it can be removed
		Artist artist = new Artist(artistID);
		songArtists = new Hashtable<String, Artist>();
		songArtists.remove(artist.getArtistID(), artist);
		//sql statement
		String sql = "DELETE FROM song_artist WHERE fk_artist_id = ?;";
		
		try {
			//Create database object to pass the sql statement
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, artistID);
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Delete from the junction table and the Hashtable by referencing the object itself
	 * @param artist
	 */
	public void deleteArtist(Artist artist) {
		//instantiate songArtists as Hashtable, then remove the object passed from the Hashtable
		songArtists = new Hashtable<String, Artist>();
		songArtists.remove(artist.getArtistID(), artist);
		
		//SQL delete statement
String sql = "DELETE FROM song_artist WHERE fk_artist_id = ?;";
		
		try {
			//Create database object to pass the sql statement
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, artist.getArtistID());
			db.closeDbConnection();
			db = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFilePath(String filePath) {
		//It's not enough to just set the class property. We also need to update it in the database
		this.filePath = filePath;
		
		//SQL statement
		String sql = "UPDATE song SET file_path = ? WHERE song_id = ?;";
		//Import SQL statement using prepared statement
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			//update the filepath for the specified song
			ps.setString(1,  this.filePath);
			ps.setString(2, this.songID);
			ps = conn.prepareStatement(sql);
			db.closeDbConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * Getter for songID
 * @return
 */
	public String getSongID() {
		return songID;
	}

	//Have not used any of these methods yet, but took them from the code example.
	//Will hold onto them for now, in case we need them later
	public String getReleaseDate() {
		return releaseDate;
	}


	public String getTitle() {
		return title;
	}

	public double getLength() {
		return length;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public Map<String, Artist> getSongArtists() {
		return songArtists;
	}
	
	//I started on a method that was supposed to check if the artist existed in the database already, and would not add if it was unnecessary
	//Found out that the unnecessary thing was this method
	//Keeping for now because it may be useful later
	/*
	public void addArtist(String artist) {
		//Create new DbUtilities
		DbUtilities db = new DbUtilities();
		//Check if the artist already exists
		String sql = "SELECT * FROM artist WHERE first_name + last_name = '" + artist + "' OR"
				+ " band_name = '" + artist + "';";
		try {
			ResultSet rs = db.getResultSet(sql);
			//
			if (rs == null){
				Artist newArtist = new Artist(artist);
				songArtists.put(artist, newArtist);
			}
			
			else {
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		*/


}