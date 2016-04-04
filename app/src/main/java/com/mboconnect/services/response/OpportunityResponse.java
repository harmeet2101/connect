package com.mboconnect.services.response;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.Application;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.helpers.DBHelper;
import com.tenpearls.android.utilities.JsonUtility;

/**
 * Created by ali.mehmood on 6/22/2015.
 */
public class OpportunityResponse extends BaseResponse<Opportunity> {

	private OpportunityMeta meta;
	private final String    KEY_PAGE_SIZE    = "page_size";
	private final String    KEY_TOTAL        = "total";
	private final String    KEY_TOTAL_ACTIVE = "total_active";
	private String          url;

	public OpportunityResponse (String url) {

		this.url = url;
	}

	@Override
	public void set (String input) {

		try {

			JsonObject jsonObject = JsonUtility.parseToJsonObject (input);
			JsonObject metaObject = jsonObject.getAsJsonObject (KEY_META);
			JsonArray jsonArray = jsonObject.getAsJsonArray (KEY_DATA);

			meta = new OpportunityMeta ();
			meta.setPageSize (JsonUtility.getInt (metaObject, KEY_PAGE_SIZE));
			meta.setTotal (JsonUtility.getInt (metaObject, KEY_TOTAL));
			meta.setTotalActive (JsonUtility.getInt (metaObject, KEY_TOTAL_ACTIVE));

			for (int i = 0; i < jsonArray.size (); i++) {

				Opportunity opportunity = new Opportunity ();
				opportunity.set (jsonArray.get (i).toString ());
				list.add (opportunity);

				if (url.contains ("favorite")) {

					DBHelper.getInstance (Application.getInstance ()).addFavoriteOpportunity (opportunity);
				}
				else {

					DBHelper.getInstance (Application.getInstance ()).addOpportunity (opportunity);
				}
			}
		}
		catch (Exception e) {

			e.printStackTrace ();
		}
	}

	public OpportunityMeta getMeta () {

		return meta;
	}

	public class OpportunityMeta {

		int total;
		int pageSize;
		int totalActive;

		public int getTotal () {

			return total;
		}

		public void setTotal (int total) {

			this.total = total;
		}

		public int getPageSize () {

			return pageSize;
		}

		public void setPageSize (int pageSize) {

			this.pageSize = pageSize;
		}

		public int getTotalActive () {

			return totalActive;
		}

		public void setTotalActive (int totalActive) {

			this.totalActive = totalActive;
		}
	}
}
