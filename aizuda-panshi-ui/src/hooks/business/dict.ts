import type { SelectOption } from 'naive-ui';
import { fetchGetDictSelectList } from '@/service/api';

export function useDict() {
  async function getDictOptions(type: string) {
    const { data, error } = await fetchGetDictSelectList(type);
    if (error) return [];
    return data.map(item => ({ label: item.title, value: item.content }));
  }

  async function transformDictByCode(type: string, code: string) {
    const options = await getDictOptions(type);
    transformDictByOptions(code, options);
  }

  function transformDictByOptions(code: string, options: SelectOption[]) {
    return options.find(item => item.value === code)?.label;
  }

  return {
    getDictOptions,
    transformDictByCode,
    transformDictByOptions
  };
}
