import Link from 'next/link';

export default function DashboardPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-8">FinanceNarrator Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <Link href="/narrative-requests" className="p-6 border rounded shadow hover:bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Narrative Requests</h2>
          <p className="text-gray-600">Manage executive-grade financial narrative generation requests.</p>
        </Link>
        <Link href="/narrative-sections" className="p-6 border rounded shadow hover:bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Narrative Sections</h2>
          <p className="text-gray-600">Manage individual sections of narrative documents.</p>
        </Link>
        <Link href="/supporting-metrics" className="p-6 border rounded shadow hover:bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Supporting Metrics</h2>
          <p className="text-gray-600">Manage structured financial data subsets supporting narratives.</p>
        </Link>
        <Link href="/tone-profiles" className="p-6 border rounded shadow hover:bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Tone Profiles</h2>
          <p className="text-gray-600">Manage output personas (e.g. optimistic, conservative, board-prep).</p>
        </Link>
        <Link href="/approval-reviews" className="p-6 border rounded shadow hover:bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Approval Reviews</h2>
          <p className="text-gray-600">Manage human-in-the-loop review and feedback workflows.</p>
        </Link>
        <Link href="/exports" className="p-6 border rounded shadow hover:bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Export Artifacts</h2>
          <p className="text-gray-600">Manage generated PDF/PPTX/Docx export files.</p>
        </Link>
      </div>
    </div>
  );
}
