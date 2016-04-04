package com.mboconnect.entities;



/**
 * Created by ali.mehmood on 7/28/2015.
 */
public class Meta {

	private int allOpportunitiesCount;
	private int favoriteOpportunitiesCount;
	private int messagesCount;

	public Meta () {

	}

	public int getAllOpportunitiesCount () {

		return allOpportunitiesCount;
	}

	public int getFavoriteOpportunitiesCount () {

		return favoriteOpportunitiesCount;
	}

	public void setFavoriteOpportunitiesCount (int favoriteOpportunitiesCount) {

		this.favoriteOpportunitiesCount = favoriteOpportunitiesCount;
	}

	public void setAllOpportunitiesCount (int allOpportunitiesCount) {

		this.allOpportunitiesCount = allOpportunitiesCount;
	}

	public int getMessagesCount () {

		return messagesCount;
	}

	public void setMessagesCount (int messagesCount) {

		this.messagesCount = messagesCount;
	}
}
