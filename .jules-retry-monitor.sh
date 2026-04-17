#!/bin/bash
set -euo pipefail

REPO="Shashank2577/micro-saas"
DISPATCH_LOG="/Users/shashanksaxena/Documents/Personal/Code/micro-saas/.jules-30-apps-dispatch.log"
FAILED_APPS=("featureflagai" "billingai" "constructioniq")

echo "🔄 Jules Automatic Retry Monitor"
echo "Checking for capacity every 10 minutes..."
echo ""

while true; do
  # Count running sessions
  running=$(jules remote list --session 2>/dev/null | grep -c "^ID:" || echo "0")
  available=$((15 - running))
  
  echo "[$(date '+%H:%M:%S')] Sessions: $running/15 running, $available slots available"
  
  if [[ $available -gt 0 ]]; then
    echo "🚀 Capacity detected! Retrying failed apps..."
    
    for app in "${FAILED_APPS[@]}"; do
      # Check if already has session ID
      current=$(grep "^${app}=" "$DISPATCH_LOG" | cut -d= -f2)
      
      if [[ "$current" == "UNPARSED" ]]; then
        echo "  🛰  $app..."
        
        out=$(jules remote new --repo "$REPO" --session "Build $app autonomously" 2>&1)
        sid=$(echo "$out" | grep "^ID:" | awk '{print $2}' || true)
        
        if [[ -n "$sid" ]]; then
          echo "     ✅ session=$sid"
          sed -i '' "s/^${app}=.*/${app}=${sid}/" "$DISPATCH_LOG"
        fi
        
        sleep 5
      fi
    done
  fi
  
  # Check if all done
  unparsed=$(grep "=UNPARSED$" "$DISPATCH_LOG" | wc -l)
  if [[ $unparsed -eq 0 ]]; then
    echo "✅ All 30 apps now have session IDs!"
    exit 0
  fi
  
  sleep 600  # Check every 10 minutes
done
