package com.meteorologia.controller;

import com.meteorologia.model.WeatherData;
import com.meteorologia.model.WeatherForecast;
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

@Path("/api/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Weather API", description = "API para consultar datos meteorológicos")
public class WeatherController {

    @Inject
    WeatherService weatherService;

    @GET
    @Path("/current/{stationId}")
    @Operation(
        summary = "Obtener clima actual por ID de estación",
        description = "Retorna los datos meteorológicos actuales de una estación específica"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Datos meteorológicos obtenidos exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherData.class)
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
    public Response getCurrentWeather(
        @Parameter(
            description = "ID único de la estación meteorológica",
            required = true,
            example = "STATION_001"
        )
        @PathParam("stationId") String stationId) {
        
        try {
            WeatherData weatherData = weatherService.getCurrentWeather(stationId);
            if (weatherData == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Estación no encontrada\"}")
                    .build();
            }
            return Response.ok(weatherData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/current")
    @Operation(
        summary = "Obtener clima actual por coordenadas",
        description = "Retorna los datos meteorológicos actuales basados en latitud y longitud"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Datos meteorológicos obtenidos exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherData.class)
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
    public Response getCurrentWeatherByLocation(
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
        @QueryParam("lon") Double longitude) {
        
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
            
            WeatherData weatherData = weatherService.getCurrentWeatherByLocation(latitude, longitude);
            return Response.ok(weatherData).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/forecast/{stationId}")
    @Operation(
        summary = "Obtener pronóstico meteorológico por ID de estación",
        description = "Retorna el pronóstico meteorológico para los próximos días de una estación específica"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Pronóstico obtenido exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherForecast[].class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Parámetros inválidos"
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
    public Response getForecast(
        @Parameter(
            description = "ID único de la estación meteorológica",
            required = true,
            example = "STATION_001"
        )
        @PathParam("stationId") String stationId,
        
        @Parameter(
            description = "Número de días para el pronóstico (1-10)",
            required = false,
            example = "5"
        )
        @QueryParam("days") @DefaultValue("5") int days) {
        
        try {
            if (days < 1 || days > 10) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El número de días debe estar entre 1 y 10\"}")
                    .build();
            }
            
            List<WeatherForecast> forecast = weatherService.getForecast(stationId, days);
            if (forecast.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Estación no encontrada\"}")
                    .build();
            }
            
            return Response.ok(forecast).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/forecast")
    @Operation(
        summary = "Obtener pronóstico meteorológico por coordenadas",
        description = "Retorna el pronóstico meteorológico basado en latitud y longitud"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Pronóstico obtenido exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = WeatherForecast[].class)
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
    public Response getForecastByLocation(
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
            description = "Número de días para el pronóstico (1-10)",
            required = false,
            example = "5"
        )
        @QueryParam("days") @DefaultValue("5") int days) {
        
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
            
            if (days < 1 || days > 10) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El número de días debe estar entre 1 y 10\"}")
                    .build();
            }
            
            List<WeatherForecast> forecast = weatherService.getForecastByLocation(latitude, longitude, days);
            return Response.ok(forecast).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Error interno del servidor\"}")
                .build();
        }
    }

    @GET
    @Path("/health")
    @Operation(
        summary = "Verificar estado del servicio",
        description = "Endpoint para verificar que el servicio meteorológico está funcionando"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Servicio funcionando correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Map.class)
            )
        )
    })
    public Response healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "service", "Meteorologia Mock API",
            "version", "1.0.0",
            "timestamp", java.time.LocalDateTime.now()
        );
        return Response.ok(health).build();
    }
}