#!/bin/bash
for app in */ ; do
    app_name=$(basename "$app")
    if [ -f "$app/backend/pom.xml" ] && [ -f "$app/frontend/package.json" ]; then
        echo "Processing $app_name..."
        node capture.js "$app_name"
    fi
done
