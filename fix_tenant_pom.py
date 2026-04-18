import xml.etree.ElementTree as ET
import sys

def main(app_dir):
    pom_path = f'{app_dir}/backend/pom.xml'
    tree = ET.parse(pom_path)
    root = tree.getroot()

    ns = {'mvn': 'http://maven.apache.org/POM/4.0.0'}
    ET.register_namespace('', 'http://maven.apache.org/POM/4.0.0')

    # ensure annotation processors are setup
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

    tree.write(pom_path, xml_declaration=True, encoding='UTF-8')

if __name__ == '__main__':
    main(sys.argv[1])
