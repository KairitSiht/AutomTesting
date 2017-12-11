package tests;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import weather.WeatherDatabaseResult;
import weather.WeatherDatabase;
import java.io.IOException;
import java.net.MalformedURLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Kairit
 */

public class WeatherDatabaseTest {
    private WeatherDatabaseResult weatherDatabaseResult;

    @Before
    public void setUpBeforeEach() {
        weatherDatabaseResult = new WeatherDatabase().writeToFile("Tallinn");
    }

    @Test
    public void
    givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
            throws ClientProtocolException, IOException {

        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet( "http://api.openweathermap.org/data/2.5/forecast?q=Tallinn&appid=ffb9d2b2358292d5736ff280c7c938f0" );

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );

        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals( jsonMimeType, mimeType );
    }

    @Test
    public void getTallinnApi() throws MalformedURLException {
        WeatherDatabase weatherDatabase = new WeatherDatabase();
        weatherDatabase.getWeatherInfo("Tallinn");
    }

    @Test
    public void isTemperatureNotNull() {
        assertNotNull(weatherDatabaseResult.getForecast().get(0));
    }

    @Test
    public void isCityNotNull() {
        assertNotNull(weatherDatabaseResult.getCity().getName());
    }

    @Test
    public void isCountryNotNull() {
        assertNotNull(weatherDatabaseResult.getCity().getCountry());
    }

    @Test
    public void isCityCorrect() {
        assertEquals("Tallinn", weatherDatabaseResult.getCity().getName());
    }

    @Test
    public void isCountryCorrect() {
        assertEquals("EE", weatherDatabaseResult.getCity().getCountry());
    }

    @Test
    public void isLatitudeCorrect() {
        assertEquals(59.437D, weatherDatabaseResult.getCity().getCoordinates().getLatitude(), 0.01);
    }

    @Test
    public void isLongitudeCorrect() {
        assertEquals(24.753D, weatherDatabaseResult.getCity().getCoordinates().getLongitude(), 0.01);
    }

    @Test
    public void areCoordinatesCorrectlyFormatted() {
        assertEquals("59:25", weatherDatabaseResult.getCity().getCoordinates().formatCoordinates());
    }

    @Test
    public void areThereEnoughTemperatureData() {
        assertEquals(40, weatherDatabaseResult.getResultsCount(), 0.01);
    }

    @Test
    public void isMinTemperatureLessOrEqualToCurrent() {
        assertTrue(weatherDatabaseResult.getForecast().get(0).getTemperature().getMinimumTemperature() <=
                weatherDatabaseResult.getForecast().get(0).getTemperature().getTemperature());
    }

    @Test
    public void isMaxTemperatureMoreOrEqualToCurrent() {
        assertTrue(weatherDatabaseResult.getForecast().get(0).getTemperature().getMaximumTemperature() >=
                weatherDatabaseResult.getForecast().get(0).getTemperature().getTemperature());
    }

    @Test(timeout = 100)
    public void testEfficiency() {
        weatherDatabaseResult.getCity().getCoordinates().formatCoordinates();
    }

    @After
    public void setDownAfterEach() {
        weatherDatabaseResult = null;
    }
}