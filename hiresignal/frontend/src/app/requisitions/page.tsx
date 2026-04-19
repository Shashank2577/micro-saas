import RequisitionList from '@/components/RequisitionList';

export default function RequisitionsPage() {
  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold mb-4">Requisitions</h1>
      <RequisitionList />
    </main>
  );
}
