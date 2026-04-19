#!/bin/bash
find compbenchmark/frontend/src/app -type f -name "page.tsx" -exec sed -i 's/\.\.\/\.\.\/components\/PageHeader/\.\.\/\.\.\/components\/PageHeader/g' {} +
