import re

with open('copyoptimizer/backend/src/test/java/com/microsaas/copyoptimizer/controller/CopyAssetControllerTest.java', 'r') as f:
    content = f.read()

# Mock the AiService and TenantFilter dependencies that might be causing context initialization issues in WebMvcTest
content = content.replace(
    '@WebMvcTest(CopyAssetController.class)',
    '@WebMvcTest(CopyAssetController.class)\n@org.springframework.boot.test.mock.mockito.MockBean(com.microsaas.copyoptimizer.service.CopyAiService.class)'
)

with open('copyoptimizer/backend/src/test/java/com/microsaas/copyoptimizer/controller/CopyAssetControllerTest.java', 'w') as f:
    f.write(content)
