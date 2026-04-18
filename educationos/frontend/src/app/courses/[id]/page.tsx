"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import { useParams } from 'next/navigation';

interface Course {
  id: string;
  title: string;
  description: string;
}

interface Module {
  id: string;
  title: string;
  content: string;
  difficultyLevel: string;
  orderIndex: number;
}

export default function CourseDetailsPage() {
  const params = useParams();
  const id = params?.id as string;
  
  const [course, setCourse] = useState<Course | null>(null);
  const [modules, setModules] = useState<Module[]>([]);

  useEffect(() => {
    if (!id) return;
    async function loadData() {
      try {
        const [courseRes, modulesRes] = await Promise.all([
          api.get(`/api/courses/${id}`),
          api.get(`/api/courses/${id}/modules`)
        ]);
        setCourse(courseRes.data);
        setModules(modulesRes.data.sort((a: Module, b: Module) => a.orderIndex - b.orderIndex));
      } catch (error) {
        console.error('Error fetching course details', error);
      }
    }
    loadData();
  }, [id]);

  if (!course) return <div className="p-8 text-center text-gray-500">Loading...</div>;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">{course.title}</h1>
        <p className="text-lg text-gray-700">{course.description}</p>
      </div>

      <div className="space-y-4">
        <h2 className="text-2xl font-semibold text-gray-800 border-b pb-2">Modules</h2>
        {modules.map((module) => (
          <div key={module.id} className="bg-white p-5 rounded border border-gray-200 shadow-sm">
            <div className="flex justify-between items-center mb-2">
              <h3 className="text-xl font-medium text-indigo-700">
                {module.orderIndex}. {module.title}
              </h3>
              <span className="text-sm px-3 py-1 bg-gray-100 rounded-full text-gray-600">
                Level: {module.difficultyLevel}
              </span>
            </div>
            <p className="text-gray-600 mt-2">{module.content}</p>
            <div className="mt-4">
              <button 
                className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition"
                onClick={() => alert('Start Module functionality coming soon')}
              >
                Start Module
              </button>
            </div>
          </div>
        ))}
        {modules.length === 0 && (
          <p className="text-gray-500 italic">No modules available for this course.</p>
        )}
      </div>
    </div>
  );
}
