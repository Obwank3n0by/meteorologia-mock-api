package com.meteorologia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.time.LocalDate;
import java.util.Objects;

@Schema(name = "WeatherForecast", description = "Pronóstico meteorológico diario")
public class WeatherForecast {

    @Schema(description = "Fecha del pronóstico", example = "2025-05-23")
    @NotNull
    private LocalDate date;

    @Schema(description = "Temperatura máxima en grados Celsius", example = "28.5")
    @JsonProperty("max_temperature")
    private Double maxTemperature;

    @Schema(description = "Temperatura mínima en grados Celsius", example = "15.2")
    @JsonProperty("min_temperature")
    private Double minTemperature;

    @Schema(description = "Humedad promedio en porcentaje", example = "70")
    @DecimalMin(value = "0", message = "La humedad debe estar entre 0 y 100")
    @DecimalMax(value = "100", message = "La humedad debe estar entre 0 y 100")
    private Integer humidity;

    @Schema(description = "Probabilidad de precipitación en porcentaje", example = "30")
    @JsonProperty("precipitation_probability")
    @DecimalMin(value = "0", message = "La probabilidad debe estar entre 0 y 100")
    @DecimalMax(value = "100", message = "La probabilidad debe estar entre 0 y 100")
    private Integer precipitationProbability;

    @Schema(description = "Precipitación esperada en mm", example = "5.2")
    @JsonProperty("precipitation_amount")
    private Double precipitationAmount;

    @Schema(description = "Velocidad del viento en km/h", example = "12.5")
    @JsonProperty("wind_speed")
    private Double windSpeed;

    @Schema(description = "Dirección del viento en grados", example = "225")
    @JsonProperty("wind_direction")
    @DecimalMin(value = "0", message = "La dirección del viento debe estar entre 0 y 359 grados")
    @DecimalMax(value = "359", message = "La dirección del viento debe estar entre 0 y 359 grados")
    private Integer windDirection;

    @Schema(description = "Descripción del viento", example = "Suroeste")
    @JsonProperty("wind_description")
    private String windDescription;

    @Schema(description = "Índice UV máximo", example = "7")
    @JsonProperty("uv_index")
    @DecimalMin(value = "0", message = "El índice UV debe ser mayor o igual a 0")
    @DecimalMax(value = "11", message = "El índice UV debe ser menor o igual a 11")
    private Integer uvIndex;

    @Schema(description = "Descripción del clima", example = "Parcialmente nublado con chubascos ocasionales")
    @JsonProperty("weather_description")
    private String weatherDescription;

    @Schema(description = "Código del icono del clima", example = "partly-cloudy-rain")
    @JsonProperty("weather_icon")
    private String weatherIcon;

    @Schema(description = "Hora del amanecer", example = "06:45")
    private String sunrise;

    @Schema(description = "Hora del atardecer", example = "20:30")
    private String sunset;

    // Constructor vacío
    public WeatherForecast() {}

    // Constructor completo
    public WeatherForecast(LocalDate date, Double maxTemperature, Double minTemperature,
                          Integer humidity, Integer precipitationProbability, Double precipitationAmount,
                          Double windSpeed, Integer windDirection, String windDescription,
                          Integer uvIndex, String weatherDescription, String weatherIcon,
                          String sunrise, String sunset) {
        this.date = date;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.humidity = humidity;
        this.precipitationProbability = precipitationProbability;
        this.precipitationAmount = precipitationAmount;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.windDescription = windDescription;
        this.uvIndex = uvIndex;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    // Getters y Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getMaxTemperature() { return maxTemperature; }
    public void setMaxTemperature(Double maxTemperature) { this.maxTemperature = maxTemperature; }

    public Double getMinTemperature() { return minTemperature; }
    public void setMinTemperature(Double minTemperature) { this.minTemperature = minTemperature; }

    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }

    public Integer getPrecipitationProbability() { return precipitationProbability; }
    public void setPrecipitationProbability(Integer precipitationProbability) { 
        this.precipitationProbability = precipitationProbability; 
    }

    public Double getPrecipitationAmount() { return precipitationAmount; }
    public void setPrecipitationAmount(Double precipitationAmount) { 
        this.precipitationAmount = precipitationAmount; 
    }

    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }

    public Integer getWindDirection() { return windDirection; }
    public void setWindDirection(Integer windDirection) { this.windDirection = windDirection; }

    public String getWindDescription() { return windDescription; }
    public void setWindDescription(String windDescription) { this.windDescription = windDescription; }

    public Integer getUvIndex() { return uvIndex; }
    public void setUvIndex(Integer uvIndex) { this.uvIndex = uvIndex; }

    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }

    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }

    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }

    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherForecast that = (WeatherForecast) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "date=" + date +
                ", maxTemperature=" + maxTemperature +
                ", minTemperature=" + minTemperature +
                ", weatherDescription='" + weatherDescription + '\'' +
                '}';
    }
}