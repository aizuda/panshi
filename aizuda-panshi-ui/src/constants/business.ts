import { transformRecordToNumberOption, transformRecordToOption } from '@/utils/common';

export const enableStatusRecord: Record<Api.Common.EnableStatus, string> = {
  1: '启用',
  0: '禁用'
};

export const enableStatusOptions = transformRecordToNumberOption(enableStatusRecord, true);

export const menuTypeRecord: Record<Api.System.MenuType, string> = {
  [-1]: '目录',
  0: '菜单',
  1: 'Iframe',
  2: '外链',
  3: '按钮'
};

export const menuTypeOptions = transformRecordToNumberOption(menuTypeRecord);

/** menu icon type */
export const menuIconTypeRecord: Record<Api.System.IconType, string> = {
  '1': 'iconify',
  '2': '本地图标'
};

export const menuIconTypeOptions = transformRecordToOption(menuIconTypeRecord);

export const regionLevelRecord: Record<Api.System.RegionLevel, string> = {
  0: '省份直辖市',
  1: '地区',
  2: '区县'
};

export const regionLevelOptions = transformRecordToNumberOption(regionLevelRecord);

export const messageTypeRecord: Record<Api.System.MessageType, string> = {
  0: '通知',
  1: '消息',
  2: '待办'
};

export const messageTypeOptions = transformRecordToNumberOption(messageTypeRecord);
