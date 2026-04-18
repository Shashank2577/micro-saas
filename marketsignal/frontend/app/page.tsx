import SignalList from '../components/SignalList';
import PatternList from '../components/PatternList';

export default function Home() {
  return (
    <main className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-6xl mx-auto space-y-8">
        <header className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">MarketSignal Intelligence</h1>
          <p className="text-gray-600">AI-powered market signals and pattern detection.</p>
        </header>

        <section>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-bold text-gray-800">Detected Patterns</h2>
          </div>
          <PatternList />
        </section>

        <section>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-bold text-gray-800">Recent Signals</h2>
          </div>
          <SignalList />
        </section>
      </div>
    </main>
  );
}
