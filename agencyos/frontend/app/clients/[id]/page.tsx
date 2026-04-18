"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";

interface Project {
  id: string;
  name: string;
  status: string;
}

export default function ClientDetailsPage() {
  const { id } = useParams();
  const [projects, setProjects] = useState<Project[]>([]);

  useEffect(() => {
    fetch(`/api/projects?clientId=${id}`, { headers: { "X-Tenant-ID": "tenant-1" } })
      .then((res) => {
        if(!res.ok) return [];
        return res.json();
      })
      .then((data) => setProjects(data))
      .catch(console.error);
  }, [id]);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Client Projects</h1>
      <Link href="/clients" className="text-blue-500 mb-4 block">&larr; Back to Clients</Link>
      <div className="space-y-4">
        {projects.length === 0 ? <p>No projects found.</p> : null}
        {projects.map((project) => (
          <Link key={project.id} href={`/projects/${project.id}`}>
            <div className="border p-4 rounded block hover:bg-gray-50">
              <h3 className="font-bold">{project.name}</h3>
              <span className="text-sm bg-gray-200 px-2 py-1 rounded">{project.status}</span>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
