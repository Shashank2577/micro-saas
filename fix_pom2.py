import re

with open('copyoptimizer/backend/pom.xml', 'r') as f:
    content = f.read()

# Add standard maven-compiler-plugin configuration for lombok
replacement = """
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>1.18.32</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
"""

content = re.sub(
    r'<plugin>\s*<groupId>org.springframework.boot</groupId>\s*<artifactId>spring-boot-maven-plugin</artifactId>',
    replacement + '<groupId>org.springframework.boot</groupId><artifactId>spring-boot-maven-plugin</artifactId>',
    content
)

with open('copyoptimizer/backend/pom.xml', 'w') as f:
    f.write(content)
