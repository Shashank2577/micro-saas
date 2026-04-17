import CompetitorList from '../components/CompetitorList';

export default function Home() {
  return (
    <main className="min-h-screen p-24">
      <h1 className="text-4xl font-bold mb-8">CompetitorRadar Dashboard</h1>
      <CompetitorList />
    </main>
  );
}
