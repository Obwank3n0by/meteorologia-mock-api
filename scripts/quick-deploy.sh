#!/bin/bash

# Script rápido para desplegar en OpenShift usando S2I (Source-to-Image)
# Ideal para desarrollo rápido sin necesidad de construir imágenes localmente

set -e

# Variables
APP_NAME="meteorologia-mock-api"
NAMESPACE=${1:-$(oc project -q)}
GIT_REPO=${2:-"."}
BUILD_IMAGE="registry.redhat.io/ubi8/openjdk-17:1.18"

echo "🚀 Desplegue rápido de Meteorología Mock API"
echo "📁 Namespace: $NAMESPACE"
echo "🎯 Aplicación: $APP_NAME"

# Verificar conexión a OpenShift
if ! oc whoami &> /dev/null; then
    echo "❌ Error: No hay sesión activa en OpenShift"
    echo "💡 Ejecuta: oc login <cluster-url>"
    exit 1
fi

# Cambiar al namespace
oc project $NAMESPACE

echo "🏗️  Creando aplicación con Source-to-Image..."

# Eliminar aplicación existente si existe
oc delete all -l app=$APP_NAME 2>/dev/null || true

# Crear nueva aplicación usando S2I con código fuente local
oc new-app $BUILD_IMAGE~$GIT_REPO \
    --name=$APP_NAME \
    --labels="app=$APP_NAME,version=1.0.0" \
    --env="MAVEN_ARGS=clean package -DskipTests -Dquarkus.package.type=fast-jar"

# Esperar a que termine el build
echo "⏳ Esperando que termine la construcción..."
oc logs -f bc/$APP_NAME

# Exponer el servicio
echo "🌐 Exponiendo servicio..."
oc expose service/$APP_NAME \
    --hostname=$APP_NAME-$NAMESPACE.apps.$(oc whoami --show-server | cut -d'.' -f2-) \
    --labels="app=$APP_NAME"

# Configurar health checks
echo "❤️  Configurando health checks..."
oc set probe deployment/$APP_NAME \
    --readiness \
    --get-url=http://:8080/api/weather/health \
    --initial-delay-seconds=10 \
    --period-seconds=5

oc set probe deployment/$APP_NAME \
    --liveness \
    --get-url=http://:8080/api/weather/health \
    --initial-delay-seconds=30 \
    --period-seconds=10

# Configurar recursos
echo "⚡ Configurando recursos..."
oc set resources deployment/$APP_NAME \
    --requests=cpu=100m,memory=256Mi \
    --limits=cpu=500m,memory=512Mi

# Esperar despliegue
echo "⏳ Esperando despliegue..."
oc rollout status deployment/$APP_NAME --timeout=300s

# Mostrar información
echo ""
echo "✅ Despliegue completado!"
echo "📊 Estado de la aplicación:"
oc get pods -l app=$APP_NAME
echo ""

# Obtener URL
ROUTE_URL=$(oc get route $APP_NAME -o jsonpath='{.spec.host}' 2>/dev/null)
if [ ! -z "$ROUTE_URL" ]; then
    echo "🌐 URL de la aplicación: https://$ROUTE_URL"
    echo "📖 Swagger UI: https://$ROUTE_URL/swagger-ui"
    echo "🔍 OpenAPI Spec: https://$ROUTE_URL/q/openapi"
    echo "❤️  Health Check: https://$ROUTE_URL/api/weather/health"
else
    echo "⚠️  No se pudo obtener la URL de la ruta"
fi

echo ""
echo "💡 Comandos útiles:"
echo "   Ver logs: oc logs -f deployment/$APP_NAME"
echo "   Escalar: oc scale deployment/$APP_NAME --replicas=N"
echo "   Eliminar: oc delete all -l app=$APP_NAME"