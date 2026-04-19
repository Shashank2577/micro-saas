import CandidateList from '@/components/CandidateList';

export default function CandidatesPage() {
  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold mb-4">Candidates</h1>
      <CandidateList />
    </main>
  );
}
