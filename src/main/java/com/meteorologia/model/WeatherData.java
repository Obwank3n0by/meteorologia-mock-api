package com.meteorologia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(name = "WeatherData", description = "Datos meteorológicos actuales")
public class WeatherData {

    @Schema(description = "Identificador único de la estación meteorológica", example = "STATION_001")
    @JsonProperty("station_id")
    @NotNull
    private String stationId;

    @Schema(description = "Nombre de la ciudad", example = "Madrid")
    @NotNull
    private String city;

    @Schema(description = "País", example = "España")
    @NotNull
    private String country;

    @Schema(description = "Latitud de la ubicación", example = "40.4168")
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90 grados")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90 grados")
    private Double latitude;

    @Schema(description = "Longitud de la ubicación", example = "-3.7038")
    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180 grados")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180 grados")
    private Double longitude;

    @Schema(description = "Temperatura actual en grados Celsius", example = "23.5")
    private Double temperature;

    @Schema(description = "Sensación térmica en grados Celsius", example = "25.2")
    @JsonProperty("feels_like")
    private Double feelsLike;

    @Schema(description = "Humedad relativa en porcentaje", example = "65")
    @DecimalMin(value = "0", message = "La humedad debe estar entre 0 y 100")
    @DecimalMax(value = "100", message = "La humedad debe estar entre 0 y 100")
    private Integer humidity;

    @Schema(description = "Presión atmosférica en hPa", example = "1013.25")
    private Double pressure;

    @Schema(description = "Velocidad del viento en km/h", example = "15.3")
    @JsonProperty("wind_speed")
    private Double windSpeed;

    @Schema(description = "Dirección del viento en grados", example = "180")
    @JsonProperty("wind_direction")
    @DecimalMin(value = "0", message = "La dirección del viento debe estar entre 0 y 359 grados")
    @DecimalMax(value = "359", message = "La dirección del viento debe estar entre 0 y 359 grados")
    private Integer windDirection;

    @Schema(description = "Descripción del viento", example = "Sur")
    @JsonProperty("wind_description")
    private String windDescription;

    @Schema(description = "Visibilidad en kilómetros", example = "10.0")
    private Double visibility;

    @Schema(description = "Índice UV", example = "5")
    @JsonProperty("uv_index")
    @DecimalMin(value = "0", message = "El índice UV debe ser mayor o igual a 0")
    @DecimalMax(value = "11", message = "El índice UV debe ser menor o igual a 11")
    private Integer uvIndex;

    @Schema(description = "Descripción del clima", example = "Parcialmente nublado")
    @JsonProperty("weather_description")
    private String weatherDescription;

    @Schema(description = "Código del icono del clima", example = "partly-cloudy")
    @JsonProperty("weather_icon")
    private String weatherIcon;

    @Schema(description = "Fecha y hora de la medición", example = "2025-05-22T14:30:00")
    @JsonProperty("measurement_time")
    private LocalDateTime measurementTime;

    @Schema(description = "Precipitación en las últimas 24 horas en mm", example = "2.5")
    @JsonProperty("precipitation_24h")
    private Double precipitation24h;

    // Constructor vacío
    public WeatherData() {}

    // Constructor completo
    public WeatherData(String stationId, String city, String country, Double latitude, Double longitude,
                      Double temperature, Double feelsLike, Integer humidity, Double pressure,
                      Double windSpeed, Integer windDirection, String windDescription,
                      Double visibility, Integer uvIndex, String weatherDescription,
                      String weatherIcon, LocalDateTime measurementTime, Double precipitation24h) {
        this.stationId = stationId;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.windDescription = windDescription;
        this.visibility = visibility;
        this.uvIndex = uvIndex;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.measurementTime = measurementTime;
        this.precipitation24h = precipitation24h;
    }

    // Getters y Setters
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(Double feelsLike) { this.feelsLike = feelsLike; }

    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }

    public Double getPressure() { return pressure; }
    public void setPressure(Double pressure) { this.pressure = pressure; }

    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }

    public Integer getWindDirection() { return windDirection; }
    public void setWindDirection(Integer windDirection) { this.windDirection = windDirection; }

    public String getWindDescription() { return windDescription; }
    public void setWindDescription(String windDescription) { this.windDescription = windDescription; }

    public Double getVisibility() { return visibility; }
    public void setVisibility(Double visibility) { this.visibility = visibility; }

    public Integer getUvIndex() { return uvIndex; }
    public void setUvIndex(Integer uvIndex) { this.uvIndex = uvIndex; }

    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }

    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }

    public LocalDateTime getMeasurementTime() { return measurementTime; }
    public void setMeasurementTime(LocalDateTime measurementTime) { this.measurementTime = measurementTime; }

    public Double getPrecipitation24h() { return precipitation24h; }
    public void setPrecipitation24h(Double precipitation24h) { this.precipitation24h = precipitation24h; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherData that = (WeatherData) o;
        return Objects.equals(stationId, that.stationId) &&
               Objects.equals(measurementTime, that.measurementTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, measurementTime);
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "stationId='" + stationId + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", temperature=" + temperature +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", measurementTime=" + measurementTime +
                '}';
    }
}