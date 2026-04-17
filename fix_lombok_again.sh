#!/bin/bash
for f in $(find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence/domain -name "*.java"); do
    sed -i '/import lombok.Data;/d' $f
    sed -i '/import lombok.NoArgsConstructor;/d' $f
    sed -i '/import lombok.AllArgsConstructor;/d' $f
    sed -i '/import lombok.Builder;/d' $f
    sed -i '1s/^/import lombok.Data;\nimport lombok.NoArgsConstructor;\nimport lombok.AllArgsConstructor;\nimport lombok.Builder;\n/' $f
    sed -i '/@Data/d' $f
    sed -i '/@NoArgsConstructor/d' $f
    sed -i '/@AllArgsConstructor/d' $f
    sed -i '/@Builder/d' $f
    sed -i '/@Entity/a @Data\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder' $f
done

for f in $(find socialintelligence/backend/src/main/java/com/crosscutting/socialintelligence/dto -name "*.java"); do
    sed -i '/import lombok.Data;/d' $f
    sed -i '/import lombok.NoArgsConstructor;/d' $f
    sed -i '/import lombok.AllArgsConstructor;/d' $f
    sed -i '/import lombok.Builder;/d' $f
    sed -i '1s/^/import lombok.Data;\nimport lombok.NoArgsConstructor;\nimport lombok.AllArgsConstructor;\nimport lombok.Builder;\n/' $f
    sed -i '/@Data/d' $f
    sed -i '/@NoArgsConstructor/d' $f
    sed -i '/@AllArgsConstructor/d' $f
    sed -i '/@Builder/d' $f
    awk '/public class/{print "@Data\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder"; print; next}1' $f > tmp && mv tmp $f
    awk '/public static class/{print "@Data\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder"; print; next}1' $f > tmp && mv tmp $f
done
