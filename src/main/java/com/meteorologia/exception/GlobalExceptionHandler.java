package com.meteorologia.exception;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    @APIResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Map.class)
        )
    )
    public Response toResponse(Exception exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        // Determinar el código de estado y mensaje basado en el tipo de excepción
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String message = "Error interno del servidor";
        String errorCode = "INTERNAL_ERROR";
        
        if (exception instanceof IllegalArgumentException) {
            status = Response.Status.BAD_REQUEST;
            message = "Parámetros inválidos: " + exception.getMessage();
            errorCode = "INVALID_PARAMETERS";
        } else if (exception instanceof SecurityException) {
            status = Response.Status.UNAUTHORIZED;
            message = "Acceso no autorizado";
            errorCode = "UNAUTHORIZED";
        } else if (exception instanceof UnsupportedOperationException) {
            status = Response.Status.NOT_IMPLEMENTED;
            message = "Operación no implementada";
            errorCode = "NOT_IMPLEMENTED";
        }
        
        // Construir respuesta de error estructurada
        errorResponse.put("error", true);
        errorResponse.put("error_code", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.getStatusCode());
        
        // En desarrollo, incluir detalles del stack trace
        boolean includeStackTrace = Boolean.parseBoolean(
            System.getProperty("quarkus.profile", "dev").equals("dev") ? "true" : "false"
        );
        
        if (includeStackTrace) {
            errorResponse.put("exception_type", exception.getClass().getSimpleName());
            errorResponse.put("stack_trace", getStackTraceAsString(exception));
        }
        
        return Response.status(status)
            .entity(errorResponse)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
    
    private String getStackTraceAsString(Exception exception) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}