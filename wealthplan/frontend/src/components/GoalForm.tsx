import { useForm } from "react-hook-form";
import axios from "axios";
import { useState } from "react";

export default function GoalForm({ onGoalCreated }: { onGoalCreated: () => void }) {
  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [loading, setLoading] = useState(false);

  const onSubmit = async (data: any) => {
    setLoading(true);
    try {
      await axios.post("http://localhost:8202/api/goals", data, {
        headers: { "X-Tenant-ID": "tenant-1" }
      });
      reset();
      onGoalCreated();
    } catch (e) {
      console.error(e);
      alert("Failed to create goal");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 bg-white p-6 rounded-lg shadow border border-gray-200">
      <h3 className="font-semibold text-lg text-gray-800">Add New Goal</h3>
      <div>
        <label className="block text-sm font-medium text-gray-700">Name</label>
        <input {...register("name", { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" />
        {errors.name && <span className="text-red-500 text-xs">Required</span>}
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Type</label>
        <select {...register("type", { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">
          <option value="RETIREMENT">Retirement</option>
          <option value="HOME_PURCHASE">Home Purchase</option>
          <option value="EDUCATION">Education</option>
          <option value="CUSTOM">Custom</option>
        </select>
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Target Amount</label>
          <input type="number" {...register("targetAmount", { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Current Amount</label>
          <input type="number" {...register("currentAmount", { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Target Date</label>
        <input type="date" {...register("targetDate", { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" />
      </div>
      <button disabled={loading} type="submit" className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
        {loading ? "Saving..." : "Create Goal"}
      </button>
    </form>
  );
}
