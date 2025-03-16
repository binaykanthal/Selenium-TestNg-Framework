package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

	public static Response sendGetRequest(String endpoint) {
		return RestAssured.get(endpoint);
	}

	public static Response sendPostRequest(String endpoint, String payLoad) {
		return RestAssured.given().header("Content-Type", "application/json")
				.body(payLoad)
				.post();
	}
	
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;
	}
	
	public static String getJsonValue(Response response, String value ) {
		return response.jsonPath().getString(value);
	}
}
