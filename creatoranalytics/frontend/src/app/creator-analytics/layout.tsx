export default function CreatorAnalyticsLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="flex min-h-screen flex-col">
      <header className="p-4 border-b">
        <h1 className="text-xl font-bold">Creator Analytics</h1>
      </header>
      <main className="flex-1">{children}</main>
    </div>
  )
}
