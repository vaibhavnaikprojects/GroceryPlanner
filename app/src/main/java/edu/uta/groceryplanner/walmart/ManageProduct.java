package edu.uta.groceryplanner.walmart;

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
	private static void fetchJSONData(int productId) {
		try {
			String restURL = "http://api.walmartlabs.com/v1/items/"+String.valueOf(productId)+"?&format=json&apiKey=mbqtt78en6jgfpzmuyj6ab5s";
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
			System.out.println(e.getMessage());
		}
	}
	
	public static ProductBean getProduct(int productId) throws ParseException {
		fetchJSONData(productId);
		ProductBean p = new ProductBean();
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject)parser.parse(data);
		
		p.setProductTypeId((String)obj.get("categoryNode"));
		//p.setProductId((String)obj.get("itemId"));
		//p.setProductName((String)obj.get("name"));
		p.setCost((Double)obj.get("salePrice"));
		
		return p;
	}
}
