package com.JesacaLin.GrubGoblin_v13.util;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import io.github.cdimascio.dotenv.Dotenv;

public class AddressConverter {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final String API_Key = DOTENV.get("MY_GOOGLE_API_KEY");
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_Key).maxRetries(3).build();
    public static double[] convertAddress(String address) {
        //Check if the API key is set
        if (API_Key == null || API_Key.isEmpty()) {
            throw new IllegalArgumentException("API key is not set!");
        }
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            if (results.length > 0) {
                double latitude = results[0].geometry.location.lat;
                double longitude = results[0].geometry.location.lng;
                return new double[] {latitude, longitude};
            } else {
                throw new Exception("No results found for address: " + address);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting address to coordinates: " + e.getMessage(), e);
        } finally {
            context.shutdown();
        }
    }
}
