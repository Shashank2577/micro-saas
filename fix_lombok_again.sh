#!/bin/bash
# Remove spring-boot-maven-plugin completely from pom.xml and just leave standard configuration
# We just need it to compile properly, so we can strip the <build> entirely and put our own

pom_file="copyoptimizer/backend/pom.xml"

sed -i '/<build>/,/<\/build>/d' $pom_file

cat << 'END_XML' >> $pom_file
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
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
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
END_XML

# Remove the trailing </project> that might be duplicated
sed -i 's/<\/project><\/project>/<\/project>/g' $pom_file

