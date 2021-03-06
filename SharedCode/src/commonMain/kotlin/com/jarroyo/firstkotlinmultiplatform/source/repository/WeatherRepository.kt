package com.jarroyo.kotlinmultiplatform.repository

import com.jarroyo.firstkotlinmultiplatform.domain.usecase.weather.getWeatherList.GetWeatherListRequest
import com.jarroyo.kotlinmultiplatform.domain.Response
import com.jarroyo.kotlinmultiplatform.domain.model.CurrentWeather
import com.jarroyo.kotlinmultiplatform.domain.model.Location
import com.jarroyo.kotlinmultiplatform.source.network.WeatherApi

class WeatherRepository(
    private val weatherApi: WeatherApi
) {

    suspend fun getWeather(location: Location): Response<CurrentWeather> {
        return weatherApi.getWeather(location)
    }

    suspend fun getWeatherList(request: GetWeatherListRequest): Response<List<Response<CurrentWeather>>> {

        var list = mutableListOf<Response<CurrentWeather>>()
        for (locationModel in request.locationList) {
            val weather = getWeather(Location(locationModel.city_name))

            list.add(weather)
        }
        return Response.Success(list)
    }
}