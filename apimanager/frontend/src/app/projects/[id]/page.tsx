'use client';

import React, { useState, useEffect } from 'react';
import { api } from '../../../lib/api';
import SwaggerViewer from '../../../components/SwaggerViewer';
import ApiKeyManager from '../../../components/ApiKeyManager';

export default function ProjectDetail({ params }: { params: { id: string } }) {
  const [project, setProject] = useState<any>(null);
  const [versions, setVersions] = useState<any[]>([]);
  const [activeVersion, setActiveVersion] = useState<any>(null);
  const [schema, setSchema] = useState<any>(null);
  const [activeTab, setActiveTab] = useState('docs');

  useEffect(() => {
    loadProject();
    loadVersions();
  }, [params.id]);

  useEffect(() => {
    if (activeVersion) {
      loadSchema(activeVersion.id);
    }
  }, [activeVersion]);

  const loadProject = async () => {
    try {
      const res = await api.get(`/api/v1/projects/${params.id}`);
      setProject(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  const loadVersions = async () => {
    try {
      const res = await api.get(`/api/v1/projects/${params.id}/versions`);
      setVersions(res.data);
      if (res.data.length > 0 && !activeVersion) {
        setActiveVersion(res.data[res.data.length - 1]);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const loadSchema = async (versionId: string) => {
    try {
      const res = await api.get(`/api/v1/projects/${params.id}/versions/${versionId}/schema`);
      setSchema(res.data);
    } catch (e) {
      console.error(e);
      setSchema(null);
    }
  };

  const createVersion = async () => {
    const sampleSchema = {
      openapi: "3.0.0",
      info: { title: "Sample API", version: "1.0.0" },
      paths: {
        "/users": {
          get: {
            summary: "Get users",
            responses: { "200": { description: "Successful response" } }
          }
        }
      }
    };
    try {
      await api.post(`/api/v1/projects/${params.id}/versions`, {
        versionString: `v${versions.length + 1}.0`,
        openapiSchema: JSON.stringify(sampleSchema)
      });
      loadVersions();
    } catch (e) {
      console.error(e);
    }
  };

  if (!project) return <div className="p-8">Loading...</div>;

  return (
    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">{project.name}</h1>
        <p className="mt-1 text-gray-500">{project.description}</p>
      </div>

      <div className="mb-6 border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          {['docs', 'keys', 'analytics'].map(tab => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`${
                activeTab === tab
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm capitalize`}
            >
              {tab}
            </button>
          ))}
        </nav>
      </div>

      {activeTab === 'docs' && (
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          <div className="lg:col-span-1">
            <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200 mb-4">
              <h3 className="font-medium mb-3">Versions</h3>
              <div className="space-y-2">
                {versions.map(v => (
                  <button
                    key={v.id}
                    onClick={() => setActiveVersion(v)}
                    className={`w-full text-left px-3 py-2 rounded-md text-sm ${
                      activeVersion?.id === v.id ? 'bg-blue-50 text-blue-700 font-medium' : 'text-gray-700 hover:bg-gray-50'
                    }`}
                  >
                    {v.versionString}
                    <span className="ml-2 text-xs text-gray-400">({v.status})</span>
                  </button>
                ))}
              </div>
              <button 
                onClick={createVersion}
                className="mt-4 w-full bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-50 text-sm font-medium"
              >
                + New Version
              </button>
            </div>
          </div>
          <div className="lg:col-span-3">
            {schema ? (
              <SwaggerViewer spec={schema} />
            ) : (
              <div className="bg-white p-8 text-center rounded-lg shadow-sm border border-gray-200">
                <p className="text-gray-500">No schema available for this version.</p>
              </div>
            )}
          </div>
        </div>
      )}

      {activeTab === 'keys' && (
        <ApiKeyManager projectId={project.id} />
      )}

      {activeTab === 'analytics' && (
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 text-center">
          <p className="text-gray-500">Analytics feature coming soon.</p>
        </div>
      )}
    </div>
  );
}
