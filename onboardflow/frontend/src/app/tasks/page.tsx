import OnboardingTasksList from '../../components/OnboardingTasksList';

export default function OnboardingTasksPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">OnboardingTasks Management</h1>
      <OnboardingTasksList />
    </main>
  );
}
