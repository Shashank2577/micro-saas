"use client";

import { useEffect, useState } from 'react';
import api from '../../../lib/api';
import RolloutControl from '../../../components/RolloutControl';
import SegmentList from '../../../components/SegmentList';

export default function FlagDetail({ params }: { params: { id: string } }) {
  const [flag, setFlag] = useState<any>(null);
  const [segments, setSegments] = useState<any[]>([]);

  useEffect(() => {
    api.get(`/flags/${params.id}`).then(res => {
      setFlag(res.data);
    });
    api.get(`/flags/${params.id}/segments`).then(res => {
      setSegments(res.data);
    });
  }, [params.id]);

  const handleRolloutSave = async (rolloutPct: number, enabled: boolean) => {
    await api.post(`/flags/${params.id}/rollout`, { rolloutPct, enabled });
    alert('Rollout updated');
  };

  if (!flag) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8 max-w-4xl">
      <h1 className="text-3xl font-bold mb-6">{flag.name} Settings</h1>

      <RolloutControl
        initialRollout={flag.rolloutPct}
        initialEnabled={flag.enabled}
        onSave={handleRolloutSave}
      />

      <SegmentList flagId={params.id} initialSegments={segments} />
    </div>
  );
}
