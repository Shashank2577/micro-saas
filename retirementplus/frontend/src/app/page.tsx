"use client";

import React from 'react';
import Link from 'next/link';

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen py-2">
      <main className="flex flex-col items-center justify-center w-full flex-1 px-20 text-center">
        <h1 className="text-6xl font-bold">
          Welcome to <span className="text-blue-600">RetirementPlus</span>
        </h1>

        <p className="mt-3 text-2xl">
          Comprehensive retirement planning and income projection.
        </p>

        <div className="flex flex-wrap items-center justify-around max-w-4xl mt-6 sm:w-full">
          <Link href="/profile" className="p-6 mt-6 text-left border w-96 rounded-xl hover:text-blue-600 focus:text-blue-600">
            <h3 className="text-2xl font-bold">Get Started &rarr;</h3>
            <p className="mt-4 text-xl">
              Create your profile and start planning.
            </p>
          </Link>

          <Link href="/projections" className="p-6 mt-6 text-left border w-96 rounded-xl hover:text-blue-600 focus:text-blue-600">
            <h3 className="text-2xl font-bold">Projections &rarr;</h3>
            <p className="mt-4 text-xl">
              View your personalized retirement projections.
            </p>
          </Link>
        </div>
      </main>
    </div>
  );
}
