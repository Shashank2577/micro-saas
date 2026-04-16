import { VideoAsset, api } from '@/lib/api';

export function AppCard({ video }: { video: VideoAsset }) {
  return (
    <div className="bg-white border border-gray-200 rounded-lg p-5 hover:shadow-md transition-shadow">
      <div className="flex justify-between items-start mb-4">
        <div>
          <h3 className="text-lg font-medium text-gray-900">{video.title}</h3>
          <p className="text-xs text-gray-500 mt-1">ID: {video.id}</p>
        </div>
        <span className={`px-2 py-1 text-xs rounded-full font-medium ${
          video.status === 'DONE' ? 'bg-green-100 text-green-800' :
          video.status === 'UPLOADED' ? 'bg-blue-100 text-blue-800' :
          video.status === 'PROCESSING' ? 'bg-yellow-100 text-yellow-800' :
          'bg-gray-100 text-gray-800'
        }`}>
          {video.status}
        </span>
      </div>
      <div className="text-sm text-gray-600 space-y-2 mb-4">
        <p><span className="font-medium text-gray-900">URL:</span> <span className="truncate block">{video.fileUrl}</span></p>
        <p><span className="font-medium text-gray-900">Duration:</span> {video.durationSeconds}s</p>
      </div>
      <div className="pt-4 border-t border-gray-100 flex gap-2">
         {/* Placeholder for action buttons */}
      </div>
    </div>
  );
}
