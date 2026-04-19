'use client';

import React, { useEffect, useState } from 'react';
import { Table } from '@/components/Table';
import { Modal } from '@/components/Modal';

export default function CounterpartiesPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    setData([
      { id: '1', name: 'Acme Corp', status: 'VERIFIED' },
    ]);
    setLoading(false);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Counterparties</h1>
        <button onClick={() => setModalOpen(true)} className="bg-blue-600 text-white px-4 py-2 rounded">
          New Profile
        </button>
      </div>
      {loading ? <p>Loading...</p> : <Table data={data} columns={['id', 'name', 'status']} />}
      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title="New Counterparty Profile">
        <p>Form coming soon...</p>
      </Modal>
    </div>
  );
}
