import { exec } from 'child_process';
import util from 'util';
import path from 'path';

const execPromise = util.promisify(exec);

describe('CLI Commands Test', () => {
  const binPath = path.resolve(__dirname, '../bin/run.js');

  it('should display help', async () => {
    const { stdout } = await execPromise(`node ${binPath} help`);
    expect(stdout).toContain('spec-factory');
    expect(stdout).toContain('COMMANDS');
  });

  it('should require app name for generate', async () => {
    try {
      await execPromise(`node ${binPath} generate`);
    } catch (error: any) {
      expect(error.stderr || error.message).toContain('Missing required flag');
    }
  });
});
