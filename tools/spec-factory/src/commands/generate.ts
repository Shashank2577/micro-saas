import { Command, Flags } from '@oclif/core';
import { LitellmClient } from '../shared/litellm';
import { getSpinner } from '../shared/spinner';
import { getChalk } from '../shared/chalk';
import { logger } from '../shared/logger';
import fs from 'fs';
import path from 'path';

export default class Generate extends Command {
  static description = 'AI-powered spec creation';

  static flags = {
    app: Flags.string({ char: 'a', description: 'Name of the application', required: true }),
    out: Flags.string({ char: 'o', description: 'Output directory', default: './specs' })
  };

  async run(): Promise<void> {
    const { flags } = await this.parse(Generate);
    const chalk = await getChalk();
    const spinner = await getSpinner(`Generating spec for ${flags.app}...`);
    
    spinner.start();
    try {
      const client = new LitellmClient();
      const systemPrompt = `You are an expert technical product manager. Generate a JSON specification for an application named ${flags.app}. Ensure the JSON output complies with this schema:
{
  "title": "string",
  "description": "string",
  "features": ["string"],
  "endpoints": [{"method": "string", "path": "string", "description": "string"}]
}
Output ONLY valid JSON. No markdown backticks.`;

      const response = await client.generateText(`Generate a spec for ${flags.app}`, 'gpt-4o-mini', systemPrompt);
      
      let parsedSpec;
      try {
        parsedSpec = JSON.parse(response.trim().replace(/^```json/, '').replace(/```$/, ''));
      } catch (e) {
        throw new Error(`Failed to parse LLM response as JSON. Response was: ${response}`);
      }

      if (!fs.existsSync(flags.out)) {
        fs.mkdirSync(flags.out, { recursive: true });
      }

      const filePath = path.join(flags.out, `${flags.app}-spec.json`);
      fs.writeFileSync(filePath, JSON.stringify(parsedSpec, null, 2));

      spinner.succeed(`Spec successfully generated: ${filePath}`);
    } catch (error: any) {
      spinner.fail('Failed to generate spec');
      logger.error(error.message);
    }
  }
}
