import { api, VideoAsset } from '@/lib/api';
import { AppCard } from '@/components/AppCard';

export default async function DashboardPage() {
  let videos: VideoAsset[] = [];

  try {
    videos = await api.videos.list();
  } catch {
    // Backend may not be running in build/static environments
  }

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">VideoNarrator</h1>
          <p className="text-gray-500 mt-1">AI video intelligence platform — {videos.length} videos processed</p>
        </div>

        {/* Videos grid */}
        <section className="mb-8">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Your Videos</h2>
          {videos.length === 0 ? (
            <p className="text-gray-400 text-sm">No videos uploaded yet. Use the API to upload your first video.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
              {videos.map((video) => <AppCard key={video.id} video={video} />)}
            </div>
          )}
        </section>
      </div>
    </main>
  );
}
