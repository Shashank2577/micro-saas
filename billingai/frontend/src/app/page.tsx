import { getInvoices, getSubscriptions } from '../lib/api';

export default async function DashboardPage() {
  let invoices: any[] = [];
  let subscriptions: any[] = [];

  try {
    invoices = await getInvoices();
    subscriptions = await getSubscriptions();
  } catch (error) {
    console.error("Failed to load data", error);
  }

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Billing AI Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <h2 className="text-xl font-semibold mb-4">Invoices</h2>
          <div className="text-3xl font-bold text-blue-600">{invoices.length}</div>
          <p className="text-sm text-gray-500 mt-1">Total Invoices</p>
        </div>
        
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <h2 className="text-xl font-semibold mb-4">Subscriptions</h2>
          <div className="text-3xl font-bold text-green-600">{subscriptions.length}</div>
          <p className="text-sm text-gray-500 mt-1">Active Subscriptions</p>
        </div>
      </div>
    </div>
  );
}
