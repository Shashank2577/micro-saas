#!/bin/bash
set -euo pipefail

REPO="Shashank2577/micro-saas"
DISPATCH_LOG="/Users/shashanksaxena/Documents/Personal/Code/micro-saas/.jules-30-apps-dispatch.log"
RETRY_LOG="/Users/shashanksaxena/Documents/Personal/Code/micro-saas/.jules-retry-3-apps.log"

echo "[$(date)] Starting monitor for final 3 apps..." >> "$RETRY_LOG"

while true; do
  echo "[$(date)] Checking for available capacity..." >> "$RETRY_LOG"
  
  # Count active sessions
  active=$(jules remote list --session 2>/dev/null | grep -c "IN_PROGRESS\|PLANNING" || echo "0")
  
  echo "[$(date)] Active sessions: $active/15" >> "$RETRY_LOG"
  
  if [[ $active -lt 12 ]]; then
    echo "[$(date)] Capacity available! Retrying failed apps..." >> "$RETRY_LOG"
    
    for app in featureflagai billingai constructioniq; do
      current=$(grep "^${app}=" "$DISPATCH_LOG" | cut -d= -f2)
      
      if [[ "$current" == "UNPARSED" ]]; then
        echo "[$(date)] Retrying $app..." >> "$RETRY_LOG"
        
        out=$(jules remote new --repo "$REPO" --session "Build $app autonomously" 2>&1)
        sid=$(echo "$out" | grep "^ID:" | awk '{print $2}' || true)
        
        if [[ -n "$sid" ]]; then
          echo "[$(date)] ✅ $app dispatched: session=$sid" >> "$RETRY_LOG"
          sed -i '' "s/^${app}=.*/${app}=${sid}/" "$DISPATCH_LOG"
        else
          echo "[$(date)] ❌ $app failed again" >> "$RETRY_LOG"
        fi
        
        sleep 30
      fi
    done
    
    # Check if all 3 are done
    unparsed=$(grep "UNPARSED" "$DISPATCH_LOG" | wc -l)
    if [[ $unparsed -eq 0 ]]; then
      echo "[$(date)] ✅ All 30 apps dispatched!" >> "$RETRY_LOG"
      break
    fi
  fi
  
  echo "[$(date)] Waiting 5 minutes before next check..." >> "$RETRY_LOG"
  sleep 300
done
