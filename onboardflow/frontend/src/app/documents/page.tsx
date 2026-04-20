import DocumentsList from '../../components/DocumentsList';

export default function DocumentsPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Documents Management</h1>
      <DocumentsList />
    </main>
  );
}
