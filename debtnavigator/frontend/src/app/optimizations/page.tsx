import OptimizationRunList from "@/components/OptimizationRunList";

export default function OptimizationsPage() {
  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Optimization Runs</h1>
      <OptimizationRunList />
    </div>
  );
}
