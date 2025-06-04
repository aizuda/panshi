import { transformRecordToOption } from '@/utils/common';

export const databaseTypeRecord: Record<Api.Gen.DatabaseType, string> = {
  mysql: 'MySQL',
  oracle: 'Oracle',
  pgsql: 'PostgreSQL'
};

export const databaseTypeOptions = transformRecordToOption(databaseTypeRecord);
