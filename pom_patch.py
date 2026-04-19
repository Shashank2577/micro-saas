import sys

files = [
    'complianceradar/backend/src/test/java/com/microsaas/complianceradar/service/FeedsServiceTest.java',
    'complianceradar/backend/src/test/java/com/microsaas/complianceradar/service/NormalizationServiceTest.java'
]

for file_path in files:
    with open(file_path, 'r') as file:
        content = file.read()
    content = content.replace('TenantContext.set(tenantId.toString());', 'TenantContext.set(tenantId);')
    with open(file_path, 'w') as file:
        file.write(content)
