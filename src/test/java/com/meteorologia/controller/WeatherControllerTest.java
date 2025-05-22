package com.meteorologia.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class WeatherControllerTest {

    @Test
    public void testHealthEndpoint() {
        given()
            .when().get("/api/weather/health")
            .then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("service", is("Meteorologia Mock API"))
                .body("version", is("1.0.0"))
                .body("timestamp", notNullValue());
    }

    @Test
    public void testGetCurrentWeatherByStationId() {
        given()
            .when().get("/api/weather/current/STATION_001")
            .then()
                .statusCode(200)
                .body("station_id", is("STATION_001"))
                .body("city", is("Madrid"))
                .body("country", is("España"))
                .body("temperature", notNullValue())
                .body("humidity", notNullValue())
                .body("measurement_time", notNullValue());
    }

    @Test
    public void testGetCurrentWeatherByInvalidStationId() {
        given()
            .when().get("/api/weather/current/INVALID_STATION")
            .then()
                .statusCode(404)
                .body("error", is("Estación no encontrada"));
    }

    @Test
    public void testGetCurrentWeatherByCoordinates() {
        given()
            .param("lat", 40.4168)
            .param("lon", -3.7038)
            .when().get("/api/weather/current")
            .then()
                .statusCode(200)
                .body("latitude", is(40.4168f))
                .body("longitude", is(-3.7038f))
                .body("temperature", notNullValue())
                .body("humidity", notNullValue());
    }

    @Test
    public void testGetCurrentWeatherByInvalidCoordinates() {
        given()
            .param("lat", 91.0) // Latitud inválida
            .param("lon", -3.7038)
            .when().get("/api/weather/current")
            .then()
                .statusCode(400)
                .body("error", is("Coordenadas fuera de rango válido"));
    }

    @Test
    public void testGetCurrentWeatherMissingCoordinates() {
        given()
            .param("lat", 40.4168)
            // Falta el parámetro lon
            .when().get("/api/weather/current")
            .then()
                .statusCode(400)
                .body("error", is("Latitud y longitud son requeridas"));
    }

    @Test
    public void testGetForecastByStationId() {
        given()
            .param("days", 5)
            .when().get("/api/weather/forecast/STATION_001")
            .then()
                .statusCode(200)
                .body("size()", is(5))
                .body("[0].date", notNullValue())
                .body("[0].max_temperature", notNullValue())
                .body("[0].min_temperature", notNullValue());
    }

    @Test
    public void testGetForecastByInvalidStationId() {
        given()
            .param("days", 3)
            .when().get("/api/weather/forecast/INVALID_STATION")
            .then()
                .statusCode(404)
                .body("error", is("Estación no encontrada"));
    }

    @Test
    public void testGetForecastByCoordinates() {
        given()
            .param("lat", 40.4168)
            .param("lon", -3.7038)
            .param("days", 7)
            .when().get("/api/weather/forecast")
            .then()
                .statusCode(200)
                .body("size()", is(7))
                .body("[0].date", notNullValue())
                .body("[0].weather_description", notNullValue());
    }

    @Test
    public void testGetForecastInvalidDaysParameter() {
        given()
            .param("days", 15) // Más de 10 días
            .when().get("/api/weather/forecast/STATION_001")
            .then()
                .statusCode(400)
                .body("error", is("El número de días debe estar entre 1 y 10"));
    }

    @Test
    public void testGetForecastDefaultDays() {
        // Sin especificar parámetro days (debería usar 5 por defecto)
        given()
            .when().get("/api/weather/forecast/STATION_001")
            .then()
                .statusCode(200)
                .body("size()", is(5));
    }
}