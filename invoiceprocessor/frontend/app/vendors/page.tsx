'use client';

import React, { useState, useEffect } from 'react';

export default function VendorsPage() {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/vendors', {
      headers: {
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
      }
    })
      .then(res => res.json())
      .then(data => {
        setVendors(data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-semibold text-gray-900">Vendors</h1>
        <button className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700">
          Add Vendor
        </button>
      </div>

      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <ul className="divide-y divide-gray-200">
          {loading ? (
            <li className="px-4 py-4 sm:px-6">Loading...</li>
          ) : Array.isArray(vendors) && vendors.length === 0 ? (
            <li className="px-4 py-4 sm:px-6">No vendors found.</li>
          ) : Array.isArray(vendors) ? (
            vendors.map((vendor: any) => (
              <li key={vendor.id}>
                <div className="px-4 py-4 sm:px-6 hover:bg-gray-50 cursor-pointer">
                  <div className="flex items-center justify-between">
                    <p className="text-sm font-medium text-indigo-600 truncate">
                      {vendor.name}
                    </p>
                  </div>
                  <div className="mt-2 sm:flex sm:justify-between">
                    <div className="sm:flex text-sm text-gray-500">
                      <p>{vendor.email}</p>
                      <p className="ml-4">{vendor.phoneNumber}</p>
                    </div>
                  </div>
                </div>
              </li>
            ))
          ) : (
             <li className="px-4 py-4 sm:px-6">Error loading vendors.</li>
          )}
        </ul>
      </div>
    </div>
  );
}
