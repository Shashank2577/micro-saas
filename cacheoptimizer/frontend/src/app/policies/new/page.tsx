"use client";
import { PolicyForm } from '@/src/components/PolicyForm';
import { useRouter } from 'next/navigation';

export default function NewPolicyPage() {
  const router = useRouter();

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Create New Policy</h1>
      <PolicyForm onSuccess={() => router.push('/policies')} />
    </div>
  );
}
