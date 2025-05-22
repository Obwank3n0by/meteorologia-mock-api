package com.meteorologia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(name = "WeatherStation", description = "Información de estación meteorológica")
public class WeatherStation {

    @Schema(description = "Identificador único de la estación", example = "STATION_001")
    @NotNull
    private String id;

    @Schema(description = "Nombre descriptivo de la estación", example = "Estación Madrid Centro")
    @NotNull
    private String name;

    @Schema(description = "Ciudad donde se ubica la estación", example = "Madrid")
    @NotNull
    private String city;

    @Schema(description = "País donde se ubica la estación", example = "España")
    @NotNull
    private String country;

    @Schema(description = "Región o estado", example = "Comunidad de Madrid")
    private String region;

    @Schema(description = "Latitud de la ubicación", example = "40.4168")
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90 grados")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90 grados")
    private Double latitude;

    @Schema(description = "Longitud de la ubicación", example = "-3.7038")
    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180 grados")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180 grados")
    private Double longitude;

    @Schema(description = "Altitud en metros sobre el nivel del mar", example = "650")
    private Integer altitude;

    @Schema(description = "Zona horaria", example = "Europe/Madrid")
    @JsonProperty("time_zone")
    private String timeZone;

    @Schema(description = "Tipo de estación", example = "AUTOMATICA", 
            enumeration = {"AUTOMATICA", "MANUAL", "MIXTA"})
    @JsonProperty("station_type")
    private String stationType;

    @Schema(description = "Estado operativo de la estación", example = "ACTIVA",
            enumeration = {"ACTIVA", "INACTIVA", "MANTENIMIENTO"})
    private String status;

    @Schema(description = "Fecha de instalación", example = "2020-01-15T10:00:00")
    @JsonProperty("installation_date")
    private LocalDateTime installationDate;

    @Schema(description = "Fecha de la última actualización", example = "2025-05-22T14:30:00")
    @JsonProperty("last_update")
    private LocalDateTime lastUpdate;

    @Schema(description = "Organización responsable", example = "AEMET")
    @JsonProperty("responsible_organization")
    private String responsibleOrganization;

    @Schema(description = "Información de contacto", example = "contacto@aemet.es")
    @JsonProperty("contact_info")
    private String contactInfo;

    @Schema(description = "Descripción adicional", example = "Estación automática ubicada en el centro urbano")
    private String description;

    // Constructor vacío
    public WeatherStation() {}

    // Constructor completo
    public WeatherStation(String id, String name, String city, String country, String region,
                         Double latitude, Double longitude, Integer altitude, String timeZone,
                         String stationType, String status, LocalDateTime installationDate,
                         LocalDateTime lastUpdate, String responsibleOrganization,
                         String contactInfo, String description) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timeZone = timeZone;
        this.stationType = stationType;
        this.status = status;
        this.installationDate = installationDate;
        this.lastUpdate = lastUpdate;
        this.responsibleOrganization = responsibleOrganization;
        this.contactInfo = contactInfo;
        this.description = description;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getAltitude() { return altitude; }
    public void setAltitude(Integer altitude) { this.altitude = altitude; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    public String getStationType() { return stationType; }
    public void setStationType(String stationType) { this.stationType = stationType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDateTime installationDate) { this.installationDate = installationDate; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    public String getResponsibleOrganization() { return responsibleOrganization; }
    public void setResponsibleOrganization(String responsibleOrganization) { 
        this.responsibleOrganization = responsibleOrganization; 
    }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherStation that = (WeatherStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WeatherStation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}