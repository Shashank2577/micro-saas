import SystemProvisioningRequestsList from '../../components/SystemProvisioningRequestsList';

export default function SystemProvisioningRequestsPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">SystemProvisioningRequests Management</h1>
      <SystemProvisioningRequestsList />
    </main>
  );
}
