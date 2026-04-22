import re
with open('/app/peopleanalytics/backend/pom.xml', 'r') as f:
    content = f.read()

content = re.sub(
    r'<plugin>\s*<groupId>org.springframework.boot</groupId>\s*<artifactId>spring-boot-maven-plugin</artifactId>\s*</plugin>',
    '<plugin>\n                <groupId>org.springframework.boot</groupId>\n                <artifactId>spring-boot-maven-plugin</artifactId>\n                <configuration>\n                    <excludes>\n                        <exclude>\n                            <groupId>org.projectlombok</groupId>\n                            <artifactId>lombok</artifactId>\n                        </exclude>\n                    </excludes>\n                </configuration>\n            </plugin>',
    content, flags=re.DOTALL
)

with open('/app/peopleanalytics/backend/pom.xml', 'w') as f:
    f.write(content)
