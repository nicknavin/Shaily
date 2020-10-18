package com.app.session.interfaces;

import org.json.JSONObject;

public interface RequestCallback {

	public void onSuccess(JSONObject js, String success);
	public void onFailed(JSONObject js, String failed, int status);
	public void onNull(JSONObject js, String nullp);
	public void onException(JSONObject js, String exception);
}
