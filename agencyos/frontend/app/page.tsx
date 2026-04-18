export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div className="z-10 w-full max-w-5xl items-center justify-between font-mono text-sm lg:flex">
        <h1 className="text-4xl font-bold mb-8">AgencyOS Dashboard</h1>
        <div className="flex gap-4">
          <a href="/clients" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Manage Clients
          </a>
        </div>
      </div>
    </main>
  );
}
