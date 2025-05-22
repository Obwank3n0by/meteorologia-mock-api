package com.meteorologia.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class StationsControllerTest {

    @Test
    public void testGetAllStations() {
        given()
            .when().get("/api/stations")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].city", notNullValue());
    }

    @Test
    public void testGetStationById() {
        given()
            .when().get("/api/stations/STATION_001")
            .then()
                .statusCode(200)
                .body("id", is("STATION_001"))
                .body("name", is("Estación Madrid Centro"))
                .body("city", is("Madrid"))
                .body("country", is("España"))
                .body("status", is("ACTIVA"));
    }

    @Test
    public void testGetStationByInvalidId() {
        given()
            .when().get("/api/stations/INVALID_ID")
            .then()
                .statusCode(404)
                .body("error", is("Estación no encontrada"));
    }

    @Test
    public void testGetStationsByCity() {
        given()
            .when().get("/api/stations/search/city/Madrid")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].city", is("Madrid"));
    }

    @Test
    public void testGetStationsByCityNotFound() {
        given()
            .when().get("/api/stations/search/city/CiudadInexistente")
            .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testGetStationsByCountry() {
        given()
            .when().get("/api/stations/search/country/España")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].country", is("España"));
    }

    @Test
    public void testSearchStationsWithFilters() {
        given()
            .param("city", "Madrid")
            .param("status", "ACTIVA")
            .when().get("/api/stations/search")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testSearchStationsWithTypeFilter() {
        given()
            .param("type", "AUTOMATICA")
            .when().get("/api/stations/search")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetStationStatistics() {
        given()
            .when().get("/api/stations/statistics")
            .then()
                .statusCode(200)
                .body("total_stations", greaterThan(0))
                .body("active_stations", notNullValue())
                .body("countries", notNullValue())
                .body("last_updated", notNullValue());
    }

    @Test
    public void testGetNearbyStations() {
        given()
            .param("lat", 40.4168)
            .param("lon", -3.7038)
            .param("radius", 100)
            .param("limit", 5)
            .when().get("/api/stations/nearby")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetNearbyStationsInvalidCoordinates() {
        given()
            .param("lat", 91.0) // Latitud inválida
            .param("lon", -3.7038)
            .when().get("/api/stations/nearby")
            .then()
                .statusCode(400)
                .body("error", is("Coordenadas fuera de rango válido"));
    }

    @Test
    public void testGetNearbyStationsMissingCoordinates() {
        given()
            .param("lat", 40.4168)
            // Falta parámetro lon
            .when().get("/api/stations/nearby")
            .then()
                .statusCode(400)
                .body("error", is("Latitud y longitud son requeridas"));
    }

    @Test
    public void testGetNearbyStationsInvalidRadius() {
        given()
            .param("lat", 40.4168)
            .param("lon", -3.7038)
            .param("radius", 1500) // Radio muy grande
            .when().get("/api/stations/nearby")
            .then()
                .statusCode(400)
                .body("error", is("El radio debe estar entre 0 y 1000 km"));
    }

    @Test
    public void testGetNearbyStationsInvalidLimit() {
        given()
            .param("lat", 40.4168)
            .param("lon", -3.7038)
            .param("limit", 100) // Límite muy alto
            .when().get("/api/stations/nearby")
            .then()
                .statusCode(400)
                .body("error", is("El límite debe estar entre 1 y 50"));
    }

    @Test
    public void testGetNearbyStationsWithDefaults() {
        // Usar valores por defecto para radius y limit
        given()
            .param("lat", 40.4168)
            .param("lon", -3.7038)
            .when().get("/api/stations/nearby")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}