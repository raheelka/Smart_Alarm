package com.example.socialctxt_alarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;

public class transitSystemInformation extends Activity {

	public static HashMap<String, String> bus_info = new HashMap<String, String>();

	/**
	 * @param args
	 */
	static long user_set_time;

	public static void getBusDetails(String src, String dest, String time) {

		/*
		 * @SuppressWarnings("unused") String bus_information = "";
		 */
		Calendar c = Calendar.getInstance();
		String arr[] = time.split("-");

		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr[0]));
		c.set(Calendar.MINUTE, Integer.parseInt(arr[1]));
		TimeZone tz = TimeZone.getDefault();
		c.setTimeZone(tz);
		user_set_time = c.getTimeInMillis() / 1000;

		String st = "http://maps.googleapis.com/maps/api/directions/json?origin="
				+ src
				+ "&destination="
				+ dest
				+ "&sensor=false&departure_time="
				+ (c.getTimeInMillis() / 1000) + "&mode=transit";
		String st2 = st.replaceAll(" ", "%20");
		String origin = src;
		String destination = dest;
		bus_info.put("origin", origin.replaceAll(" ", "%20"));
		bus_info.put("destination", destination.replaceAll(" ", "%20"));
		if (transit_info_fetch(st2).get("bus_info") != null)
			bus_info.putAll(transit_info_fetch(st2));
		else
			bus_info.clear();
		/*
		 * if (bus_info.get("bus_info") != null) { bus_information =
		 * bus_info.get("bus_info") + "\nBus Number:" +
		 * bus_info.get("bus_number") + "\nDeparture Time:" +
		 * bus_info.get("departure_time") + "\nArrival Time:" +
		 * bus_info.get("arrival_time") + "\nDuration Time:" +
		 * bus_info.get("duration_time"); } else bus_information =
		 * "Bus not Found";
		 */
	}

	public static Map<String, String> transit_info_fetch(String url) {
		HashMap<String, String> bus_info_temp = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(((HttpURLConnection) (new URL(url))
							.openConnection()).getInputStream(), Charset
							.forName("UTF-8")));
			String jsonresponse;
			final StringBuilder builder = new StringBuilder(4096);
			while ((jsonresponse = reader.readLine()) != null) {
				builder.append(jsonresponse);
			}
			JSONObject json = new JSONObject(builder.toString());
			JSONArray route_array = json.getJSONArray("routes");
			for (int i = 0; i < route_array.length(); i++) {
				JSONObject route_array_object = route_array.getJSONObject(i);
				JSONArray legs_array = route_array_object.getJSONArray("legs");
				for (int y = 0; y < legs_array.length(); y++) {
					JSONObject legs_array_object = legs_array.getJSONObject(y);
					JSONArray steps_array = legs_array_object
							.getJSONArray("steps");
					for (int z = 0; z < steps_array.length(); z++) {
						JSONObject steps_array_object = steps_array
								.getJSONObject(z);
						String html_inst = steps_array_object.getString(
								"html_instructions").toString();
						if (html_inst.contains("Bus towards")) {
							JSONObject duration = (JSONObject) steps_array_object
									.get("duration");
							String duration_time = duration.getString("text");
							JSONObject transit_details_object = (JSONObject) steps_array_object
									.get("transit_details");
							JSONObject line = (JSONObject) transit_details_object
									.get("line");
							JSONObject departure_time_object = (JSONObject) transit_details_object
									.get("departure_time");
							String bus_number = line.getString("name") + ":"
									+ line.getString("short_name");
							JSONObject arrival_time_object = (JSONObject) transit_details_object
									.get("arrival_time");
							long arr_time_value = Long
									.parseLong(arrival_time_object
											.getString("value"));
							long dept_time_value = Long
									.parseLong(departure_time_object
											.getString("value"));
							// only the departure time of the first connecting
							// bus saved
							if (bus_info_temp.get("departure_time_value") != null) {
								if (Long.parseLong(bus_info_temp
										.get("departure_time_value")) > dept_time_value) {
									bus_info_temp.put("departure_time_value",
											Long.toString(dept_time_value));
									bus_info_temp.put("departure_time",
											departure_time_object
													.getString("text"));
								}
							} else {
								bus_info_temp.put("departure_time_value",
										Long.toString(dept_time_value));
								bus_info_temp
										.put("departure_time",
												departure_time_object
														.getString("text"));
							}
							// only the departure time of the first connecting
							// bus saved

							// only the arrival time of the last connecting bus
							// saved
							if (bus_info_temp.get("arrival_time_value") != null) {
								if (Long.parseLong(bus_info_temp
										.get("arrival_time_value")) < arr_time_value) {
									bus_info_temp.put("arrival_time_value",
											Long.toString(arr_time_value));
									bus_info_temp.put("arrival_time",
											arrival_time_object
													.getString("text"));
								}
							} else {
								bus_info_temp.put("arrival_time_value",
										Long.toString(arr_time_value));
								bus_info_temp.put("arrival_time",
										arrival_time_object.getString("text"));
							}
							// only the arrival time of the last connecting bus
							// saved

							if (bus_info_temp.get("bus_number") != null)
								bus_info_temp.put("bus_number",
										bus_info_temp.get("bus_number") + " & "
												+ bus_number);
							else
								bus_info_temp.put("bus_number", bus_number);

							if (bus_info_temp.get("duration_time") != null) {
								String[] st1 = bus_info_temp.get(
										"duration_time").split(" ");
								String[] st2 = duration_time.split(" ");
								String dur_time = Integer.toString((Integer
										.parseInt(st1[0]) + Integer
										.parseInt(st2[0])))
										+ " mins";
								bus_info_temp.put("duration_time", dur_time);
							}

							else
								bus_info_temp.put("duration_time",
										duration_time);

							if (bus_info_temp.get("bus_info") != null)
								bus_info_temp
										.put("bus_info",
												bus_info_temp.get("bus_info")
														+ " & "
														+ steps_array_object
																.getString("html_instructions"));
							else
								bus_info_temp
										.put("bus_info", steps_array_object
												.getString("html_instructions"));
							/*System.out.println(steps_array_object
									.getString("html_instructions"));
							System.out.println("Departure Time:"
									+ departure_time_object.getString("text")
									+ ":"
									+ departure_time_object.getString("value"));
							System.out.println("Journey Duration:"
									+ duration_time);
							System.out.println("Bus Number:" + bus_number);*/
						}
					}
				}
			}
		} catch (JSONException e) {
			//
			e.printStackTrace();
		} catch (MalformedURLException e) {
			//
			e.printStackTrace();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		return bus_info_temp;
	}

	public long check_transit_system_and_update_alarm(long bus_search_unix_time) {
		long alarm_reset_time = 0;
		long function_call_time = System.currentTimeMillis() / 1000 + 3600;
		long reduced_time = 0;
		boolean bus_found = false;
		String st1 = "http://maps.googleapis.com/maps/api/directions/json?origin=";
		String st2 = "&destination=";
		String st3 = "&sensor=false&departure_time=";
		String st4 = "&mode=transit";

		while (!bus_found
				&& (function_call_time < bus_search_unix_time - reduced_time)) {
			long info_fetch_time = bus_search_unix_time - reduced_time;
			if (!(bus_info.get("origin") == null)
					&& !(bus_info.get("destination") == null)) {
				String url = st1 + bus_info.get("origin").toString() + st2
						+ bus_info.get("destination").toString() + st3
						+ info_fetch_time + st4;
				HashMap<String, String> bus_info_temp = new HashMap<String, String>();
				bus_info_temp.putAll(transit_info_fetch(url));
				if (!bus_info_temp.isEmpty()) {
					if (bus_info_temp.get("arrival_time_value") != null
							&& bus_info.get("arrival_time_value") != null) {
						if (Long.parseLong(bus_info_temp
								.get("arrival_time_value")) == Long
								.parseLong(bus_info.get("arrival_time_value"))) {
							bus_found = true;
						} else if (Long.parseLong(bus_info_temp
								.get("arrival_time_value")) > Long
								.parseLong(bus_info.get("arrival_time_value"))) {
							reduced_time = reduced_time + 600;
						} else {
							bus_found = true;
							bus_info.putAll(bus_info_temp);
							long bus_dept_time_seconds = Long
									.parseLong(bus_info
											.get("departure_time_value"));
							Calendar currtime = Calendar.getInstance();
							currtime.setTimeInMillis(System.currentTimeMillis());
							long currtime_sec = currtime.getTimeInMillis() / 1000;
							alarm_reset_time = bus_dept_time_seconds
									- MainActivity.buffer_time_to_get_ready
									- currtime_sec;
						}
					}
				}
			}
		}
		return alarm_reset_time;
	}

}
