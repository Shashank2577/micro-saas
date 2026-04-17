import { z } from 'zod';

export const SpecSchema = z.object({
  title: z.string(),
  description: z.string(),
  features: z.array(z.string()),
  endpoints: z.array(z.object({
    method: z.string(),
    path: z.string(),
    description: z.string()
  })).optional()
});

export type Spec = z.infer<typeof SpecSchema>;
