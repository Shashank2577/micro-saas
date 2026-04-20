import os
import re

def fix_pom(file_path):
    with open(file_path, 'r') as f:
        content = f.read()
    
    # Check if maven-compiler-plugin is already there with annotationProcessorPaths
    if 'annotationProcessorPaths' in content and 'lombok' in content:
        return False
    
    # Define the plugin configuration
    plugin_config = """            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>1.18.38</version>
                        </path>
                        <path>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-configuration-processor</artifactId>
                            <version>3.3.5</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
"""
    
    if '<plugins>' in content:
        # Insert before the first plugin or at the start of plugins
        new_content = content.replace('<plugins>', f'<plugins>\n{plugin_config}')
    else:
        # If no <plugins> section, add it to <build>
        if '<build>' in content:
            new_content = content.replace('<build>', f'<build>\n        <plugins>\n{plugin_config}        </plugins>')
        else:
            # If no <build> section, add it before </project>
            new_content = content.replace('</project>', f'    <build>\n        <plugins>\n{plugin_config}        </plugins>\n    </build>\n</project>')

    with open(file_path, 'w') as f:
        f.write(new_content)
    return True

# Walk through all directories and find backend pom.xml files
for root, dirs, files in os.walk('.'):
    if 'node_modules' in root:
        continue
    if 'target' in root:
        continue
    for file in files:
        if file == 'pom.xml' and 'backend' in root:
            full_path = os.path.join(root, file)
            if fix_pom(full_path):
                print(f"Fixed {full_path}")

