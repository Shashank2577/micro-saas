import glob
import re

for test_file in glob.glob('copyoptimizer/frontend/src/test/*Page.test.tsx'):
    with open(test_file, 'r') as f:
        content = f.read()

    if 'Analytics' not in test_file:
        # Move the data check inside waitFor because it's fetched asynchronously now
        content = re.sub(
            r"expect\(screen\.getByText\('Mock Data'\)\)\.toBeInTheDocument\(\);",
            "",
            content
        )
        content = re.sub(
            r"expect\(screen\.queryByText\('Loading...'\)\)\.not\.toBeInTheDocument\(\);",
            "expect(screen.queryByText('Loading...')).not.toBeInTheDocument();\n      expect(screen.getByText('Mock Data')).toBeInTheDocument();",
            content
        )
        with open(test_file, 'w') as f:
            f.write(content)
