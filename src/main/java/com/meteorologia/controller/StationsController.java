package com.meteorologia.controller;

import com.meteorologia.model.WeatherStation;
import com.meteorologia.service.WeatherService;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/stations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Stations API", description = "API para gestionar estaciones meteorológicas")
public class StationsController {

    @Inject
    WeatherService weatherService;

    @GET
    @Operation(
        summary = "Obtener todas las estaciones meteorológicas",
        description = "Retorna una lista de todas las estaciones meteorológicas disponibles"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Lista de estaciones obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherStation.class, type = Schema.SchemaType.ARRAY)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response getAllStations() {
        try {
            List<WeatherStation> stations = weatherService.getAllStations();
            return Response.ok(stations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Obtener estación por ID",
        description = "Retorna los detalles de una estación meteorológica específica"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Estación encontrada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherStation.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Estación no encontrada"
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response getStationById(
        @Parameter(
            description = "ID único de la estación meteorológica",
            required = true,
            example = "STATION_001"
        )
        @PathParam("id") String id) {
        
        try {
            Optional<WeatherStation> station = weatherService.getStationById(id);
            if (station.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Estación no encontrada\"}")
                    .build();
            }
            return Response.ok(station.get()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/search/city/{city}")
    @Operation(
        summary = "Buscar estaciones por ciudad",
        description = "Retorna todas las estaciones meteorológicas de una ciudad específica"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Estaciones encontradas exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherStation.class, type = Schema.SchemaType.ARRAY)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Parámetros inválidos"
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response getStationsByCity(
        @Parameter(
            description = "Nombre de la ciudad a buscar",
            required = true,
            example = "Madrid"
        )
        @PathParam("city") String city) {
        
        try {
            if (city == null || city.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El nombre de la ciudad es requerido\"}")
                    .build();
            }
            
            List<WeatherStation> stations = weatherService.getStationsByCity(city);
            return Response.ok(stations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/search/country/{country}")
    @Operation(
        summary = "Buscar estaciones por país",
        description = "Retorna todas las estaciones meteorológicas de un país específico"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Estaciones encontradas exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherStation.class, type = Schema.SchemaType.ARRAY)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Parámetros inválidos"
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response getStationsByCountry(
        @Parameter(
            description = "Nombre del país a buscar",
            required = true,
            example = "España"
        )
        @PathParam("country") String country) {
        
        try {
            if (country == null || country.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El nombre del país es requerido\"}")
                    .build();
            }
            
            List<WeatherStation> stations = weatherService.getStationsByCountry(country);
            return Response.ok(stations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/search")
    @Operation(
        summary = "Buscar estaciones con filtros múltiples",
        description = "Busca estaciones meteorológicas usando múltiples criterios de filtrado"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Búsqueda completada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherStation.class, type = Schema.SchemaType.ARRAY)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response searchStations(
        @Parameter(
            description = "Filtrar por ciudad",
            required = false,
            example = "Madrid"
        )
        @QueryParam("city") String city,
        
        @Parameter(
            description = "Filtrar por país", 
            required = false,
            example = "España"
        )
        @QueryParam("country") String country,
        
        @Parameter(
            description = "Filtrar por estado de la estación",
            required = false,
            example = "ACTIVA"
        )
        @QueryParam("status") String status,
        
        @Parameter(
            description = "Filtrar por tipo de estación",
            required = false,
            example = "AUTOMATICA"
        )
        @QueryParam("type") String type) {
        
        try {
            List<WeatherStation> stations = weatherService.getAllStations();
            
            // Aplicar filtros si están presentes
            if (city != null && !city.trim().isEmpty()) {
                stations = stations.stream()
                    .filter(s -> s.getCity().toLowerCase().contains(city.toLowerCase()))
                    .toList();
            }
            
            if (country != null && !country.trim().isEmpty()) {
                stations = stations.stream()
                    .filter(s -> s.getCountry().toLowerCase().contains(country.toLowerCase()))
                    .toList();
            }
            
            if (status != null && !status.trim().isEmpty()) {
                stations = stations.stream()
                    .filter(s -> s.getStatus().equalsIgnoreCase(status))
                    .toList();
            }
            
            if (type != null && !type.trim().isEmpty()) {
                stations = stations.stream()
                    .filter(s -> s.getStationType().equalsIgnoreCase(type))
                    .toList();
            }
            
            return Response.ok(stations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/statistics")
    @Operation(
        summary = "Obtener estadísticas de estaciones",
        description = "Retorna estadísticas generales sobre las estaciones meteorológicas"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Map.class)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response getStationStatistics() {
        try {
            Map<String, Object> statistics = weatherService.getStationStatistics();
            return Response.ok(statistics).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/nearby")
    @Operation(
        summary = "Buscar estaciones cercanas",
        description = "Encuentra las estaciones meteorológicas más cercanas a una ubicación específica"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Estaciones cercanas encontradas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherStation.class, type = Schema.SchemaType.ARRAY)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Parámetros inválidos"
        ),
        @APIResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Response getNearbyStations(
        @Parameter(
            description = "Latitud en grados decimales",
            required = true,
            example = "40.4168"
        )
        @QueryParam("lat") Double latitude,
        
        @Parameter(
            description = "Longitud en grados decimales",
            required = true,
            example = "-3.7038"
        )
        @QueryParam("lon") Double longitude,
        
        @Parameter(
            description = "Radio de búsqueda en kilómetros",
            required = false,
            example = "50"
        )
        @QueryParam("radius") @DefaultValue("100") Double radius,
        
        @Parameter(
            description = "Límite de resultados",
            required = false,
            example = "5"
        )
        @QueryParam("limit") @DefaultValue("10") int limit) {
        
        try {
            if (latitude == null || longitude == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Latitud y longitud son requeridas\"}")
                    .build();
            }
            
            if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Coordenadas fuera de rango válido\"}")
                    .build();
            }
            
            if (radius <= 0 || radius > 1000) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El radio debe estar entre 0 y 1000 km\"}")
                    .build();
            }
            
            if (limit <= 0 || limit > 50) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El límite debe estar entre 1 y 50\"}")
                    .build();
            }
            
            // Implementación simple de búsqueda por proximidad
            List<WeatherStation> nearbyStations = weatherService.getAllStations().stream()
                .filter(station -> {
                    double distance = calculateDistance(
                        latitude, longitude,
                        station.getLatitude(), station.getLongitude()
                    );
                    return distance <= radius;
                })
                .sorted((s1, s2) -> {
                    double dist1 = calculateDistance(latitude, longitude, s1.getLatitude(), s1.getLongitude());
                    double dist2 = calculateDistance(latitude, longitude, s2.getLatitude(), s2.getLongitude());
                    return Double.compare(dist1, dist2);
                })
                .limit(limit)
                .toList();
            
            return Response.ok(nearbyStations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
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
}