import xml.etree.ElementTree as ET

tree = ET.parse('observabilitystack/backend/pom.xml')
root = tree.getroot()

ns = {'mvn': 'http://maven.apache.org/POM/4.0.0'}
ET.register_namespace('', 'http://maven.apache.org/POM/4.0.0')

for plugin in root.findall('.//mvn:plugin', ns):
    artifactId = plugin.find('mvn:artifactId', ns)
    if artifactId is not None and artifactId.text == 'maven-compiler-plugin':
        config = plugin.find('mvn:configuration', ns)
        if config is None:
            config = ET.SubElement(plugin, 'configuration')

        paths = config.find('mvn:annotationProcessorPaths', ns)
        if paths is None:
            paths = ET.SubElement(config, 'annotationProcessorPaths')
            path = ET.SubElement(paths, 'path')
            groupId = ET.SubElement(path, 'groupId')
            groupId.text = 'org.projectlombok'
            artId = ET.SubElement(path, 'artifactId')
            artId.text = 'lombok'
            version = ET.SubElement(path, 'version')
            version.text = '${lombok.version}'

tree.write('observabilitystack/backend/pom.xml', xml_declaration=True, encoding='UTF-8')
