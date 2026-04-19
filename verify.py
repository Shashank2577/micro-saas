from playwright.sync_api import sync_playwright

def run_cuj(page):
    page.goto("http://localhost:3000/policies")
    page.wait_for_timeout(2000)

    # Take screenshot at the key moment
    page.screenshot(path="/home/jules/verification/screenshots/policies.png")

    page.goto("http://localhost:3000/stewards")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/stewards.png")

    page.goto("http://localhost:3000/violations")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/violations.png")

    page.goto("http://localhost:3000/exceptions")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/exceptions.png")

    page.goto("http://localhost:3000/controls")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/controls.png")

    page.goto("http://localhost:3000/audit")
    page.wait_for_timeout(1000)
    page.screenshot(path="/home/jules/verification/screenshots/audit.png")

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
