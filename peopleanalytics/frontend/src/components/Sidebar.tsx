import React from 'react';
import Link from 'next/link';
import { LayoutDashboard, Users, MessageSquare, AlertTriangle, FileText } from 'lucide-react';

const Sidebar = () => {
  const navItems = [
    { name: 'Dashboard', href: '/', icon: LayoutDashboard },
    { name: 'Employees', href: '/employees', icon: Users },
    { name: 'Surveys', href: '/surveys', icon: MessageSquare },
    { name: 'Retention Risk', href: '/retention', icon: AlertTriangle },
    { name: 'Reports', href: '/reports', icon: FileText },
  ];

  return (
    <div className="w-64 h-screen bg-gray-900 text-white flex flex-col">
      <div className="p-6 text-2xl font-bold border-b border-gray-800">
        PeopleAnalytics
      </div>
      <nav className="flex-1 p-4">
        <ul className="space-y-2">
          {navItems.map((item) => (
            <li key={item.name}>
              <Link href={item.href} className="flex items-center space-x-3 p-3 rounded-lg hover:bg-gray-800 transition">
                <item.icon size={20} />
                <span>{item.name}</span>
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;
