package com.ampion.poc.WeatherPOC;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenWeatherClient {

	private static String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
	private static String units = "metric";
	private static String API_KEY = "567634b2941bea97d0b52b43ec12a86d";
	  
	public static void weatherForecast(String city, float minTemp) {

		String endPoint = BASE_URL + city + "&appid=" + API_KEY + "&units=" + units;

		JSONArray list;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endPoint)).build();
		HttpResponse<String> response;

		try {

			response = client.send(request, BodyHandlers.ofString());

			// System.out.println("response: " + response.body().toString());
			JSONObject obj = new JSONObject(response.body().toString());
			System.out.println(city.toUpperCase() + " Weather forecast report");
			System.out.println("**********************************");
			
			list = obj.getJSONArray("list");
			
			HashSet<String> dateArray = new HashSet<String>();
			HashSet<String> sunnyDays = new HashSet<String>();
			
			String weatherDesc = new String();
			String temparature = new String();
			String date = new String();

			for (int index = 0; index < 40; index++) {

				JSONObject item = list.getJSONObject(index);
				weatherDesc = item.getJSONArray("weather").getJSONObject(0).get("description").toString();
				JSONObject main = item.getJSONObject("main");
				temparature = main.get("temp").toString();
				Float temp = Float.parseFloat(temparature.toString());

				date = item.get("dt_txt").toString().substring(0, 10);

				if (temp > minTemp) {
					dateArray.add(date);
				}

				if ((!weatherDesc.isEmpty()) && (weatherDesc.equalsIgnoreCase("clear sky"))) {
					sunnyDays.add(date);

				}

			}

			if (dateArray.size() > 0) {

				System.out.println(
						"Number of days in"+city+"  where the temperature is predicated to be above 20 degrees in next 5 days: "
								+ dateArray.size() + "\n");
				System.out.println("Days Below...." + "\n");
				for (String d : dateArray) {
					System.out.println(d);
				}
			} else {
				System.out.println("There is no day with above 20 degrees in next 5 days in Sydney.");

			}

			if (sunnyDays.size() > 0) {

				System.out.println("Number of Sunny days predicated in next 5 days: " + sunnyDays.size() + "\n");
				System.out.println("Days Below...." + "\n");

				for (String sunnyDay : sunnyDays) {
					System.out.println(sunnyDay);
				}
			} else {
				System.out.println("There is no Sunny day in next 5 days.");

			}
			
			System.out.println("**************End of the Report ************");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		OpenWeatherClient.weatherForecast("Sydney", 20);
	}

}



