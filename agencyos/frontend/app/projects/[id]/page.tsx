"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";

interface Task {
  id: string;
  title: string;
  status: string;
}

export default function ProjectDetailsPage() {
  const { id } = useParams();
  const [tasks, setTasks] = useState<Task[]>([]);

  useEffect(() => {
    fetch(`/api/tasks?projectId=${id}`, { headers: { "X-Tenant-ID": "tenant-1" } })
      .then((res) => {
         if(!res.ok) return [];
         return res.json();
      })
      .then((data) => setTasks(data))
      .catch(console.error);
  }, [id]);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Project Tasks</h1>
      <Link href="/clients" className="text-blue-500 mb-4 block">&larr; Back to Clients</Link>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {['TODO', 'IN_PROGRESS', 'DONE'].map((status) => (
          <div key={status} className="bg-gray-100 p-4 rounded">
            <h3 className="font-bold mb-4">{status}</h3>
            <div className="space-y-2">
              {tasks.filter(t => t.status === status).map(task => (
                <div key={task.id} className="bg-white p-3 rounded shadow-sm">
                  {task.title}
                </div>
              ))}
              {tasks.filter(t => t.status === status).length === 0 && (
                <p className="text-sm text-gray-500">No tasks.</p>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
