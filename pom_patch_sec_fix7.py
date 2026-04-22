import glob
import re

for file_path in glob.glob('/app/peopleanalytics/backend/src/main/java/com/microsaas/peopleanalytics/controller/*.java'):
    with open(file_path, 'r') as f:
        content = f.read()

    content = re.sub(r'import org.springframework.security.access.prepost.PreAuthorize;', '', content)
    content = re.sub(r'@PreAuthorize\([^)]*\)', '', content)

    with open(file_path, 'w') as f:
        f.write(content)
