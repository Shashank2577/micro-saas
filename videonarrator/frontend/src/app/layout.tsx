export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <div className="min-h-screen bg-gray-50 flex flex-col">
          <header className="bg-white shadow-sm py-4 px-6 border-b">
            <h1 className="text-xl font-bold text-gray-800">VideoNarrator</h1>
          </header>
          <main className="flex-grow p-6">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
