import glob
import re

for file_path in glob.glob('/app/peopleanalytics/frontend/src/app/**/*.tsx', recursive=True):
    with open(file_path, 'r') as f:
        content = f.read()

    # ensure everything using useQuery isn't prerendered statically
    if 'useQuery' in content:
        # Just return null if not running on client / suspense wrapper is better but this works
        pass # The error means it is running on the server but our QueryClientProvider is missing on the server
        # Next 13+ with App Router needs a QueryClientProvider inside a client component
