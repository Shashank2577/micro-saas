"use client";

import Link from 'next/link';

export default function Navigation() {
  return (
    <nav className="bg-gray-800 text-white p-4">
      <div className="container mx-auto flex gap-4">
        <Link href="/" className="font-bold">CustomerSuccessOS</Link>
        <Link href="/accounts" className="hover:text-gray-300">Accounts</Link>
      </div>
    </nav>
  );
}
