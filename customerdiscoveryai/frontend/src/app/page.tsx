'use client'

import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import Link from 'next/link'
import { Project } from '@/types'

export default function Home() {
  const [projects, setProjects] = useState<Project[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadProjects() {
      try {
        const response = await api.get('/api/projects')
        setProjects(response.data)
      } catch (error) {
        console.error('Failed to load projects', error)
      } finally {
        setLoading(false)
      }
    }
    loadProjects()
  }, [])

  if (loading) {
    return <div className="p-8">Loading projects...</div>
  }

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-8">Customer Discovery AI</h1>
      <p className="mb-8 text-gray-600">Research projects workspace.</p>

      <div className="grid gap-4">
        {projects.map((project) => (
          <Link href={`/projects/${project.id}`} key={project.id}>
            <div className="border p-6 rounded-lg hover:shadow-md transition-shadow cursor-pointer">
              <h2 className="text-xl font-semibold mb-2">{project.name}</h2>
              <p className="text-gray-600 mb-4">{project.description}</p>
              <div className="text-sm text-gray-500">Audience: {project.targetAudience}</div>
            </div>
          </Link>
        ))}
      </div>
      
      {projects.length === 0 && (
        <div className="text-gray-500 italic border p-8 rounded text-center">
          No research projects found. Create one to get started.
        </div>
      )}
    </div>
  )
}
