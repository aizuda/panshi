import { $t } from '@/locales';

/**
 * Transform record to option
 *
 * @example
 *   ```ts
 *   const record = {
 *     key1: 'label1',
 *     key2: 'label2'
 *   };
 *   const options = transformRecordToOption(record);
 *   // [
 *   //   { value: 'key1', label: 'label1' },
 *   //   { value: 'key2', label: 'label2' }
 *   // ]
 *   ```;
 *
 * @param record
 */
export function transformRecordToOption<T extends Record<string, string>>(record: T, reverse: boolean = false) {
  const options = Object.entries(record).map(([value, label]) => ({
    value,
    label
  })) as CommonType.Option<keyof T, T[keyof T]>[];
  return reverse ? options.reverse() : options;
}

export function transformRecordToNumberOption<T extends Record<string, string>>(record: T, reverse: boolean = false) {
  const options = Object.entries(record).map(([value, label]) => ({
    value: Number(value),
    label
  })) as CommonType.Option<keyof T, T[keyof T]>[];
  return reverse ? options.reverse() : options;
}

/**
 * Translate options
 *
 * @param options
 */
export function translateOptions(options: CommonType.Option<string, App.I18n.I18nKey>[]) {
  return options.map(option => ({
    ...option,
    label: $t(option.label)
  }));
}

/**
 * Translate options
 *
 * @param options
 */
export function translateNumberOptions(options: CommonType.Option<number>[]) {
  return options.map(option => ({
    ...option,
    label: option.label
  }));
}

/**
 * Toggle html class
 *
 * @param className
 */
export function toggleHtmlClass(className: string) {
  function add() {
    document.documentElement.classList.add(className);
  }

  function remove() {
    document.documentElement.classList.remove(className);
  }

  return {
    add,
    remove
  };
}

/*
 * 驼峰转换下划线
 */
export function humpToLine(str: string, line: string = '-') {
  let temp = str.replace(/[A-Z]/g, match => {
    return `${line}${match.toLowerCase()}`;
  });
  // 如果首字母是大写，执行replace时会多一个_，这里需要去掉
  if (temp.slice(0, 1) === line) {
    temp = temp.slice(1);
  }
  return temp;
}

export function isNotNull(value: any) {
  return value !== undefined && value !== null && value !== '' && value !== 'undefined' && value !== 'null';
}
