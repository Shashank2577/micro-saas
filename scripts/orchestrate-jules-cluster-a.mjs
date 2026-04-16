#!/usr/bin/env node
/**
 * Jules Orchestrator for Cluster A App Builds
 *
 * Executes the .jules-plan-cluster-a.json plan by dispatching
 * all 5 apps in parallel (max 5 concurrent Jules sessions).
 */

import { jules } from '@google/jules-sdk';
import { readFileSync } from 'fs';
import { join } from 'path';

const PLAN_FILE = '.jules-plan-cluster-a.json';
const REPO_ROOT = process.cwd();
const PLAN_PATH = join(REPO_ROOT, PLAN_FILE);

console.log('🚀 Jules Orchestrator for Cluster A');
console.log(`📋 Plan: ${PLAN_FILE}`);
console.log(`🌐 Repo: Shashank2577/micro-saas`);
console.log(`📦 Branch: main`);
console.log('');

// Load plan
const plan = JSON.parse(readFileSync(PLAN_PATH, 'utf8'));
const batch = plan.batches[0]; // Cluster A is all in one batch

console.log(`📦 Batch: ${batch.name}`);
console.log(`📝 Rationale: ${batch.rationale}`);
console.log(`🔄 Concurrency: 5 (Jules max)`);
console.log('');

// Create sessions for all tasks
async function dispatchApps() {
  const sessions = await jules.all(
    batch.tasks,
    (task) => ({
      prompt: task.prompt,
      source: { github: plan.repo, baseBranch: plan.branch },
      title: task.title,
      automationMode: 'AUTO_CREATE_PR',
    }),
    { concurrency: 5, stopOnError: false, delayMs: 1000 }
  );

  console.log(`✅ Dispatched ${sessions.length} Jules sessions`);
  console.log('');
  console.log('Session IDs:');
  sessions.forEach((session, i) => {
    console.log(`  ${i + 1}. ${batch.tasks[i].id}`);
    console.log(`     Session: ${session.sessionId}`);
  });
  console.log('');

  // Wait for all to complete
  console.log('⏳ Waiting for all sessions to complete...');
  console.log('   (This may take 4-6 hours per app)');
  console.log('');

  const results = await Promise.all(sessions.map(s => s.result()));

  console.log('');
  console.log('📊 Results:');
  results.forEach((r, i) => {
    const task = batch.tasks[i];
    const status = r.state === 'COMPLETED' ? '✅' : '❌';
    const prUrl = r.pullRequest?.url || 'N/A';

    console.log(`  ${status} ${task.id}`);
    console.log(`     State: ${r.state}`);
    console.log(`     PR: ${prUrl}`);

    if (r.state !== 'COMPLETED') {
      console.log(`     Error: ${r.error?.message || 'Unknown error'}`);
    }
    console.log('');
  });

  // Summary
  const completed = results.filter(r => r.state === 'COMPLETED').length;
  const failed = results.filter(r => r.state !== 'COMPLETED').length;

  console.log('📈 Summary:');
  console.log(`  ✅ Completed: ${completed}/${results.length}`);
  console.log(`  ❌ Failed: ${failed}/${results.length}`);
  console.log('');

  if (completed > 0) {
    console.log('🎉 Next Steps:');
    console.log('  1. Review PRs at: https://github.com/Shashank2577/micro-saas/pulls');
    console.log('  2. Test each app locally:');
    batch.tasks.forEach((task, i) => {
      if (results[i].state === 'COMPLETED') {
        console.log(`     cd ${task.id}/backend && mvn spring-boot:run`);
      }
    });
    console.log('  3. Register each app with Nexus Hub:');
    batch.tasks.forEach((task, i) => {
      if (results[i].state === 'COMPLETED') {
        console.log(`     ./tools/register-app.sh ${task.id}`);
      }
    });
    console.log('');
  }

  if (failed > 0) {
    console.log('⚠️  Failed sessions:');
    console.log('  Check Jules UI for details: https://jules.google.com');
    process.exit(1);
  }
}

// Run orchestrator
dispatchApps().catch(err => {
  console.error('❌ Orchestrator failed:', err);
  process.exit(1);
});
