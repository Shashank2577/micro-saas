import { BreakingChangeList } from '@/components/BreakingChangeList';

export default function BreakingChangesPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Breaking Changes</h1>
      <BreakingChangeList />
    </div>
  );
}
