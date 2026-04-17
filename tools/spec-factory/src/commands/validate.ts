import { Command, Flags } from '@oclif/core';
import { SpecSchema } from '../shared/schemas';
import { LitellmClient } from '../shared/litellm';
import { getSpinner } from '../shared/spinner';
import { getChalk } from '../shared/chalk';
import { logger } from '../shared/logger';
import fs from 'fs';
import path from 'path';

export default class Validate extends Command {
  static description = 'Quality checks for generated specs';

  static flags = {
    file: Flags.string({ char: 'f', description: 'Path to the spec file', required: true })
  };

  async run(): Promise<void> {
    const { flags } = await this.parse(Validate);
    const chalk = await getChalk();
    const spinner = await getSpinner(`Validating spec file ${flags.file}...`);
    
    spinner.start();
    try {
      const fullPath = path.resolve(flags.file);
      if (!fs.existsSync(fullPath)) {
        throw new Error(`File not found: ${fullPath}`);
      }

      const fileContent = fs.readFileSync(fullPath, 'utf8');
      const parsedJson = JSON.parse(fileContent);

      // 1. Zod Schema Validation
      const result = SpecSchema.safeParse(parsedJson);
      if (!result.success) {
        throw new Error(`Schema validation failed: ${result.error.message}`);
      }

      // 2. LLM-based Quality Check
      const client = new LitellmClient();
      const systemPrompt = `You are a strict technical reviewer. Review the following JSON spec for an application. Determine if it is high quality, clear, and actionable. Respond with ONLY 'PASS' or 'FAIL' followed by a brief reason.`;
      
      const response = await client.generateText(fileContent, 'gpt-4o-mini', systemPrompt);

      if (response.startsWith('PASS')) {
        spinner.succeed(`Validation passed: ${chalk.green(response)}`);
      } else {
        spinner.fail(`Validation failed quality check: ${chalk.red(response)}`);
      }

    } catch (error: any) {
      spinner.fail('Validation encountered an error');
      logger.error(error.message);
      this.exit(1);
    }
  }
}
