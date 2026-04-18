"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';
import Link from 'next/link';

export default function SkusPage() {
  const [skus, setSkus] = useState<any[]>([]);

  useEffect(() => {
    async function load() {
      try {
        const res = await api.get('/api/skus');
        setSkus(res.data);
      } catch (err) {
        console.error("Failed to load skus", err);
      }
    }
    load();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">SKU Inventory</h1>
      <table className="min-w-full border">
        <thead>
          <tr className="bg-gray-100">
            <th className="p-2 border">Code</th>
            <th className="p-2 border">Name</th>
            <th className="p-2 border">Cost Price</th>
            <th className="p-2 border">Current Price</th>
            <th className="p-2 border">Stock</th>
            <th className="p-2 border">Actions</th>
          </tr>
        </thead>
        <tbody>
          {skus.map(sku => (
            <tr key={sku.id} className="border">
              <td className="p-2 border">{sku.skuCode}</td>
              <td className="p-2 border">{sku.name}</td>
              <td className="p-2 border">${sku.costPrice}</td>
              <td className="p-2 border">${sku.currentPrice}</td>
              <td className="p-2 border">{sku.stockQuantity}</td>
              <td className="p-2 border">
                <Link href={`/skus/${sku.id}`} className="text-blue-500 hover:underline">View Details</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
