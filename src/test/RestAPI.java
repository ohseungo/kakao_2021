package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestAPI {
	public static final String AUTH_TOKEN 
					= "1f3a8460f0115c3dff1546b6e8ff1215";
	public static final String BASE_URL 
					= "https://pegkq2svv6.execute-api.ap-northeast-2.amazonaws.com/prod/users";
	
	public static JSONObject call(String auth_key, String subUrl, JSONObject parameter, String type) throws IOException, ParseException {
		
		URL url =null;
		try {
			url = new URL(BASE_URL + subUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(type);
		if (auth_key.equals("")) {
			conn.setRequestProperty("X-Auth-Token", AUTH_TOKEN);
		}else {
			conn.setRequestProperty("Authorization", auth_key);
		}
		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);
		
		
		if (parameter != null) {
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(parameter.toString());	
			osw.flush();
			osw.close();
		}
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String s;
		while(br.ready()) {
			s = br.readLine();
//			System.out.println(s);
			sb.append(s);
		}
		
		JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());
		
		return result;
	}
	
	
	public static String startAPI(int problem) throws IOException, ParseException {
		JSONObject json = new JSONObject();
		json.put("problem", problem);
		
		JSONObject result = call("", "/start", json, "POST");
		String auth_key = (String) result.get("auth_key");
		System.out.println("auth_key : " + auth_key);
		return auth_key;
	}
	
	public static long[][] locationsAPI(int size, String auth_key) throws IOException, ParseException {
		JSONObject result = call(auth_key, "/locations", null, "GET");
		long[][] answer = new long[size][size];
		
		JSONArray array = (JSONArray) result.get("locations");
		
		JSONObject temp;
		
		for (int i = 0; i<size*size; i++) {
			temp = (JSONObject) array.get(i);
			answer[(i%size)][i/size] = (long) temp.get("located_bikes_count");
		}
		
		return answer;
	}
	
	public static Truck[] trucksAPI(int count, String auth_key) throws IOException, ParseException {
		JSONObject result = call(auth_key, "/trucks", null, "GET");
		
		Truck[] trucks = new Truck[count];
		
		JSONArray array = (JSONArray) result.get("trucks");
		JSONObject temp;
		long id, location_id, loaded_bikes_count;
		int idInt;
		for (int i = 0; i<count; i++) {
			temp = (JSONObject) array.get(i);
			id = (long) temp.get("id");
			location_id = (long) temp.get("location_id");
			loaded_bikes_count = (long) temp.get("loaded_bikes_count");
			
			idInt = (int) id;
			trucks[idInt] = new Truck(idInt, (int) location_id, (int) loaded_bikes_count);
		}
		
		return trucks;
	}
	
	public static String simulateAPI(Truck[] trucks, String auth_key) throws IOException, ParseException {
		JSONArray array = new JSONArray();
		JSONArray command;
		JSONObject object;
		for (int t = 0 ; t< trucks.length; t++) {
			object = new JSONObject();
			command = new JSONArray();
			object.put("truck_id", trucks[t].id );
			for (int i=0; i<10; i++) {
				command.add(trucks[t].command[i]);
			}
			object.put("command", command);
			array.add(object);
		}
		object =new JSONObject();
		object.put("commands", array);
		
		JSONObject result = call(auth_key, "/simulate", object, "PUT");
		if (((Long) result.get("time")) % 10 ==0 ) System.out.println(result.toString());
		
		return (String) result.get("status");
	}
	public static void scoreAPI(String auth_key) throws IOException, ParseException {
		JSONObject result = call(auth_key, "/score", null, "GET");
		System.out.println(result.toString());
	}
	
}
