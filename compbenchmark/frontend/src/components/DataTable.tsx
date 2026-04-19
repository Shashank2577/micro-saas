import React from 'react';

export default function DataTable({ data, columns }: { data: any[], columns: string[] }) {
    if (!data || data.length === 0) return <div>No data available</div>;

    return (
        <div className="overflow-x-auto">
            <table className="min-w-full bg-white border border-gray-200">
                <thead>
                    <tr>
                        {columns.map(col => <th key={col} className="px-4 py-2 border-b">{col}</th>)}
                    </tr>
                </thead>
                <tbody>
                    {data.map((row, i) => (
                        <tr key={i} className="hover:bg-gray-100">
                            {columns.map(col => <td key={col} className="px-4 py-2 border-b">{row[col]}</td>)}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
