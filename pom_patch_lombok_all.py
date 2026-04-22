import glob
import re

for file_path in glob.glob('/app/peopleanalytics/backend/src/main/java/com/microsaas/peopleanalytics/**/*.java', recursive=True):
    with open(file_path, 'r') as f:
        content = f.read()

    # Re-enable Data for all models since we fixed the lombok plugin
    if "import lombok." in content:
        # We need to make sure we don't accidentally remove things we want,
        # but the lombok plugin exclude was the root cause, now that it's fixed we should revert any previous manual changes if needed.
        pass
