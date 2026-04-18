'use client';

import React, { useEffect } from 'react';
import SwaggerUI from 'swagger-ui-react';
import 'swagger-ui-react/swagger-ui.css';

interface SwaggerViewerProps {
  url?: string;
  spec?: object;
}

export default function SwaggerViewer({ url, spec }: SwaggerViewerProps) {
  return (
    <div className="swagger-container bg-white p-4 rounded-lg shadow-sm border border-gray-200">
      <SwaggerUI url={url} spec={spec} />
    </div>
  );
}
