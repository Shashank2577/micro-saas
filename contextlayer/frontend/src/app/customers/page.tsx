import React from 'react';
import Link from 'next/link';

export default function CustomersPage() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Customers</h1>
      <ul className="list-disc pl-5">
        <li>
          <Link href="/customers/cust_123" className="text-blue-500 hover:underline">
            View Customer cust_123 Context
          </Link>
        </li>
      </ul>
    </div>
  );
}
