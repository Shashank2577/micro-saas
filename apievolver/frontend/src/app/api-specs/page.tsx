import { ApiSpecList } from '@/components/ApiSpecList';

export default function ApiSpecsPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">API Specs</h1>
      <ApiSpecList />
    </div>
  );
}
