"use client";

import React from 'react';
import api from '@/lib/api';

export default function ReportsPage() {
  const downloadPdf = async () => {
    const res = await api.get('/reports/export/pdf', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'employee_report.pdf');
    document.body.appendChild(link);
    link.click();
  };

  const downloadCsv = async () => {
    const res = await api.get('/reports/export/csv', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'employee_report.csv');
    document.body.appendChild(link);
    link.click();
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Reports & Exports</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 flex flex-col items-center">
          <div className="w-16 h-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mb-4">
            <span className="text-2xl font-bold">PDF</span>
          </div>
          <h2 className="text-xl font-bold mb-2">Employee Summary</h2>
          <p className="text-gray-500 text-center mb-6">Complete list of employees with departments and roles in a printable format.</p>
          <button onClick={downloadPdf} className="w-full bg-gray-900 text-white py-3 rounded-lg font-semibold hover:bg-gray-800 transition">
            Download PDF
          </button>
        </div>
        <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 flex flex-col items-center">
          <div className="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-4">
            <span className="text-2xl font-bold">CSV</span>
          </div>
          <h2 className="text-xl font-bold mb-2">Raw Data Export</h2>
          <p className="text-gray-500 text-center mb-6">Export raw employee and performance data for external analysis in Excel.</p>
          <button onClick={downloadCsv} className="w-full border-2 border-gray-900 text-gray-900 py-3 rounded-lg font-semibold hover:bg-gray-50 transition">
            Download CSV
          </button>
        </div>
      </div>
    </div>
  );
}
