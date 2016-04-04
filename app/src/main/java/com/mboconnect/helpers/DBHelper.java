package com.mboconnect.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mboconnect.entities.Meta;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.entities.Profile;

import java.util.ArrayList;

/**
 * Created by ali.mehmood on 7/31/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final int	DATABASE_VERSION		   = 1;
	private static final String	DATABASE_NAME			   = "MboConnectDB";
	public static final String	TABLE_OPPORTUNITY		   = "opportunity";
	public static final String	TABLE_FAVORITE_OPPORTUNITY = "favorite_opportunity";
	public static final String	TABLE_META				   = "meta";
	public static final String	TABLE_PROFILE			   = "profile";
	private static final int	META_ID					   = 123;
	static DBHelper				dbHelper;

	public synchronized static DBHelper getInstance (Context context) {

		if (dbHelper == null) {

			dbHelper = new DBHelper (context);
		}

		return dbHelper;
	}

	private DBHelper (Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate (SQLiteDatabase db) {

		createOpportunityTable (db);
		createFavoriteOpportunityTable (db);
		createMetaTable (db);
		createProfileTable(db);
	}

	private void createOpportunityTable (SQLiteDatabase db) {

		String CREATE_BOOK_TABLE = "CREATE TABLE opportunity (opportunity_id TEXT PRIMARY KEY, start_date INTEGER, end_date INTEGER, created INTEGER, title TEXT, " + "description TEXT, company_name TEXT, company_id TEXT, company_image_url TEXT, company_title TEXT, company_industry TEXT, company_phone TEXT," + "company_email TEXT, is_company_active INTEGER, is_company_mbo_certified INTEGER, is_company_email_verified INTEGER, min_rate INTEGER, " + "max_rate INTEGER, min_type TEXT, max_type TEXT, status TEXT, skill_json TEXT, is_favorite INTEGER, is_responded INTEGER, is_accepted INTEGER, " + "duration TEXT, address TEXT, company_address TEXT, author TEXT, company_info_visible INTEGER, company_mobile TEXT)";
		db.execSQL(CREATE_BOOK_TABLE);
	}

	private void createFavoriteOpportunityTable (SQLiteDatabase db) {

		String CREATE_BOOK_TABLE = "CREATE TABLE favorite_opportunity (opportunity_id TEXT PRIMARY KEY, start_date INTEGER, end_date INTEGER, created INTEGER, title TEXT, " + "description TEXT, company_name TEXT, company_id TEXT, company_image_url TEXT, company_title TEXT, company_industry TEXT, company_phone TEXT," + "company_email TEXT, is_company_active INTEGER, is_company_mbo_certified INTEGER, is_company_email_verified INTEGER, min_rate INTEGER, " + "max_rate INTEGER, min_type TEXT, max_type TEXT, status TEXT, skill_json TEXT, is_favorite INTEGER, is_responded INTEGER, is_accepted INTEGER, " + "duration TEXT, address TEXT, company_address TEXT, author TEXT, company_info_visible INTEGER, company_mobile TEXT)";
		db.execSQL (CREATE_BOOK_TABLE);
	}

	private void createMetaTable (SQLiteDatabase db) {

		String CREATE_BOOK_TABLE = "CREATE TABLE meta (id INTEGER PRIMARY KEY, all_opportunities_count INTEGER, favorite_opportunities_count INTEGER, messages_count INTEGER)";
		db.execSQL (CREATE_BOOK_TABLE);
	}

	private void createProfileTable (SQLiteDatabase db) {

		String CREATE_BOOK_TABLE = "CREATE TABLE profile (user_id TEXT PRIMARY KEY, first_name TEXT, last_name TEXT, designation TEXT, image_url TEXT, email TEXT, phone TEXT, summary TEXT, skills TEXT, resume TEXT, education TEXT, image_data BLOB, mobile TEXT, preferences TEXT, preferred_location TEXT)";
		db.execSQL (CREATE_BOOK_TABLE);
	}

	public void addOpportunity (Opportunity opportunity) {

		if (getOpportunityById (opportunity.getOpportunityId (), TABLE_OPPORTUNITY) != null) {

			updateOpportunity (opportunity);
			return;
		}
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues ();
		values.put ("opportunity_id", opportunity.getOpportunityId());
		values.put ("start_date", opportunity.getStartDate());
		values.put ("end_date", opportunity.getEndDate());
		values.put ("created", opportunity.getCreated());
		values.put ("title", opportunity.getTitle());
		values.put ("description", opportunity.getDescription());
		values.put ("company_name", opportunity.getCompanyName());
		values.put ("company_id", opportunity.getCompanyId());
		values.put ("company_image_url", opportunity.getCompanyImgUrl());
		values.put ("company_title", opportunity.getCompanyTitle());
		values.put ("company_industry", opportunity.getCompanyIndustry());
		values.put ("company_phone", opportunity.getCompanyPhone());
		values.put ("company_email", opportunity.getCompanyEmail ());
		values.put ("is_company_active", (opportunity.getIsCompanyActive() ? 1 : 0));
		values.put ("is_company_mbo_certified", (opportunity.getIsCompanyMboCertified() ? 1 : 0));
		values.put ("is_company_email_verified", (opportunity.getIsCompanyEmailVerified () ? 1 : 0));
		values.put ("min_rate", opportunity.getMinRate());
		values.put ("max_rate", opportunity.getMaxRate());
		values.put ("min_type", opportunity.getMinType());
		values.put ("max_type", opportunity.getMaxType());
		values.put ("status", opportunity.getStatus());
		values.put ("skill_json", opportunity.getSkillsJson ());
		values.put ("is_favorite", (opportunity.isFavorite() ? 1 : 0));
		values.put ("is_responded", (opportunity.isResponded() ? 1 : 0));
		values.put ("is_accepted", (opportunity.isAccepted () ? 1 : 0));
		values.put ("duration", opportunity.getDuration());
		values.put ("address", opportunity.getAddressJson());
		values.put ("company_address", opportunity.getCompanyAddressJson());
		values.put ("author", opportunity.getAuthorJson());
		values.put ("company_info_visible", opportunity.isCompanyInfoVisible());
		values.put("company_mobile", opportunity.getCompanyMobile());

		// 3. insert
		db.insert(TABLE_OPPORTUNITY, null, values);

		// 4. close
		// db.close ();
	}

	public void updateOpportunity (Opportunity opportunity) {

		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues ();
		values.put ("opportunity_id", opportunity.getOpportunityId ());
		values.put ("start_date", opportunity.getStartDate ());
		values.put ("end_date", opportunity.getEndDate ());
		values.put ("created", opportunity.getCreated ());
		values.put ("title", opportunity.getTitle ());
		values.put ("description", opportunity.getDescription ());
		values.put ("company_name", opportunity.getCompanyName ());
		values.put ("company_id", opportunity.getCompanyId ());
		values.put ("company_image_url", opportunity.getCompanyImgUrl ());
		values.put ("company_title", opportunity.getCompanyTitle ());
		values.put ("company_industry", opportunity.getCompanyIndustry ());
		values.put ("company_phone", opportunity.getCompanyPhone ());
		values.put ("company_email", opportunity.getCompanyEmail ());
		values.put ("is_company_active", (opportunity.getIsCompanyActive () ? 1 : 0));
		values.put ("is_company_mbo_certified", (opportunity.getIsCompanyMboCertified () ? 1 : 0));
		values.put ("is_company_email_verified", (opportunity.getIsCompanyEmailVerified () ? 1 : 0));
		values.put ("min_rate", opportunity.getMinRate ());
		values.put ("max_rate", opportunity.getMaxRate ());
		values.put ("min_type", opportunity.getMinType ());
		values.put ("max_type", opportunity.getMaxType ());
		values.put ("status", opportunity.getStatus ());
		values.put ("skill_json", opportunity.getSkillsJson ());
		values.put ("is_favorite", (opportunity.isFavorite () ? 1 : 0));
		values.put ("is_responded", (opportunity.isResponded () ? 1 : 0));
		values.put ("is_accepted", (opportunity.isAccepted () ? 1 : 0));
		values.put ("duration", opportunity.getDuration ());
		values.put ("address", opportunity.getAddressJson ());
		values.put ("company_address", opportunity.getCompanyAddressJson ());
		values.put ("author", opportunity.getAuthorJson ());
		values.put ("company_info_visible", opportunity.isCompanyInfoVisible ());
		values.put ("company_mobile", opportunity.getCompanyMobile ());

		// 3. updating row
		db.update (TABLE_OPPORTUNITY, values, "opportunity_id" + " = ?", new String[] { String.valueOf (opportunity.getOpportunityId ()) });
		db.update(TABLE_FAVORITE_OPPORTUNITY, values, "opportunity_id" + " = ?", new String[]{String.valueOf(opportunity.getOpportunityId())});

		// 4. close
		// db.close ();
	}

	public void addFavoriteOpportunity (Opportunity opportunity) {

		if (getOpportunityById (opportunity.getOpportunityId (), TABLE_FAVORITE_OPPORTUNITY) != null) {

			updateOpportunity (opportunity);
			return;
		}

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues ();
		values.put ("opportunity_id", opportunity.getOpportunityId());
		values.put ("start_date", opportunity.getStartDate());
		values.put ("end_date", opportunity.getEndDate());
		values.put ("created", opportunity.getCreated());
		values.put ("title", opportunity.getTitle());
		values.put ("description", opportunity.getDescription());
		values.put ("company_name", opportunity.getCompanyName());
		values.put ("company_id", opportunity.getCompanyId());
		values.put ("company_image_url", opportunity.getCompanyImgUrl());
		values.put ("company_title", opportunity.getCompanyTitle());
		values.put ("company_industry", opportunity.getCompanyIndustry());
		values.put ("company_phone", opportunity.getCompanyPhone());
		values.put ("company_email", opportunity.getCompanyEmail ());
		values.put ("is_company_active", (opportunity.getIsCompanyActive() ? 1 : 0));
		values.put ("is_company_mbo_certified", (opportunity.getIsCompanyMboCertified() ? 1 : 0));
		values.put ("is_company_email_verified", (opportunity.getIsCompanyEmailVerified () ? 1 : 0));
		values.put ("min_rate", opportunity.getMinRate());
		values.put ("max_rate", opportunity.getMaxRate());
		values.put ("min_type", opportunity.getMinType());
		values.put ("max_type", opportunity.getMaxType());
		values.put ("status", opportunity.getStatus());
		values.put ("skill_json", opportunity.getSkillsJson ());
		values.put ("is_favorite", (opportunity.isFavorite() ? 1 : 0));
		values.put ("is_responded", (opportunity.isResponded() ? 1 : 0));
		values.put ("is_accepted", (opportunity.isAccepted () ? 1 : 0));
		values.put ("duration", opportunity.getDuration());
		values.put ("address", opportunity.getAddressJson());
		values.put ("company_address", opportunity.getCompanyAddressJson());
		values.put ("author", opportunity.getAuthorJson());
		values.put ("company_info_visible", opportunity.isCompanyInfoVisible());
		values.put("company_mobile", opportunity.getCompanyMobile());

		// 3. insert
		db.insert(TABLE_FAVORITE_OPPORTUNITY, null, values);

		// 4. close
		// db.close ();
	}

	public void addMeta (Meta meta) {

		if (getMetaById () != null) {

			return;
		}

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues ();
		values.put ("id", META_ID);
		values.put ("all_opportunities_count", meta.getAllOpportunitiesCount ());
		values.put ("favorite_opportunities_count", meta.getFavoriteOpportunitiesCount ());
		values.put ("messages_count", meta.getMessagesCount ());

		// 3. insert

		db.insert(TABLE_META, null, values);

		// 4. close
		// db.close ();
	}

	public void updateMeta (Meta meta) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues ();

		if (meta.getAllOpportunitiesCount () != -1) {

			values.put ("all_opportunities_count", meta.getAllOpportunitiesCount ());
		}

		if (meta.getFavoriteOpportunitiesCount () != -1) {

			values.put ("favorite_opportunities_count", meta.getFavoriteOpportunitiesCount ());
		}

		if (meta.getMessagesCount () != -1) {

			values.put ("messages_count", meta.getMessagesCount ());
		}

		if (values.size () > 0) {

			db.update (TABLE_META, values, "id" + " = ?", new String[] { String.valueOf (META_ID) });
		}
	}

	public void deleteAllRows (String table) {

		SQLiteDatabase db = this.getWritableDatabase ();
		db.delete (table, null, null);
		// db.close ();
	}

	public Profile getProfile () {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase ();

		String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;

		Cursor cursor = null;
		Profile profile = null;

		try{
			// 2. build query
			cursor = db.rawQuery (selectQuery, null);

			// 3. if we got results get the first one
			if (cursor != null && cursor.getCount () > 0) {
				cursor.moveToFirst ();

				profile = new Profile ();
				copyProfile (cursor, profile);
			}

		}finally {

			if(cursor != null){

				cursor.close();
			}
		}

		return profile;
	}

	public void addProfile (Profile profile) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues ();
		values.put ("user_id", profile.getUserId ());
		values.put ("first_name", profile.getFirstName ());
		values.put ("last_name", profile.getLastName ());
		values.put ("designation", profile.getDesignation ());
		values.put ("image_url", profile.getImageURL ());
		values.put ("email", profile.getEmail ());
		values.put ("phone", profile.getPhone ());
		values.put ("summary", profile.getSummary ());
		values.put ("skills", profile.getSkillsJson ());
		values.put ("resume", profile.getResume ());
		values.put ("education", profile.getAcademicsJson ());
		values.put ("image_data", profile.getImageData ());
		values.put ("mobile", profile.getMobile ());
		values.put ("preferences", profile.getPreferenceJson ());
		values.put ("preferred_location", profile.getPreferredLocation ());

		if (getProfileById (profile.getUserId ()) != null) {

			db.update (TABLE_PROFILE, values, "user_id" + " = ?", new String[] { String.valueOf (profile.getUserId ()) });
		}
		else {

			// 3. insert
			db.insert (TABLE_PROFILE, null, values);
		}

		// 4. close
		// db.close ();
	}

	public Profile getProfileById (String id) {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase ();

		String selectQuery = "SELECT  * FROM " + TABLE_PROFILE + " WHERE user_id = '" + id + "'";

		Cursor cursor = null;
		Profile profile = null;

		try{

			// 2. build query
			cursor = db.rawQuery (selectQuery, null);

			// 3. if we got results get the first one
			if (cursor != null && cursor.getCount () > 0) {
				cursor.moveToFirst ();

				profile = new Profile ();
				copyProfile(cursor, profile);
			}

		}finally {

			if(cursor != null){

				cursor.close();
			}
		}

		return profile;
	}

	public Opportunity getOpportunityById (String id, String table) {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase ();

		String selectQuery = "SELECT  * FROM " + table + " WHERE opportunity_id = '" + id + "'";

		Cursor cursor = null;
		Opportunity opportunity = null;

		try{

			// 2. build query
			cursor = db.rawQuery (selectQuery, null);

			// 3. if we got results get the first one
			if (cursor != null && cursor.getCount () > 0) {
				cursor.moveToFirst ();

				opportunity = new Opportunity ();
				copyOpportunity(cursor, opportunity);
			}

		}finally {

			if(cursor != null){

				cursor.close();
			}
		}

		return opportunity;
	}

	public Meta getMetaById () {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase ();

		String selectQuery = "SELECT  * FROM " + TABLE_META + " WHERE id = " + META_ID;

		Cursor cursor = null;
		Meta meta = null;

		try{

			// 2. build query
			cursor = db.rawQuery (selectQuery, null);

			// 3. if we got results get the first one
			if (cursor != null && cursor.getCount () > 0) {
				cursor.moveToFirst ();

				meta = new Meta ();
				copyMeta (cursor, meta);
			}

		}finally {

			if(cursor != null){

				cursor.close();
			}
		}

		return meta;
	}

	public ArrayList<Opportunity> getAllOpportunities (String table) {

		ArrayList<Opportunity> list = new ArrayList<Opportunity> ();

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase ();

		String selectQuery = "SELECT  * FROM " + table;

		Cursor cursor = null;
		Opportunity opportunity = null;

		try{

			// 2. build query
			cursor = db.rawQuery (selectQuery, null);

			if (cursor != null && cursor.getCount () > 0) {

				cursor.moveToFirst ();

				do {
					opportunity = new Opportunity ();
					copyOpportunity (cursor, opportunity);
					list.add (opportunity);

				} while (cursor.moveToNext ());
			}

		}finally {

			if(cursor != null){

				cursor.close();
			}
		}

		return list;
	}

	public void deleteOpportunityById (String id) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. delete
		int i = db.delete (TABLE_OPPORTUNITY, "opportunity_id = ?", new String[] { String.valueOf (id) });

		db.delete (TABLE_FAVORITE_OPPORTUNITY, "opportunity_id = ?", new String[] { String.valueOf (id) });

		// 3. close
	}

	public void deleteOpportunityById (String id, String table) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase ();

		// 2. delete
		int i = db.delete (table, "opportunity_id = ?", new String[] { String.valueOf (id) });

		// 3. close
	}

	private void copyOpportunity (Cursor cursor, Opportunity opportunity) {

		opportunity.setOpportunityId (cursor.getString (0));
		opportunity.setStartDate (Long.parseLong (cursor.getString (1)));
		opportunity.setEndDate (Long.parseLong (cursor.getString (2)));
		opportunity.setCreated (Long.parseLong (cursor.getString (3)));
		opportunity.setTitle (cursor.getString (4));
		opportunity.setDescription (cursor.getString (5));
		opportunity.setCompanyName (cursor.getString (6));
		opportunity.setCompanyId (cursor.getString (7));
		opportunity.setCompanyImgUrl (cursor.getString (8));
		opportunity.setCompanyTitle (cursor.getString (9));
		opportunity.setCompanyIndustry (cursor.getString (10));
		opportunity.setCompanyPhone (cursor.getString (11));
		opportunity.setCompanyEmail (cursor.getString (12));
		opportunity.setIsCompanyActive (Integer.parseInt (cursor.getString (13)) == 1 ? true : false);
		opportunity.setIsCompanyMboCertified (Integer.parseInt (cursor.getString (14)) == 1 ? true : false);
		opportunity.setIsCompanyEmailVerified (Integer.parseInt (cursor.getString (15)) == 1 ? true : false);
		opportunity.setMinRate (Integer.parseInt (cursor.getString (16)));
		opportunity.setMaxRate (Integer.parseInt (cursor.getString (17)));
		opportunity.setMinType (cursor.getString (18));
		opportunity.setMaxType (cursor.getString (19));
		opportunity.setStatus (cursor.getString (20));
		opportunity.setSkillsJson (cursor.getString (21));
		opportunity.setIsFavorite (Integer.parseInt (cursor.getString (22)) == 1 ? true : false);
		opportunity.setIsResponded (Integer.parseInt (cursor.getString (23)) == 1 ? true : false);
		opportunity.setIsAccepted (Integer.parseInt (cursor.getString (24)) == 1 ? true : false);
		opportunity.setDuration (cursor.getString (25));
		opportunity.setAddressJson (cursor.getString (26));
		opportunity.setCompanyAddressJson (cursor.getString (27));
		opportunity.setAuthorJson (cursor.getString (28));
		opportunity.setCompanyInfoVisible (Integer.parseInt (cursor.getString (29)) == 1 ? true : false);
		opportunity.setCompanyMobile (cursor.getString (30));
	}

	private void copyMeta (Cursor cursor, Meta meta) {

		meta.setAllOpportunitiesCount (Integer.parseInt (cursor.getString (1)));
		meta.setFavoriteOpportunitiesCount (Integer.parseInt (cursor.getString (2)));
		meta.setMessagesCount (Integer.parseInt (cursor.getString (3)));
	}

	private void copyProfile (Cursor cursor, Profile profile) {

		profile.setUserId (cursor.getString (0));
		profile.setFirstName (cursor.getString (1));
		profile.setLastName (cursor.getString (2));
		profile.setDesignation (cursor.getString (3));
		profile.setImageURL (cursor.getString (4));
		profile.setEmail (cursor.getString (5));
		profile.setPhone (cursor.getString (6));
		profile.setSummary (cursor.getString (7));
		profile.setSkillsJson (cursor.getString (8));
		profile.setResume (cursor.getString (9));
		profile.setAcademicsJson (cursor.getString (10));
		profile.setImageData (cursor.getBlob (11));
		profile.setMobile (cursor.getString (12));
		profile.setPreferenceJson (cursor.getString (13));
		profile.setPreferredLocation (cursor.getString (14));
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older books table if existed
		db.execSQL ("DROP TABLE IF EXISTS opportunity");
		db.execSQL ("DROP TABLE IF EXISTS favorite_opportunity");
		db.execSQL ("DROP TABLE IF EXISTS meta");

		// create fresh books table
		this.onCreate (db);
	}

}