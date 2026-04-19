'use client';

import React, { useEffect, useState } from 'react';
import { Table } from '@/components/Table';
import { Modal } from '@/components/Modal';

export default function ContractsPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    // Mock data for initial implementation
    setData([
      { id: '1', name: 'Master Service Agreement', status: 'ACTIVE' },
      { id: '2', name: 'Vendor NDA', status: 'DRAFT' }
    ]);
    setLoading(false);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Contracts</h1>
        <button onClick={() => setModalOpen(true)} className="bg-blue-600 text-white px-4 py-2 rounded">
          New Contract
        </button>
      </div>

      {loading ? <p>Loading...</p> : <Table data={data} columns={['id', 'name', 'status']} />}

      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title="Create Contract">
        <p>Form coming soon...</p>
      </Modal>
    </div>
  );
}
