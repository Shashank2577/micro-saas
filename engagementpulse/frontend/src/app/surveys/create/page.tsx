"use client";
import { useState } from 'react';
import { api } from '@/lib/api';
import { useRouter } from 'next/navigation';
import { Question } from '@/types';

export default function CreateSurveyPage() {
  const router = useRouter();
  const [title, setTitle] = useState('');
  const [questions, setQuestions] = useState<Question[]>([]);

  const addQuestion = () => {
    setQuestions([...questions, { text: '', type: 'RATING', orderIndex: questions.length + 1 }]);
  };

  const updateQuestion = (index: number, text: string) => {
    const newQ = [...questions];
    newQ[index].text = text;
    setQuestions(newQ);
  };

  const submit = async () => {
    await api.post('/surveys', { title, questions });
    router.push('/surveys');
  };

  return (
    <div className="p-8 space-y-6 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold">Create Survey</h1>
      <input 
        className="w-full border p-2 rounded" 
        placeholder="Survey Title" 
        value={title} 
        onChange={e => setTitle(e.target.value)} 
      />
      
      <div className="space-y-4">
        {questions.map((q, i) => (
          <div key={i} className="flex gap-2">
            <input 
              className="flex-1 border p-2 rounded" 
              placeholder="Question text" 
              value={q.text} 
              onChange={e => updateQuestion(i, e.target.value)} 
            />
          </div>
        ))}
      </div>
      
      <div className="flex justify-between">
        <button onClick={addQuestion} className="bg-gray-200 px-4 py-2 rounded">Add Question</button>
        <button onClick={submit} className="bg-blue-600 text-white px-4 py-2 rounded">Save</button>
      </div>
    </div>
  );
}
