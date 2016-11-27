package tools;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public AsyncHttpClient getClient() {
		return client;
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(Context context, String url, HttpEntity entity,
			AsyncHttpResponseHandler responseHandler) {
		client.setConnectTimeout(3000);
		client.setTimeout(3000);
		client.post(context, getAbsoluteUrl(url), entity, "application/json",
				responseHandler);
	}
	
	public static void postWithLongTimeOut(Context context, String url, HttpEntity entity,
			AsyncHttpResponseHandler responseHandler) {
		client.setConnectTimeout(999999);
		client.setTimeout(999999);
		client.post(context, getAbsoluteUrl(url), entity, "application/json",
				responseHandler);
	}

	public static void postOne(Context context, String url, HttpEntity entity,
			AsyncHttpResponseHandler responseHandler) {

		client.post(context, url, entity, "application/json", responseHandler);
	}

	public static void get(Context context, String url, String data,
			AsyncHttpResponseHandler responseHandler) {
		client.get(context, url + data, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return Config.BASE_URL + relativeUrl;
	}
}
