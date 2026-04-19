from playwright.sync_api import sync_playwright

def run_cuj(page):
    page.goto("http://localhost:3002")
    page.wait_for_timeout(2000)

    # Dashboard view
    page.screenshot(path="/home/jules/verification/screenshots/dashboard.png")
    page.wait_for_timeout(1000)

    # Navigate to Frameworks
    page.get_by_role("link", name="Frameworks").click()
    page.wait_for_timeout(1500)
    page.screenshot(path="/home/jules/verification/screenshots/frameworks.png")

    # Navigate to Evidence
    page.get_by_role("link", name="Evidence").click()
    page.wait_for_timeout(1500)
    page.screenshot(path="/home/jules/verification/screenshots/evidence.png")

    # Navigate to Gaps
    page.get_by_role("link", name="Gaps").click()
    page.wait_for_timeout(1500)
    page.screenshot(path="/home/jules/verification/screenshots/gaps.png")
    
    # Navigate to Reports
    page.get_by_role("link", name="Reports").click()
    page.wait_for_timeout(1500)
    page.screenshot(path="/home/jules/verification/screenshots/reports.png")

if __name__ == "__main__":
    import os
    os.makedirs("/home/jules/verification/videos", exist_ok=True)
    os.makedirs("/home/jules/verification/screenshots", exist_ok=True)
    
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
