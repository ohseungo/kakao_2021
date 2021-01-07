package test;

import java.io.IOException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Problem1 {
	public final static int N = 5;
	public final static int TRUCK_COUNT = 5;
	
	public final static int CYCLE_REQUEST_MEAN = 2;
	public static void main(String[] args) throws IOException, ParseException {
		
		long[][] map = new long[N][N];
		long[][] check = new long[N][N];
		Truck[] trucks = new Truck[TRUCK_COUNT];
		String result = "ready";
		String auth_key = RestAPI.startAPI(1);
		while(result.equals("ready")) {
			map = RestAPI.locationsAPI(N, auth_key);
	
			trucks = RestAPI.trucksAPI(TRUCK_COUNT, auth_key);
			
			boolean b ;
			for (int time = 0; time < 10; time++) {
				
				for (int t = 0 ; t<trucks.length; t++) {
					check = new long[N][N];
					b= trucks[t].findNotEnough(CYCLE_REQUEST_MEAN, N, map, check);
					if (!b) trucks[t].findExceeding(CYCLE_REQUEST_MEAN, N, map, check);
				}
//				System.out.println();
			}

			result = RestAPI.simulateAPI(trucks, auth_key);
		}
		RestAPI.scoreAPI(auth_key);
		
	} 

}
