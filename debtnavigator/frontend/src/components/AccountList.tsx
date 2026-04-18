"use client";

import React, { useEffect, useState } from "react";
import { apiFetch } from "@/lib/api";

export default function AccountList() {
  const [accounts, setAccounts] = useState<any[]>([]);

  useEffect(() => {
    apiFetch("/api/v1/debt/debt-accounts").then((data) => {
      setAccounts(data || []);
    });
  }, []);

  return (
    <div>
      {accounts.length === 0 ? (
        <p>No accounts found.</p>
      ) : (
        <ul data-testid="account-list">
          {accounts.map((acc: any) => (
            <li key={acc.id} className="border p-4 mb-2 rounded">
              {acc.name} - {acc.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
