package edu.uta.groceryplanner.walmart;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uta.groceryplanner.beans.ProductBean;

public class ManageProduct {
	static String data;
	private static void fetchJSONData(String productId) {
		try {
			String restURL = "http://api.walmartlabs.com/v1/search?query="+productId+"&format=json&apiKey=mbqtt78en6jgfpzmuyj6ab5s";
			System.out.println(restURL);
			URL walmartUrl = new URL(restURL);
			HttpURLConnection con = (HttpURLConnection)walmartUrl.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			if(con.getResponseCode() == 200) {
				InputStream is = con.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader buff = new BufferedReader(isr);
				String str=null;
				StringBuilder stringBuilder = new StringBuilder();
				while((str=buff.readLine()) != null) {
					stringBuilder.append(str);
				}
				data=stringBuilder.toString();
			}else {
				throw new RuntimeException("Failed to connect to walmart REST service"+con.getResponseCode()+con.getResponseMessage());
			}
		}catch(Exception e) {
			Log.d("walmart REST error",e.getMessage());
		}
	}

	public static ProductBean getProduct(String productId) throws ParseException {
		fetchJSONData(productId);
		ProductBean p = new ProductBean();
		JSONParser parser = new JSONParser();
		Object object = parser.parse(data);
		JSONObject jsonObject = (JSONObject)object;

		JSONArray arr = (JSONArray)jsonObject.get("items");
		Object object1 = (Object)arr.get(0);
		JSONObject obj = (JSONObject)object1;
		p.setProductTypeId((String)obj.get("categoryNode"));
		p.setProductId(String.valueOf(obj.get("itemId")));
		p.setProductName((String)obj.get("name"));
		p.setCost((Double)obj.get("salePrice"));
		p.setProductTypeName((String)obj.get("categoryPath"));
		return p;
	}
}
