import OnboardingWorkflowsList from '../../components/OnboardingWorkflowsList';

export default function OnboardingWorkflowsPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">OnboardingWorkflows Management</h1>
      <OnboardingWorkflowsList />
    </main>
  );
}
