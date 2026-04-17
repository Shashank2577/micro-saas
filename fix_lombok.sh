#!/bin/bash
find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence -name "*.java" -exec sed -i 's/import lombok.Builder;/import lombok.Builder;\nimport lombok.Getter;\nimport lombok.Setter;/' {} \;
find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence -name "*.java" -exec sed -i 's/@Data/@Getter\n@Setter\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder/' {} \;
find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence -name "*.java" -exec sed -i 's/@NoArgsConstructor\n@NoArgsConstructor/@NoArgsConstructor/' {} \;
find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence -name "*.java" -exec sed -i 's/@AllArgsConstructor\n@AllArgsConstructor/@AllArgsConstructor/' {} \;
find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence -name "*.java" -exec sed -i 's/@Builder\n@Builder/@Builder/' {} \;
