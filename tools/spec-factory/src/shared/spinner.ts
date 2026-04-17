import ora from 'ora';

export async function getSpinner(text: string) {
  return ora({ text });
}
