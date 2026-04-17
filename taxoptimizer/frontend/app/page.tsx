import Link from 'next/link'

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24 bg-slate-50">
      <div className="z-10 w-full max-w-5xl items-center justify-between font-mono text-sm lg:flex">
        <h1 className="text-4xl font-bold text-slate-900">Tax Optimizer Platform</h1>
        <div className="flex gap-4">
          <Link href="/dashboard" className="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700">
            Go to Dashboard
          </Link>
        </div>
      </div>
      <div className="mt-12 max-w-3xl text-center">
        <p className="text-xl text-slate-600">AI-driven tax planning, estimation, and optimization.</p>
      </div>
    </main>
  )
}
