package com.mboconnect.entities;

/**
 * Created by tahir on 16/07/15.
 */
public class SearchResults extends BaseEntity {

	private int searchResults;

	public SearchResults (int searchNumber) {

		this.searchResults = searchNumber;

	}

	@Override
	public void set (String jsonString) {

	}

	public int getSearchResults () {

		return searchResults;
	}

	public void setSearchResults (int searchResults) {

		this.searchResults = searchResults;
	}
}
