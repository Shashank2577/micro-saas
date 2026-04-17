#!/usr/bin/env bash
# jules-monitor.sh — Auto-merge completed Jules PRs and dispatch from queue
set -uo pipefail

REPO="Shashank2577/micro-saas"
REPO_ROOT="/Users/shashanksaxena/Documents/Personal/Code/micro-saas"
SESSION_DIR="$REPO_ROOT/.omc/sessions"
PROTOCOL_FILE="$REPO_ROOT/JULES_AUTONOMOUS_BUILD_PROTOCOL.md"
QUEUE="$REPO_ROOT/.jules-dispatch-queue.txt"
LOG="$REPO_ROOT/.jules-monitor.log"
echo "[$(date)] Monitor run started" >> "$LOG"

# ── 1. Fetch new branches ────────────────────────────────────────
echo "=== Fetching remote branches ==="
new_branches=$(git -C "$REPO_ROOT" fetch --all 2>&1 | grep "new branch" || true)
echo "${new_branches:-No new branches}"

# ── 2. Merge clean PRs ──────────────────────────────────────────
echo ""
echo "=== Checking open PRs ==="
prs=$(gh pr list --limit 30 --json number,title,mergeable,mergeStateStatus,isDraft 2>/dev/null)
merged=0

if [[ -n "$prs" && "$prs" != "[]" ]]; then
  echo "$prs" | python3 -c "
import json,sys
prs=json.load(sys.stdin)
for p in prs:
    status='DRAFT' if p['isDraft'] else p['mergeStateStatus']
    print(f\"PR#{p['number']} [{status}] {p['title'][:60]}\")
"
  # Merge all clean, non-draft PRs
  while IFS= read -r pr_num; do
    echo "Marking PR #$pr_num ready..."
    gh pr ready "$pr_num" 2>/dev/null || true
    echo "Merging PR #$pr_num..."
    if gh pr merge "$pr_num" --merge --delete-branch 2>/dev/null; then
      echo "  ✅ PR #$pr_num merged"
      ((merged++)) || true
    fi
  done < <(echo "$prs" | python3 -c "
import json,sys
prs=json.load(sys.stdin)
for p in prs:
    if not p['isDraft'] and p['mergeStateStatus'] in ['CLEAN','MERGEABLE'] and p['mergeable'] == 'MERGEABLE':
        print(p['number'])
")
else
  echo "No open PRs"
fi
echo "Merged: $merged PRs"

# ── 3. Check session states ──────────────────────────────────────
echo ""
echo "=== Jules Session States ==="
session_output=$(jules remote list --session 2>&1)
micro=$(echo "$session_output" | grep "micro-sa" || true)
in_progress=$(echo "$micro" | grep -c "In Progress\|Planning" || echo 0)
awaiting=$(echo "$micro" | grep -c "Awaiting" || echo 0)
completed=$(echo "$micro" | grep -c "Completed" || echo 0)
paused=$(echo "$micro" | grep -c "Paused" || echo 0)
total_active=$((in_progress + awaiting + paused))
echo "In Progress: $in_progress | Awaiting: $awaiting | Paused: $paused | Completed: $completed"
echo "Total active slots: $total_active"

# ── 4. Dispatch from queue if capacity available ─────────────────
echo ""
echo "=== Dispatching from queue ==="
if [[ ! -f "$QUEUE" ]] || [[ ! -s "$QUEUE" ]]; then
  echo "Queue is empty — nothing to dispatch"
  echo "[$(date)] Queue empty" >> "$LOG"
  exit 0
fi

queue_count=$(grep -c . "$QUEUE" 2>/dev/null || echo 0)
echo "Queue has $queue_count apps remaining"

PROTOCOL=$(cat "$PROTOCOL_FILE")
dispatched=0
failed_apps=()

while IFS= read -r app; do
  [[ -z "$app" || "$app" == \#* ]] && continue
  [ -d "$REPO_ROOT/$app" ] && echo "⏭  $app already built" && continue

  spec_json=""
  for f in "$SESSION_DIR/$app"-*.json "$SESSION_DIR/$app.json"; do
    [[ -f "$f" ]] && spec_json="$f" && break
  done
  [[ -z "$spec_json" ]] && echo "⚠️  No spec: $app" && failed_apps+=("$app") && continue

  spec_text=$(python3 -c "
import json
d=json.load(open('$spec_json'))
desc=d.get('task-description','')
spec=d.get('detailed-spec',{})
reqs='\n'.join('- '+r for r in spec.get('requirements',[]))
ac='\n'.join('- '+a for a in spec.get('acceptance-criteria',[]))
deps='\n'.join('- '+x for x in d.get('dependencies',{}).get('backend-stack',[]))
port=d.get('app-port',8080)
print(f'App: $app\nPort: {port}\nDescription: {desc}\n\nRequirements:\n{reqs}\n\nAcceptance Criteria:\n{ac}\n\nStack:\n{deps}')
" 2>/dev/null)

  output=$(jules remote new --repo "$REPO" --session "Jules: build $app completely autonomously. No feedback will come. Push after each phase. Create PR at end. Tests must pass. Branch: feature/${app}-implementation.

APP SPEC:
${spec_text}

PROTOCOL:
${PROTOCOL}

BEGIN NOW: spec → implement → test → PR." 2>&1)

  if echo "$output" | grep -qE "FAILED_PRECONDITION|failed to create|status 400|status 429"; then
    echo "🚫 Quota full at $app — will retry next run"
    failed_apps+=("$app")
    # Preserve remaining queue entries
    found=0
    while IFS= read -r r; do
      [[ -z "$r" || "$r" == \#* ]] && continue
      [[ "$r" == "$app" ]] && found=1 && continue
      [[ $found -eq 1 ]] && failed_apps+=("$r")
    done < "$QUEUE"
    break
  fi

  sid=$(echo "$output" | grep -oE 'ID: [0-9]+' | grep -oE '[0-9]+' | head -1)
  [[ -z "$sid" ]] && sid=$(echo "$output" | grep -oE '[0-9]{15,}' | head -1)
  echo "✅ $app → ${sid:-dispatched}"
  echo "[$(date)] dispatched $app = ${sid:-ok}" >> "$LOG"
  ((dispatched++)) || true
  sleep 1
done < "$QUEUE"

# Update queue
if [ ${#failed_apps[@]} -gt 0 ]; then
  printf '%s\n' "${failed_apps[@]}" > "$QUEUE"
  echo ""
  echo "Queue updated: ${#failed_apps[@]} remaining"
else
  : > "$QUEUE"
  echo "🎉 All apps dispatched!"
fi

echo ""
echo "=== Monitor Summary ==="
echo "PRs merged: $merged | New dispatches: $dispatched | Queue remaining: ${#failed_apps[@]}"
echo "[$(date)] Done — merged=$merged dispatched=$dispatched remaining=${#failed_apps[@]}" >> "$LOG"
