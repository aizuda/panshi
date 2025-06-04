import { transformRecordToNumberOption } from '@/utils/common';

export const testFlowStatusRecord: Record<Api.Test.TestFlowStatus, string> = {
  0: '待审批',
  1: '审批中',
  2: '已通过',
  3: '已拒绝'
};

export const testFlowStatusOptions = transformRecordToNumberOption(testFlowStatusRecord);
