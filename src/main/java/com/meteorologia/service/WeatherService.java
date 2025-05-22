package com.meteorologia.service;

import com.meteorologia.model.WeatherData;
import com.meteorologia.model.WeatherForecast;
import com.meteorologia.model.WeatherStation;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class WeatherService {

    private final Random random = new Random();
    private final List<WeatherStation> mockStations;
    private final List<String> weatherConditions;
    private final List<String> weatherIcons;
    private final List<String> windDirections;

    public WeatherService() {
        this.weatherConditions = Arrays.asList(
            "Soleado", "Parcialmente nublado", "Nublado", "Lluvia ligera",
            "Lluvia", "Tormenta", "Niebla", "Viento fuerte", "Granizo", "Nieve"
        );
        
        this.weatherIcons = Arrays.asList(
            "sunny", "partly-cloudy", "cloudy", "light-rain",
            "rain", "thunderstorm", "fog", "windy", "hail", "snow"
        );
        
        this.windDirections = Arrays.asList(
            "Norte", "Noreste", "Este", "Sureste",
            "Sur", "Suroeste", "Oeste", "Noroeste"
        );
        
        this.mockStations = initializeMockStations();
    }

    private List<WeatherStation> initializeMockStations() {
        return Arrays.asList(
            new WeatherStation("STATION_001", "Estación Madrid Centro", "Madrid", "España", "Comunidad de Madrid",
                40.4168, -3.7038, 650, "Europe/Madrid", "AUTOMATICA", "ACTIVA",
                LocalDateTime.of(2020, 1, 15, 10, 0), LocalDateTime.now(), "AEMET", "contacto@aemet.es",
                "Estación automática ubicada en el centro urbano"),
            
            new WeatherStation("STATION_002", "Estación Barcelona Puerto", "Barcelona", "España", "Cataluña",
                41.3851, 2.1734, 12, "Europe/Madrid", "AUTOMATICA", "ACTIVA",
                LocalDateTime.of(2019, 6, 10, 9, 30), LocalDateTime.now(), "AEMET", "contacto@aemet.es",
                "Estación costera del puerto de Barcelona"),
            
            new WeatherStation("STATION_003", "Estación Sevilla Aeropuerto", "Sevilla", "España", "Andalucía",
                37.4162, -5.8961, 34, "Europe/Madrid", "MIXTA", "ACTIVA",
                LocalDateTime.of(2018, 3, 5, 14, 15), LocalDateTime.now(), "AEMET", "contacto@aemet.es",
                "Estación del aeropuerto de Sevilla"),
            
            new WeatherStation("STATION_004", "Estación Bilbao Ciudad", "Bilbao", "España", "País Vasco",
                43.2630, -2.9349, 19, "Europe/Madrid", "AUTOMATICA", "ACTIVA",
                LocalDateTime.of(2021, 9, 12, 11, 45), LocalDateTime.now(), "AEMET", "contacto@aemet.es",
                "Estación urbana de Bilbao"),
            
            new WeatherStation("STATION_005", "Estación Valencia Playa", "Valencia", "España", "Comunidad Valenciana",
                39.4699, -0.3763, 11, "Europe/Madrid", "AUTOMATICA", "MANTENIMIENTO",
                LocalDateTime.of(2017, 11, 20, 16, 20), LocalDateTime.now().minusDays(5), "AEMET", "contacto@aemet.es",
                "Estación costera de Valencia")
        );
    }

    public List<WeatherStation> getAllStations() {
        return new ArrayList<>(mockStations);
    }

    public Optional<WeatherStation> getStationById(String id) {
        return mockStations.stream()
            .filter(station -> station.getId().equals(id))
            .findFirst();
    }

    public List<WeatherStation> getStationsByCity(String city) {
        return mockStations.stream()
            .filter(station -> station.getCity().toLowerCase().contains(city.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<WeatherStation> getStationsByCountry(String country) {
        return mockStations.stream()
            .filter(station -> station.getCountry().toLowerCase().contains(country.toLowerCase()))
            .collect(Collectors.toList());
    }

    public WeatherData getCurrentWeather(String stationId) {
        Optional<WeatherStation> station = getStationById(stationId);
        if (station.isEmpty()) {
            return null;
        }

        WeatherStation ws = station.get();
        return generateMockWeatherData(ws);
    }

    public WeatherData getCurrentWeatherByLocation(Double latitude, Double longitude) {
        // Buscar la estación más cercana
        WeatherStation nearestStation = findNearestStation(latitude, longitude);
        return generateMockWeatherDataForLocation(nearestStation, latitude, longitude);
    }

    public List<WeatherForecast> getForecast(String stationId, int days) {
        Optional<WeatherStation> station = getStationById(stationId);
        if (station.isEmpty()) {
            return Collections.emptyList();
        }

        return IntStream.range(1, days + 1)
            .mapToObj(i -> generateMockForecast(LocalDate.now().plusDays(i)))
            .collect(Collectors.toList());
    }

    public List<WeatherForecast> getForecastByLocation(Double latitude, Double longitude, int days) {
        return IntStream.range(1, days + 1)
            .mapToObj(i -> generateMockForecast(LocalDate.now().plusDays(i)))
            .collect(Collectors.toList());
    }

    private WeatherStation findNearestStation(Double lat, Double lon) {
        return mockStations.stream()
            .min(Comparator.comparingDouble(station -> 
                calculateDistance(lat, lon, station.getLatitude(), station.getLongitude())))
            .orElse(mockStations.get(0));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private WeatherData generateMockWeatherData(WeatherStation station) {
        return generateMockWeatherDataForLocation(station, station.getLatitude(), station.getLongitude());
    }

    private WeatherData generateMockWeatherDataForLocation(WeatherStation station, Double lat, Double lon) {
        double baseTemp = getBaseTemperatureForLocation(lat);
        double temperature = baseTemp + (random.nextGaussian() * 5);
        double feelsLike = temperature + (random.nextGaussian() * 2);
        
        int weatherIndex = random.nextInt(weatherConditions.size());
        int windDirIndex = random.nextInt(8);
        
        return new WeatherData(
            station.getId(),
            station.getCity(),
            station.getCountry(),
            lat,
            lon,
            Math.round(temperature * 10.0) / 10.0,
            Math.round(feelsLike * 10.0) / 10.0,
            30 + random.nextInt(70), // Humedad 30-100%
            950.0 + random.nextDouble() * 100, // Presión 950-1050 hPa
            random.nextDouble() * 30, // Viento 0-30 km/h
            windDirIndex * 45, // Dirección del viento
            windDirections.get(windDirIndex),
            5.0 + random.nextDouble() * 15, // Visibilidad 5-20 km
            random.nextInt(12), // UV index 0-11
            weatherConditions.get(weatherIndex),
            weatherIcons.get(weatherIndex),
            LocalDateTime.now(),
            random.nextDouble() * 10 // Precipitación 0-10mm
        );
    }

    private WeatherForecast generateMockForecast(LocalDate date) {
        double baseTemp = 15 + random.nextGaussian() * 10;
        double maxTemp = baseTemp + random.nextDouble() * 10;
        double minTemp = baseTemp - random.nextDouble() * 10;
        
        int weatherIndex = random.nextInt(weatherConditions.size());
        int windDirIndex = random.nextInt(8);
        
        return new WeatherForecast(
            date,
            Math.round(maxTemp * 10.0) / 10.0,
            Math.round(minTemp * 10.0) / 10.0,
            40 + random.nextInt(60), // Humedad 40-100%
            random.nextInt(101), // Probabilidad precipitación 0-100%
            random.nextDouble() * 20, // Cantidad precipitación 0-20mm
            random.nextDouble() * 25, // Viento 0-25 km/h
            windDirIndex * 45,
            windDirections.get(windDirIndex),
            random.nextInt(12), // UV index
            weatherConditions.get(weatherIndex),
            weatherIcons.get(weatherIndex),
            String.format("%02d:%02d", 6 + random.nextInt(2), random.nextInt(60)), // Amanecer 6:00-7:59
            String.format("%02d:%02d", 19 + random.nextInt(3), random.nextInt(60)) // Atardecer 19:00-21:59
        );
    }

    private double getBaseTemperatureForLocation(Double latitude) {
        // Temperatura base según latitud (aproximación muy simple)
        double absLat = Math.abs(latitude);
        if (absLat < 23.5) return 25; // Trópicos
        else if (absLat < 40) return 18; // Subtropicales
        else if (absLat < 60) return 10; // Templadas
        else return 0; // Polares
    }

    public Map<String, Object> getStationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_stations", mockStations.size());
        stats.put("active_stations", mockStations.stream()
            .mapToLong(s -> "ACTIVA".equals(s.getStatus()) ? 1 : 0).sum());
        stats.put("inactive_stations", mockStations.stream()
            .mapToLong(s -> "INACTIVA".equals(s.getStatus()) ? 1 : 0).sum());
        stats.put("maintenance_stations", mockStations.stream()
            .mapToLong(s -> "MANTENIMIENTO".equals(s.getStatus()) ? 1 : 0).sum());
        stats.put("countries", mockStations.stream()
            .map(WeatherStation::getCountry).distinct().count());
        stats.put("last_updated", LocalDateTime.now());
        return stats;
    }
}