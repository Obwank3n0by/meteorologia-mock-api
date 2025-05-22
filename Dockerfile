# Dockerfile para OpenShift Source-to-Image (S2I)
# Este Dockerfile funciona con el proceso S2I de OpenShift donde Maven se ejecuta automáticamente

FROM registry.access.redhat.com/ubi8/openjdk-17:1.18

# Metadatos para OpenShift
LABEL name="meteorologia-mock-api" \
      version="1.0.0" \
      architecture="x86_64" \
      summary="API Mock de Meteorología desarrollada con Quarkus" \
      description="Microservicio mock para datos meteorológicos que proporciona endpoints REST para desarrollo y testing" \
      maintainer="Equipo de Desarrollo <dev@example.com>" \
      io.k8s.description="API Mock de Meteorología con Quarkus" \
      io.k8s.display-name="Meteorología Mock API" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="java,quarkus,api,mock,meteorologia"

# Variables de entorno
ENV LANGUAGE='en_US:en' \
    LANG='en_US.UTF-8' \
    LC_ALL='en_US.UTF-8'

# Configuración específica de Quarkus para OpenShift
ENV QUARKUS_HTTP_HOST=0.0.0.0 \
    QUARKUS_HTTP_PORT=8080

# Configuración optimizada de JVM para contenedores
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 \
                      -Djava.util.logging.manager=org.jboss.logmanager.LogManager \
                      -XX:+UseParallelGC \
                      -XX:MinHeapFreeRatio=10 \
                      -XX:MaxHeapFreeRatio=20 \
                      -XX:GCTimeRatio=4 \
                      -XX:AdaptiveSizePolicyWeight=90 \
                      -XX:+ExitOnOutOfMemoryError"

# Configuración del usuario para OpenShift (usuario no privilegiado)
USER root

# Crear directorio de trabajo y configurar permisos para OpenShift
RUN chown 185:0 /deployments && \
    chmod "g+rwX" /deployments && \
    chown 185:0 /deployments

# Cambiar al usuario no privilegiado
USER 185

# Puerto expuesto
EXPOSE 8080

# El punto de entrada se maneja automáticamente por la imagen base de OpenJDK
# Los archivos compilados serán copiados automáticamente por el proceso S2I