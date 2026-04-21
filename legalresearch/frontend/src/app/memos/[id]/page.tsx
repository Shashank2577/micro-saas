"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../../lib/api';
import MemoEditor from '../../../components/MemoEditor';
import WorkflowStatus from '../../../components/WorkflowStatus';

export default function MemoDetailPage({ params }: { params: { id: string } }) {
  const [memo, setMemo] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMemo = async () => {
      try {
        const data = await api.getMemo(params.id);
        setMemo(data);
      } catch (error) {
        console.error("Error fetching memo details", error);
      } finally {
        setLoading(false);
      }
    };
    fetchMemo();
  }, [params.id]);

  const handleSaveSections = async (sections: any[]) => {
    try {
      await api.updateMemo(params.id, { sections });
    } catch (error) {
      console.error("Error saving memo", error);
    }
  };

  if (loading) return <div>Loading memo details...</div>;
  if (!memo) return <div>Memo not found.</div>;

  const defaultSections = [
    { id: '1', title: 'Introduction', content: '' },
    { id: '2', title: 'Facts', content: '' },
    { id: '3', title: 'Analysis', content: '' },
    { id: '4', title: 'Conclusion', content: '' },
  ];

  type WorkflowStatusType = 'completed' | 'current' | 'upcoming';

  const workflowSteps: { id: string; name: string; status: WorkflowStatusType }[] = [
    { id: '1', name: 'Drafting', status: memo.status === 'draft' ? 'current' : 'completed' },
    { id: '2', name: 'Review', status: memo.status === 'review' ? 'current' : memo.status === 'approved' ? 'completed' : 'upcoming' },
    { id: '3', name: 'Approved', status: memo.status === 'approved' ? 'completed' : 'upcoming' },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow">
        <div>
          <h1 className="text-2xl font-bold">{memo.title || 'Untitled Memo'}</h1>
          <p className="text-sm text-gray-500">Status: {memo.status}</p>
        </div>
        <div className="flex space-x-2">
          {memo.status === 'draft' && (
            <button className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">
              Submit for Review
            </button>
          )}
          {memo.status === 'review' && (
            <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
              Approve
            </button>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <MemoEditor
            initialSections={memo.sections?.length > 0 ? memo.sections : defaultSections}
            onSave={handleSaveSections}
          />
        </div>

        <div className="space-y-6">
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Approval Workflow</h2>
            <WorkflowStatus steps={workflowSteps} />
          </div>

          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Review Comments</h2>
            <div className="space-y-4">
              {memo.comments?.map((comment: any) => (
                <div key={comment.id} className="border-b pb-2">
                  <p className="text-sm font-semibold">{comment.author}</p>
                  <p className="text-gray-700 text-sm mt-1">{comment.text}</p>
                </div>
              ))}
              {(!memo.comments || memo.comments.length === 0) && (
                <p className="text-gray-500">No comments yet.</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
