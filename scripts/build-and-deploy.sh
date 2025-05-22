#!/bin/bash

# Script para construir y desplegar la aplicación Mock de Meteorología en OpenShift
# Autor: Equipo de Desarrollo
# Versión: 1.0.0

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variables de configuración
APP_NAME="meteorologia-mock-api"
APP_VERSION="1.0.0"
NAMESPACE=${OPENSHIFT_NAMESPACE:-"default"}
REGISTRY=${CONTAINER_REGISTRY:-"image-registry.openshift-image-registry.svc:5000"}
IMAGE_NAME="${REGISTRY}/${NAMESPACE}/${APP_NAME}:${APP_VERSION}"
LATEST_IMAGE="${REGISTRY}/${NAMESPACE}/${APP_NAME}:latest"

# Funciones auxiliares
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_requirements() {
    log_info "Verificando requisitos..."
    
    # Verificar Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven no está instalado"
        exit 1
    fi
    
    # Verificar Docker/Podman
    if ! command -v docker &> /dev/null && ! command -v podman &> /dev/null; then
        log_error "Docker o Podman no están instalados"
        exit 1
    fi
    
    # Verificar oc CLI
    if ! command -v oc &> /dev/null; then
        log_error "OpenShift CLI (oc) no está instalado"
        exit 1
    fi
    
    # Verificar conexión a OpenShift
    if ! oc whoami &> /dev/null; then
        log_error "No hay sesión activa en OpenShift. Ejecuta 'oc login' primero"
        exit 1
    fi
    
    log_success "Todos los requisitos están disponibles"
}

build_application() {
    log_info "Construyendo la aplicación con Maven..."
    
    # Limpiar y compilar
    mvn clean package -DskipTests -Dquarkus.package.type=fast-jar
    
    if [ $? -eq 0 ]; then
        log_success "Aplicación construida exitosamente"
    else
        log_error "Error al construir la aplicación"
        exit 1
    fi
}

build_container() {
    log_info "Construyendo imagen de contenedor..."
    
    # Determinar el comando de contenedor (docker o podman)
    CONTAINER_CMD="docker"
    if command -v podman &> /dev/null && ! command -v docker &> /dev/null; then
        CONTAINER_CMD="podman"
    fi
    
    # Construir imagen
    $CONTAINER_CMD build -t $IMAGE_NAME -t $LATEST_IMAGE .
    
    if [ $? -eq 0 ]; then
        log_success "Imagen de contenedor construida: $IMAGE_NAME"
    else
        log_error "Error al construir la imagen de contenedor"
        exit 1
    fi
}

push_image() {
    log_info "Subiendo imagen al registro de OpenShift..."
    
    # Determinar el comando de contenedor
    CONTAINER_CMD="docker"
    if command -v podman &> /dev/null && ! command -v docker &> /dev/null; then
        CONTAINER_CMD="podman"
    fi
    
    # Hacer login al registry de OpenShift
    oc registry login
    
    # Subir imágenes
    $CONTAINER_CMD push $IMAGE_NAME
    $CONTAINER_CMD push $LATEST_IMAGE
    
    if [ $? -eq 0 ]; then
        log_success "Imagen subida exitosamente al registro"
    else
        log_error "Error al subir la imagen al registro"
        exit 1
    fi
}

deploy_to_openshift() {
    log_info "Desplegando en OpenShift..."
    
    # Crear namespace si no existe
    oc get namespace $NAMESPACE &> /dev/null || oc create namespace $NAMESPACE
    
    # Cambiar al namespace
    oc project $NAMESPACE
    
    # Aplicar ConfigMap
    log_info "Aplicando ConfigMap..."
    oc apply -f openshift-configmap.yaml
    
    # Aplicar Deployment
    log_info "Aplicando Deployment..."
    # Reemplazar imagen en el deployment
    sed "s|image: meteorologia-mock-api:latest|image: $LATEST_IMAGE|g" openshift-deployment.yaml | oc apply -f -
    
    # Aplicar Service y Route
    log_info "Aplicando Service y Route..."
    oc apply -f openshift-service.yaml
    
    # Esperar a que el deployment esté listo
    log_info "Esperando a que el deployment esté listo..."
    oc rollout status deployment/$APP_NAME --timeout=300s
    
    if [ $? -eq 0 ]; then
        log_success "Aplicación desplegada exitosamente"
    else
        log_error "Error en el despliegue"
        exit 1
    fi
}

show_deployment_info() {
    log_info "Información del despliegue:"
    
    # Obtener URL de la aplicación
    ROUTE_URL=$(oc get route ${APP_NAME}-route -o jsonpath='{.spec.host}' 2>/dev/null)
    
    if [ ! -z "$ROUTE_URL" ]; then
        echo -e "${GREEN}🌐 URL de la aplicación:${NC} https://$ROUTE_URL"
        echo -e "${GREEN}📖 Swagger UI:${NC} https://$ROUTE_URL/swagger-ui"
        echo -e "${GREEN}🔍 OpenAPI Spec:${NC} https://$ROUTE_URL/q/openapi"
        echo -e "${GREEN}❤️  Health Check:${NC} https://$ROUTE_URL/api/weather/health"
    fi
    
    # Mostrar pods
    echo -e "\n${BLUE}Pods en ejecución:${NC}"
    oc get pods -l app=$APP_NAME
    
    # Mostrar servicios
    echo -e "\n${BLUE}Servicios:${NC}"
    oc get service -l app=$APP_NAME
    
    # Mostrar rutas
    echo -e "\n${BLUE}Rutas:${NC}"
    oc get route -l app=$APP_NAME
}

cleanup() {
    log_info "Limpiando recursos temporales..."
    
    # Limpiar imágenes locales (opcional)
    if [ "$CLEANUP_LOCAL_IMAGES" = "true" ]; then
        CONTAINER_CMD="docker"
        if command -v podman &> /dev/null && ! command -v docker &> /dev/null; then
            CONTAINER_CMD="podman"
        fi
        
        $CONTAINER_CMD rmi $IMAGE_NAME $LATEST_IMAGE 2>/dev/null || true
    fi
    
    log_success "Limpieza completada"
}

main() {
    echo -e "${GREEN}"
    echo "================================================"
    echo "   Meteorología Mock API - Deploy Script"
    echo "================================================"
    echo -e "${NC}"
    
    check_requirements
    build_application
    build_container
    push_image
    deploy_to_openshift
    show_deployment_info
    
    echo -e "\n${GREEN}🎉 Despliegue completado exitosamente!${NC}"
    echo -e "${YELLOW}💡 Para ver los logs: oc logs -f deployment/$APP_NAME${NC}"
    echo -e "${YELLOW}💡 Para escalar: oc scale deployment/$APP_NAME --replicas=N${NC}"
}

# Función de ayuda
show_help() {
    echo "Uso: $0 [opciones]"
    echo ""
    echo "Opciones:"
    echo "  -h, --help              Mostrar esta ayuda"
    echo "  -n, --namespace NAME    Especificar namespace (default: current)"
    echo "  -v, --version VERSION   Especificar versión de la imagen (default: 1.0.0)"
    echo "  -c, --cleanup           Limpiar imágenes locales después del despliegue"
    echo "  --dry-run              Solo construir, no desplegar"
    echo ""
    echo "Variables de entorno:"
    echo "  OPENSHIFT_NAMESPACE     Namespace de OpenShift"
    echo "  CONTAINER_REGISTRY      Registry de contenedores"
    echo ""
    echo "Ejemplos:"
    echo "  $0                                    # Despliegue básico"
    echo "  $0 -n my-project -v 1.1.0           # Namespace y versión específicos"
    echo "  $0 --cleanup                         # Con limpieza de imágenes locales"
}

# Procesar argumentos de línea de comandos
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -n|--namespace)
            NAMESPACE="$2"
            shift 2
            ;;
        -v|--version)
            APP_VERSION="$2"
            IMAGE_NAME="${REGISTRY}/${NAMESPACE}/${APP_NAME}:${APP_VERSION}"
            shift 2
            ;;
        -c|--cleanup)
            CLEANUP_LOCAL_IMAGES="true"
            shift
            ;;
        --dry-run)
            DRY_RUN="true"
            shift
            ;;
        *)
            log_error "Opción desconocida: $1"
            show_help
            exit 1
            ;;
    esac
done

# Ejecutar función principal si no es dry-run
if [ "$DRY_RUN" = "true" ]; then
    log_info "Modo dry-run: solo construyendo..."
    check_requirements
    build_application
    build_container
    log_success "Construcción completada (dry-run)"
else
    main
    cleanup
fi