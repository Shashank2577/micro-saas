import xml.etree.ElementTree as ET
import sys

def main():
    pom_path = 'cross-cutting/pom.xml'
    tree = ET.parse(pom_path)
    root = tree.getroot()

    ns = {'mvn': 'http://maven.apache.org/POM/4.0.0'}
    ET.register_namespace('', 'http://maven.apache.org/POM/4.0.0')

    modules = root.find('mvn:modules', ns)
    if modules is not None:
        found = False
        for mod in modules.findall('mvn:module', ns):
            if mod.text == '../tenantmanager/backend':
                found = True
                break
        if not found:
            new_mod = ET.SubElement(modules, 'module')
            new_mod.text = '../tenantmanager/backend'
            tree.write(pom_path, xml_declaration=True, encoding='UTF-8')
            print("Added ../tenantmanager/backend to cross-cutting/pom.xml")
        else:
            print("../tenantmanager/backend already in cross-cutting/pom.xml")

if __name__ == '__main__':
    main()
