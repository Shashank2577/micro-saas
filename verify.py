from playwright.sync_api import sync_playwright

def run_cuj(page):
    page.goto("http://localhost:3000/api-specs")
    page.wait_for_timeout(2000)
    page.screenshot(path="/home/jules/verification/screenshots/api-specs.png")

    page.goto("http://localhost:3000/api-versions")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/api-versions.png")

    page.goto("http://localhost:3000/breaking-changes")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/breaking-changes.png")

    page.goto("http://localhost:3000/compatibility-reports")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/compatibility-reports.png")

    page.goto("http://localhost:3000/consumers")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/consumers.png")

    page.goto("http://localhost:3000/deprecation-notices")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/deprecation-notices.png")

    page.goto("http://localhost:3000/notifications")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/notifications.png")

    page.goto("http://localhost:3000/sdk-artifacts")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/sdk-artifacts.png")

    page.wait_for_timeout(1000)

if __name__ == "__main__":
    import os
    os.makedirs("/home/jules/verification/screenshots", exist_ok=True)
    os.makedirs("/home/jules/verification/videos", exist_ok=True)

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        context = browser.new_context(
            record_video_dir="/home/jules/verification/videos"
        )
        page = context.new_page()
        try:
            run_cuj(page)
        finally:
            context.close()
            browser.close()
