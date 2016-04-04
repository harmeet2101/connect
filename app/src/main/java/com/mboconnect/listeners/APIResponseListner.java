package com.mboconnect.listeners;

import com.tenpearls.android.network.CustomHttpResponse;

public interface APIResponseListner {

	void onSuccess (CustomHttpResponse response);

	void onFailure (CustomHttpResponse response);
}
