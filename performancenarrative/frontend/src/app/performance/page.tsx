import Link from 'next/link';

export default function PerformanceDashboard() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Performance Dashboard</h1>
      <ul className="space-y-2">
        <li><Link href="/performance/review-cycles" className="text-blue-600 hover:underline">Review Cycles</Link></li>
        <li><Link href="/performance/employee-reviews" className="text-blue-600 hover:underline">Employee Reviews</Link></li>
        <li><Link href="/performance/calibration-notes" className="text-blue-600 hover:underline">Calibration Notes</Link></li>
        <li><Link href="/performance/goal-evidences" className="text-blue-600 hover:underline">Goal Evidences</Link></li>
        <li><Link href="/performance/narrative-drafts" className="text-blue-600 hover:underline">Narrative Drafts</Link></li>
        <li><Link href="/performance/feedback-items" className="text-blue-600 hover:underline">Feedback Items</Link></li>
      </ul>
    </div>
  );
}
