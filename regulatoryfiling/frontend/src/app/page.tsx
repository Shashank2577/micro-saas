export default function Home() {
  return (
    <main className="p-8">
      <h1 className="text-3xl font-bold mb-4">RegulatoryFiling Dashboard</h1>
      <ul className="list-disc pl-5">
        <li><a href="/obligations" className="text-blue-500 hover:underline">Filing Obligations</a></li>
        <li><a href="/schedules" className="text-blue-500 hover:underline">Jurisdiction Schedules</a></li>
        <li><a href="/packets" className="text-blue-500 hover:underline">Submission Packets</a></li>
        <li><a href="/deadlines" className="text-blue-500 hover:underline">Filing Deadlines</a></li>
        <li><a href="/validations" className="text-blue-500 hover:underline">Validation Checks</a></li>
        <li><a href="/receipts" className="text-blue-500 hover:underline">Submission Receipts</a></li>
      </ul>
    </main>
  )
}
