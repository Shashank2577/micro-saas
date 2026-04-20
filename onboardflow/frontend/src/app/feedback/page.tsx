import OnboardingFeedbackList from '../../components/OnboardingFeedbackList';

export default function OnboardingFeedbackPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">OnboardingFeedback Management</h1>
      <OnboardingFeedbackList />
    </main>
  );
}
