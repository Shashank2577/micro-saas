import EmployeesList from '../../components/EmployeesList';

export default function EmployeesPage() {
  return (
    <main className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Employees Management</h1>
      <EmployeesList />
    </main>
  );
}
