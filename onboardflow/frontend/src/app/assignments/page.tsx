import TaskAssignmentsList from '../../components/TaskAssignmentsList';

export default function TaskAssignmentsPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">TaskAssignments Management</h1>
      <TaskAssignmentsList />
    </main>
  );
}
