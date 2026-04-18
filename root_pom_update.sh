#!/bin/bash
if ! grep -q "<module>taskqueue/backend</module>" pom.xml; then
  sed -i '/<\/modules>/i \        <module>taskqueue/backend</module>' pom.xml
fi
