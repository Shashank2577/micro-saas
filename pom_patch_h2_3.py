import glob
import re

for file_path in glob.glob('/app/peopleanalytics/backend/src/main/java/com/microsaas/peopleanalytics/model/PerformanceMetric.java'):
    with open(file_path, 'r') as f:
        content = f.read()

    content = re.sub(r'@Column\(name = "\\"value\\""\)', '@Column(name = "metric_value")', content)

    with open(file_path, 'w') as f:
        f.write(content)
