import React from 'react';

interface WorkflowStep {
  id: string;
  name: string;
  status: 'completed' | 'current' | 'upcoming';
  date?: string;
}

interface WorkflowStatusProps {
  steps: WorkflowStep[];
}

export default function WorkflowStatus({ steps }: WorkflowStatusProps) {
  return (
    <nav aria-label="Progress">
      <ol role="list" className="overflow-hidden">
        {steps.map((step, stepIdx) => (
          <li key={step.id} className={`relative ${stepIdx !== steps.length - 1 ? 'pb-10' : ''}`}>
            {stepIdx !== steps.length - 1 ? (
              <div className="absolute top-4 left-4 -ml-px h-full w-0.5 bg-gray-200" aria-hidden="true" />
            ) : null}
            <div className="relative flex items-start group">
              <span className="h-9 flex items-center">
                <span
                  className={`relative z-10 w-8 h-8 flex items-center justify-center rounded-full ${
                    step.status === 'completed'
                      ? 'bg-indigo-600'
                      : step.status === 'current'
                      ? 'bg-white border-2 border-indigo-600'
                      : 'bg-white border-2 border-gray-300'
                  }`}
                >
                  {step.status === 'completed' ? (
                    <svg className="w-5 h-5 text-white" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                  ) : step.status === 'current' ? (
                    <span className="h-2.5 w-2.5 bg-indigo-600 rounded-full" />
                  ) : null}
                </span>
              </span>
              <span className="ml-4 min-w-0 flex flex-col">
                <span className={`text-sm font-medium tracking-wide ${step.status === 'completed' ? 'text-indigo-600' : step.status === 'current' ? 'text-gray-900' : 'text-gray-500'}`}>
                  {step.name}
                </span>
                {step.date && <span className="text-sm text-gray-500">{step.date}</span>}
              </span>
            </div>
          </li>
        ))}
      </ol>
    </nav>
  );
}
