import Link from 'next/link';

export default function Home() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">DataStoryTeller Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <Link href="/data-sources">
          <div className="p-6 border rounded-lg hover:shadow-lg transition">
            <h2 className="text-xl font-semibold mb-2">Data Sources</h2>
            <p className="text-gray-600">Manage connections to Snowflake, BQ, Postgres, etc.</p>
          </div>
        </Link>
        <Link href="/datasets">
          <div className="p-6 border rounded-lg hover:shadow-lg transition">
            <h2 className="text-xl font-semibold mb-2">Datasets</h2>
            <p className="text-gray-600">Register datasets and configure sync schedules.</p>
          </div>
        </Link>
        <Link href="/narratives">
          <div className="p-6 border rounded-lg hover:shadow-lg transition">
            <h2 className="text-xl font-semibold mb-2">Narratives</h2>
            <p className="text-gray-600">View and generate AI narratives from your data.</p>
          </div>
        </Link>
        <Link href="/schedules">
          <div className="p-6 border rounded-lg hover:shadow-lg transition">
            <h2 className="text-xl font-semibold mb-2">Schedules</h2>
            <p className="text-gray-600">Automate narrative delivery via Slack, Email or Webhook.</p>
          </div>
        </Link>
        <Link href="/templates">
          <div className="p-6 border rounded-lg hover:shadow-lg transition">
            <h2 className="text-xl font-semibold mb-2">Templates</h2>
            <p className="text-gray-600">Manage narrative structure and prompt templates.</p>
          </div>
        </Link>
      </div>
    </div>
  );
}
