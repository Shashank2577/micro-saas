import PipelineMetricList from '@/components/PipelineMetricList';

export default function PipelinePage() {
  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold mb-4">Pipeline Metrics</h1>
      <PipelineMetricList />
    </main>
  );
}
