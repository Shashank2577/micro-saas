import re

with open('copyoptimizer/backend/pom.xml', 'r') as f:
    content = f.read()

# Clean up redundant maven compiler plugins
pattern = r'<plugin>\s*<groupId>org.apache.maven.plugins</groupId>\s*<artifactId>maven-compiler-plugin</artifactId>.*?</plugin>'

plugins = re.findall(pattern, content, flags=re.DOTALL)

# keep only the first one
if len(plugins) > 1:
    for p in plugins[1:]:
        content = content.replace(p, '')

with open('copyoptimizer/backend/pom.xml', 'w') as f:
    f.write(content)
