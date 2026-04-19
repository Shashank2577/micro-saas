import { DeprecationNoticeList } from '@/components/DeprecationNoticeList';

export default function DeprecationNoticesPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Deprecation Notices</h1>
      <DeprecationNoticeList />
    </div>
  );
}
