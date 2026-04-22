import Link from 'next/link';

export const dynamic = 'force-dynamic';

export default async function PolicyForgeDashboardPage() {
  return (
    <div className="space-y-6 p-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-slate-900">PolicyForge</h1>
        <p className="text-slate-500 mt-2">AI Policy Management Platform</p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <Link href="/policies" className="block p-6 bg-white border rounded-lg shadow hover:bg-slate-50">
          <h2 className="text-xl font-semibold">Policies</h2>
          <p className="text-slate-500 mt-2">Manage, version, and view policies.</p>
        </Link>
        <Link href="/gaps" className="block p-6 bg-white border rounded-lg shadow hover:bg-slate-50">
          <h2 className="text-xl font-semibold">Policy Gaps</h2>
          <p className="text-slate-500 mt-2">Review gaps detected via AI from incidents.</p>
        </Link>
        <Link href="/categories" className="block p-6 bg-white border rounded-lg shadow hover:bg-slate-50">
          <h2 className="text-xl font-semibold">Categories</h2>
          <p className="text-slate-500 mt-2">Manage policy categories.</p>
        </Link>
      </div>
    </div>
  );
}
