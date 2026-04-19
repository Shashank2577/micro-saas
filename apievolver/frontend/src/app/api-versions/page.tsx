import { ApiVersionList } from '@/components/ApiVersionList';

export default function ApiVersionsPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">API Versions</h1>
      <ApiVersionList />
    </div>
  );
}
