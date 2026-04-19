import xml.etree.ElementTree as ET
import os

pom_path = 'copyoptimizer/backend/pom.xml'
ET.register_namespace('', 'http://maven.apache.org/POM/4.0.0')
tree = ET.parse(pom_path)
root = tree.getroot()

ns = {'m': 'http://maven.apache.org/POM/4.0.0'}

build = root.find('m:build', ns)
if build is not None:
    plugins = build.find('m:plugins', ns)
    if plugins is not None:
        for plugin in plugins.findall('m:plugin', ns):
            artifactId = plugin.find('m:artifactId', ns)
            if artifactId is not None and artifactId.text == 'maven-compiler-plugin':
                configuration = plugin.find('m:configuration', ns)
                if configuration is None:
                    configuration = ET.SubElement(plugin, 'configuration')

                paths = configuration.find('m:annotationProcessorPaths', ns)
                if paths is None:
                    paths = ET.SubElement(configuration, 'annotationProcessorPaths')

                lombok_found = False
                for path in paths.findall('m:path', ns):
                    a_id = path.find('m:artifactId', ns)
                    if a_id is not None and a_id.text == 'lombok':
                        lombok_found = True
                        break

                if not lombok_found:
                    path = ET.SubElement(paths, 'path')
                    groupId = ET.SubElement(path, 'groupId')
                    groupId.text = 'org.projectlombok'
                    artifactId = ET.SubElement(path, 'artifactId')
                    artifactId.text = 'lombok'
                    # use property or hardcoded version if property not defined
                    version = ET.SubElement(path, 'version')
                    version.text = '${lombok.version}'

tree.write(pom_path, encoding='utf-8', xml_declaration=True)
