import React from 'react';
import { BudgetItem } from '../types';

export function BudgetTable({ items }: { items: BudgetItem[] }) {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white border border-gray-200">
        <thead>
          <tr>
            <th className="px-4 py-2 border-b">Category</th>
            <th className="px-4 py-2 border-b">Department</th>
            <th className="px-4 py-2 border-b">Allocated</th>
            <th className="px-4 py-2 border-b">Actual</th>
            <th className="px-4 py-2 border-b">Variance</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => {
            const variance = item.actualAmount - item.allocatedAmount;
            const variancePercent = item.allocatedAmount > 0
                ? (variance / item.allocatedAmount) * 100
                : 0;
            return (
              <tr key={item.id} className="hover:bg-gray-50">
                <td className="px-4 py-2 border-b">{item.category}</td>
                <td className="px-4 py-2 border-b">{item.department}</td>
                <td className="px-4 py-2 border-b">${item.allocatedAmount}</td>
                <td className="px-4 py-2 border-b">${item.actualAmount}</td>
                <td className={`px-4 py-2 border-b font-bold ${variance > 0 ? 'text-red-500' : 'text-green-500'}`}>
                  ${Math.abs(variance).toFixed(2)} ({variancePercent.toFixed(2)}%)
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
