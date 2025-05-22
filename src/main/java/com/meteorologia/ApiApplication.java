package com.meteorologia;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
    info = @Info(
        title = "Meteorología Mock API",
        version = "1.0.0",
        description = "API Mock para servicios meteorológicos con datos simulados. " +
                     "Esta API proporciona datos meteorológicos ficticios para pruebas y desarrollo. " +
                     "Incluye información sobre clima actual, pronósticos y gestión de estaciones meteorológicas.",
        contact = @Contact(
            name = "Equipo de Meteorología",
            email = "support@meteorologia.example.com",
            url = "http://meteorologia.example.com/contact"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor de desarrollo"
        ),
        @Server(
            url = "https://api.meteorologia.example.com",
            description = "Servidor de producción"
        )
    },
    tags = {
        @Tag(
            name = "Weather API",
            description = "Endpoints para consultar datos meteorológicos actuales y pronósticos"
        ),
        @Tag(
            name = "Stations API", 
            description = "Endpoints para gestionar y consultar información de estaciones meteorológicas"
        )
    }
)
public class ApiApplication extends Application {
    // La configuración se maneja a través de las anotaciones
}