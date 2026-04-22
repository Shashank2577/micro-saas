import glob
import re

for file_path in glob.glob('/app/peopleanalytics/frontend/src/app/**/*.tsx', recursive=True):
    with open(file_path, 'r') as f:
        content = f.read()
    content = re.sub(r'import api from \'@/lib/api\';', 'import { api } from \'@/lib/api\';', content)
    with open(file_path, 'w') as f:
        f.write(content)
