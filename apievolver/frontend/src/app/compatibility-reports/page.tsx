import { CompatibilityReportList } from '@/components/CompatibilityReportList';

export default function CompatibilityReportsPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Compatibility Reports</h1>
      <CompatibilityReportList />
    </div>
  );
}
