const { chromium } = require('playwright');
const { spawn } = require('child_process');
const fs = require('fs');

async function waitPort(port, timeout = 60000) {
    const start = Date.now();
    while (Date.now() - start < timeout) {
        try {
            await fetch(`http://localhost:${port}`);
            return true;
        } catch (e) {
            await new Promise(r => setTimeout(r, 1000));
        }
    }
    return false;
}

async function main() {
    const appDir = process.argv[2];
    if (!appDir) {
        console.error('Please provide an app directory');
        process.exit(1);
    }

    if (!fs.existsSync(`${appDir}/backend/pom.xml`) || !fs.existsSync(`${appDir}/frontend/package.json`)) {
        console.error(`Invalid app directory: ${appDir}`);
        process.exit(1);
    }

    console.log(`Installing dependencies for frontend ${appDir}...`);
    await new Promise((resolve, reject) => {
        const npmInstall = spawn('npm', ['install'], {
            cwd: `${appDir}/frontend`,
            stdio: 'inherit'
        });
        npmInstall.on('close', code => {
            if (code === 0) resolve();
            else reject(new Error(`npm install failed with code ${code}`));
        });
    });

    console.log(`Starting backend for ${appDir}...`);
    const backendProcess = spawn('mvn', ['spring-boot:run'], {
        cwd: `${appDir}/backend`,
        stdio: 'inherit',
        detached: true
    });

    console.log(`Starting frontend for ${appDir}...`);
    const frontendProcess = spawn('npm', ['run', 'dev'], {
        cwd: `${appDir}/frontend`,
        stdio: 'inherit',
        detached: true
    });

    try {
        console.log('Waiting for frontend (port 3000)...');
        if (!await waitPort(3000, 120000)) {
            throw new Error('Frontend failed to start within timeout');
        }

        console.log('Capturing screenshot...');
        const browser = await chromium.launch();
        const page = await browser.newPage();
        await page.goto('http://localhost:3000');
        await page.waitForLoadState('networkidle');

        if (!fs.existsSync('screenshots')) {
            fs.mkdirSync('screenshots');
        }
        await page.screenshot({ path: `screenshots/${appDir}.png`, fullPage: true });
        console.log(`Screenshot saved to screenshots/${appDir}.png`);

        await browser.close();
    } catch (e) {
        console.error('Error during capture:', e);
    } finally {
        console.log('Cleaning up processes...');
        try { process.kill(-backendProcess.pid); } catch(e) {}
        try { process.kill(-frontendProcess.pid); } catch(e) {}
    }
}

main();
