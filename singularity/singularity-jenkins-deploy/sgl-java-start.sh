#!/usr/bin/env bash

# #####
# USAGE
#   sgl-java-start.sh PROJECT_NAME PROJECT_PORT HOST_PORTS PROJECT_FILE
# EXAMPLE
#   sgl-java-start.sh demo-app 8080 8081 /data/project/demo-app.jar
#       Build and run a singularity project. Naming the image to `demo-app`, app exposes a port `8080`,
#       and map it to port `8081` of the host. The path of app is `/data/project/demo-app.jar`.
#   sgl-java-start.sh demo-app 8080 "8081 8082" /data/project/demo-app.jar
#       Build and run a project in multiple ports.

# -e: Exit when error occurred
set -e

# Directory of this script
BASE_DIR="$(dirname "$0")"
# Project name, or image name
PROJECT_NAME="${1:-app}"
# Exposed port of image
PROJECT_PORT="${2:-8080}"
# Mapped ports of host
HOST_PORTS="${3:-18080}"
# Path of project file
PROJECT_FILE="${4:-app.jar}"
# Directory of project file
PROJECT_DIR="$(dirname "$PROJECT_FILE")"
# Path of generated Singularity def file
SGL_DEF_FILE="$BASE_DIR/sgl-java-project-$PROJECT_NAME-$RANDOM.def"
# Path of Singularity sif file
SGL_SIF_FILE="$PROJECT_DIR/$PROJECT_NAME.sif"
# Directory of container overlay
SGL_OVERLAY_DIR="$PROJECT_DIR/$PROJECT_NAME-overlay"

echo ================================
echo Debug information:
echo "Script directory: $BASE_DIR/"
echo "Project name: $PROJECT_NAME"
echo "Project port: $PROJECT_PORT"
echo "Host ports: $HOST_PORTS"
echo "Project file: $PROJECT_FILE"
echo "Project directory: $PROJECT_DIR/"
echo "Singularity def file path: $SGL_DEF_FILE"
echo "Singularity sif file path: $SGL_SIF_FILE"
echo "Singularity overlay directory: $SGL_OVERLAY_DIR/"
echo

echo ================================
echo Initializing...
# Create overlay directory
mkdir -p "$SGL_OVERLAY_DIR/"
# copy entrypoint shell
cp -f "$BASE_DIR/sgl-java-entrypoint.sh" "$PROJECT_DIR/"
# Generate Singularity def file
SGL_PROJECT_FILE="$(basename "$PROJECT_FILE")"
# Make a copy of origional denfination file
cp -f "$BASE_DIR/sgl-java-project.def" "$SGL_DEF_FILE"
# Replace variables in def file
sed -i -E -e "s|\{\{PROJECT_FILE\}\}|$SGL_PROJECT_FILE|g" \
    -e "s|\{\{PROJECT_DIR\}\}|$PROJECT_DIR|g" \
    "$SGL_DEF_FILE"
echo

echo ================================
echo Building container...
sudo singularity build --force "$SGL_SIF_FILE" "$SGL_DEF_FILE"
echo

echo ================================
for PORT in $HOST_PORTS; do
    CONTAINER_NAME="$PROJECT_NAME-$PORT"
    echo --------------------------------
    echo "Running container $CONTAINER_NAME..."
    [[ -n "$(sudo singularity instance list | grep "$CONTAINER_NAME")" ]] \
        && echo "Stopping running container $CONTAINER_NAME..." \
        && sudo singularity instance stop --force "$CONTAINER_NAME"
    sudo singularity instance start \
        --overlay "$SGL_OVERLAY_DIR/" \
        --net --network-args "portmap=$PORT:$PROJECT_PORT/tcp" \
        --bind "/data:/host-data" \
        "$SGL_SIF_FILE" "$CONTAINER_NAME"
    sudo singularity run \
        --overlay "$SGL_OVERLAY_DIR/" \
        --net --network-args "portmap=$PORT:$PROJECT_PORT/tcp" \
        --bind "/data:/host-data" \
        "$SGL_SIF_FILE" \
        >> "$PROJECT_DIR/$PROJECT_FILE-$(date +%Y%m%d).out" &
    echo "Logs:"
    sudo singularity instance list -l | sed -n "/$CONTAINER_NAME/ p" | awk '{print $(NF)}'
done
echo

echo ================================
echo Deleting temporary files...
rm -f "$SGL_DEF_FILE"
echo

echo ALL DONE!
