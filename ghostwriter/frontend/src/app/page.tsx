export default function Home() {
  return (
    <main className="min-h-screen bg-white p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold mb-4">GhostWriter</h1>
        <p className="text-gray-600 mb-8">AI long-form writing co-pilot trained on your personal voice.</p>

        <div className="bg-gray-50 p-6 rounded-lg border border-gray-200">
          <h2 className="text-2xl font-semibold mb-4">Writing Sessions</h2>
          <p className="text-gray-500 text-sm">Start a new writing session or continue an existing one.</p>
          <div className="mt-4">
            <button className="bg-black text-white px-4 py-2 rounded-md hover:bg-gray-800 transition-colors">
              New Session
            </button>
          </div>
        </div>

        <div className="mt-8 grid grid-cols-2 gap-6">
          <div className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h2 className="text-xl font-semibold mb-2">Voice Models</h2>
            <p className="text-gray-500 text-sm mb-4">Manage your trained voice models.</p>
            <button className="text-sm bg-white border border-gray-300 px-3 py-1 rounded hover:bg-gray-50 transition-colors">
              Train New Model
            </button>
          </div>

          <div className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h2 className="text-xl font-semibold mb-2">Corpus Library</h2>
            <p className="text-gray-500 text-sm mb-4">View and add documents to your training corpus.</p>
            <button className="text-sm bg-white border border-gray-300 px-3 py-1 rounded hover:bg-gray-50 transition-colors">
              Upload Document
            </button>
          </div>
        </div>
      </div>
    </main>
  )
}
