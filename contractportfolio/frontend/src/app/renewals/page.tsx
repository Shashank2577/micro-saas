'use client';

import React, { useEffect, useState } from 'react';
import { Table } from '@/components/Table';
import { Modal } from '@/components/Modal';

export default function RenewalsPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    setData([
      { id: '1', name: 'SaaS License', status: 'DUE' },
    ]);
    setLoading(false);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Renewals</h1>
        <button onClick={() => setModalOpen(true)} className="bg-blue-600 text-white px-4 py-2 rounded">
          New Alert
        </button>
      </div>
      {loading ? <p>Loading...</p> : <Table data={data} columns={['id', 'name', 'status']} />}
      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title="New Renewal Alert">
        <p>Form coming soon...</p>
      </Modal>
    </div>
  );
}
