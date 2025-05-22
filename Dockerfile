FROM registry.access.redhat.com/ubi8/openjdk-17:1.18

ENV LANGUAGE='en_US:en'

# Configuración del usuario para OpenShift
USER root
RUN chown 185 /deployments && chmod "g+rwX" /deployments && chown 185:root /deployments
USER 185

# Copiar archivos de la aplicación
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

# Configuración de la aplicación
EXPOSE 8080
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# Etiquetas para OpenShift
LABEL name="meteorologia-mock-api" \
      version="1.0.0" \
      architecture="x86_64" \
      summary="API Mock de Meteorología" \
      description="Microservicio mock para datos meteorológicos desarrollado con Quarkus" \
      maintainer="Equipo de Desarrollo <dev@example.com>"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]