import { SdkArtifactList } from '@/components/SdkArtifactList';

export default function SdkArtifactsPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">SDK Artifacts</h1>
      <SdkArtifactList />
    </div>
  );
}
