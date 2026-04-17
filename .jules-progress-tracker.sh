#!/bin/bash

# Track Phase completion for each app
REPO_ROOT="/Users/shashanksaxena/Documents/Personal/Code/micro-saas"
PROGRESS_LOG="${REPO_ROOT}/.jules-progress.log"

echo "🚀 Jules Progress Tracker Started" > "$PROGRESS_LOG"
echo "Timestamp: $(date)" >> "$PROGRESS_LOG"
echo "" >> "$PROGRESS_LOG"

# Apps we're tracking
APPS=(
  "authvault" "apigatekeeper" "notificationhub" "observabilitystack"
  "investtracker" "wealthplan" "cashflowanalyzer" "cashflowai" "budgetpilot"
  "hiresignal" "onboardflow" "peopleanalytics"
  "socialintelligence" "videonarrator" "brandvoice"
  "analyticsbuilder" "datacatalogai" "dataqualityos" "pipelineorchestrator"
  "dealbrain" "pricingintelligence" "revopsai"
  "supportintelligence" "knowledgevault" "contextlayer" "dataroomai"
  "healthcaredocai"
)

while true; do
  {
    echo ""
    echo "=== Progress Check: $(date '+%Y-%m-%d %H:%M:%S') ==="
    
    completed_apps=0
    for app in "${APPS[@]}"; do
      app_dir="${REPO_ROOT}/${app}"
      
      if [[ -d "${app_dir}/.julius" ]]; then
        phase_count=0
        [[ -f "${app_dir}/.julius/DETAILED_SPEC.md" ]] && phase_count=$((phase_count + 1))
        [[ -f "${app_dir}/.julius/IMPLEMENTATION_LOG.md" ]] && phase_count=$((phase_count + 1))
        [[ -d "${app_dir}/backend" ]] && phase_count=$((phase_count + 1))
        [[ -d "${app_dir}/frontend" ]] && phase_count=$((phase_count + 1))
        
        if [[ $phase_count -ge 2 ]]; then
          echo "✅ $app: Phase $phase_count (building)"
          ((completed_apps++))
        fi
      fi
    done
    
    echo ""
    echo "Summary: $completed_apps/${#APPS[@]} apps actively building"
    
  } >> "$PROGRESS_LOG"
  
  sleep 300  # Check every 5 minutes
done
