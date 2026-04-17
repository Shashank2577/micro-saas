export default function CompetitorDetail({ params }: { params: { id: string } }) {
  return (
    <main className="p-8">
      <h1 className="text-3xl font-bold">Competitor Detail</h1>
      <p>Viewing details for competitor: {params.id}</p>
    </main>
  );
}
