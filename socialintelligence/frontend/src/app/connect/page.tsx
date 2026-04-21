"use client";
export default function Connect() {
  const platforms = ['INSTAGRAM', 'TIKTOK', 'YOUTUBE', 'TWITTER', 'LINKEDIN'];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Connect Platforms</h1>
      <div className="grid grid-cols-2 gap-4">
        {platforms.map(p => (
          <button key={p} className="bg-blue-500 text-white p-4 rounded text-xl font-bold hover:bg-blue-600">
            Connect {p}
          </button>
        ))}
      </div>
    </div>
  );
}
