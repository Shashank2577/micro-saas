import glob
import re

for file_path in glob.glob('/app/peopleanalytics/backend/src/main/java/com/microsaas/peopleanalytics/model/PerformanceMetric.java'):
    with open(file_path, 'r') as f:
        content = f.read()

    # Check if we successfully added the annotation or we need to try again more carefully
    if '@Column(name = "metric_value")' not in content:
        content = re.sub(r'private Double value;', '@Column(name = "metric_value")\n    private Double value;', content)

    with open(file_path, 'w') as f:
        f.write(content)
