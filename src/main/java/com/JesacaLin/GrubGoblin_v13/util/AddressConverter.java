package com.JesacaLin.GrubGoblin_v13.util;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Utility class for converting an address to geographic coordinates.
 */
public class AddressConverter {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final String API_Key = DOTENV.get("MY_GOOGLE_API_KEY");
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_Key).maxRetries(3).build();

    /**
     * Converts an address to geographic coordinates using the Google Geocoding API.
     *
     * @param address the address to convert
     * @return a double array with two elements: the latitude and the longitude
     * @throws IllegalArgumentException if the API key is not set
     * @throws RuntimeException if an error occurs during geocoding
     */
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
