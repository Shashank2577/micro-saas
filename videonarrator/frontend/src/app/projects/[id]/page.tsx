'use client';
import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import api from '@/lib/api';
import VideoPlayer from '@/components/VideoPlayer';
import TimelineEditor from '@/components/TimelineEditor';
import VoiceSelector from '@/components/VoiceSelector';
import SubtitleList from '@/components/SubtitleList';

export default function ProjectEditorPage() {
  const params = useParams();
  const projectId = params.id as string;

  const [project, setProject] = useState<any>(null);
  const [transcriptions, setTranscriptions] = useState<any[]>([]);
  const [subtitles, setSubtitles] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentTime, setCurrentTime] = useState(0);

  useEffect(() => {
    async function loadData() {
      try {
        const pRes = await api.get(`/projects/${projectId}`);
        setProject(pRes.data);

        const tRes = await api.get(`/projects/${projectId}/transcriptions`);
        const ts = tRes.data;
        setTranscriptions(ts);

        if (ts.length > 0) {
          const sRes = await api.get(`/transcriptions/${ts[0].id}/subtitles`);
          setSubtitles(sRes.data);
        }
      } catch (err) {
        console.error('Failed to load project editor data', err);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, [projectId]);

  const handleTranscribe = async () => {
    try {
      await api.post(`/projects/${projectId}/transcribe`, { languageCode: 'en' });
      alert('Transcription started');
      window.location.reload();
    } catch(err) {
      console.error(err);
      alert('Failed to start transcription');
    }
  };

  if (loading) return <div>Loading editor...</div>;
  if (!project) return <div>Project not found</div>;

  return (
    <div className="grid grid-cols-3 gap-6 h-[calc(100vh-100px)]">
      {/* Left Column: Video & Controls */}
      <div className="col-span-2 flex flex-col gap-4">
        <div className="bg-black rounded-lg overflow-hidden aspect-video relative">
          <VideoPlayer
            src={project.videoUrl}
            onTimeUpdate={(t) => setCurrentTime(t)}
          />
        </div>

        <div className="bg-white p-4 rounded-lg shadow flex gap-4">
           {transcriptions.length === 0 ? (
             <button onClick={handleTranscribe} className="bg-purple-600 text-white px-4 py-2 rounded">
               Start Transcription
             </button>
           ) : (
             <VoiceSelector projectId={projectId} transcriptionId={transcriptions[0].id} />
           )}
        </div>

        <div className="bg-white p-4 rounded-lg shadow flex-grow">
          <TimelineEditor subtitles={subtitles} currentTime={currentTime} />
        </div>
      </div>

      {/* Right Column: Subtitles */}
      <div className="col-span-1 bg-white p-4 rounded-lg shadow overflow-y-auto">
        <h3 className="font-semibold text-lg mb-4">Subtitles</h3>
        <SubtitleList
           subtitles={subtitles}
           currentTime={currentTime}
           onUpdateSubtitle={async (id, data) => {
             const res = await api.put(`/subtitles/${id}`, data);
             setSubtitles(subs => subs.map(s => s.id === id ? res.data : s));
           }}
        />
      </div>
    </div>
  );
}
