package com.wolff.wardrobe.yahooWeather;

/**
 * Created by wolff on 24.03.2017.
 */

public interface YahooWeatherInfoListener {
    void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType);
}

