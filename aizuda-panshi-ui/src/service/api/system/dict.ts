import { request } from '../../request';

/** 查询字典列表 */
export function fetchGetDictList(data?: Api.System.DictSearchParams) {
  return request<Api.System.DictList>({
    url: '/sys/dict/page',
    method: 'post',
    data
  });
}

/** 新增字典 */
export function fetchCreateDict(data: Api.System.DictFormParams) {
  return request<boolean>({
    url: '/sys/dict/create',
    method: 'post',
    data
  });
}

/** 修改字典 */
export function fetchUpdateDict(data: Api.System.DictFormParams) {
  return request<boolean>({
    url: '/sys/dict/update',
    method: 'post',
    data
  });
}

/** 批量删除字典 */
export function fetchBatchDeleteDict(data: string[]) {
  return request<boolean>({
    url: '/sys/dict/delete',
    method: 'post',
    data
  });
}

/** 查询字典父级列表 */
export function fetchGetDictParentList() {
  return request<Api.System.DictParentList>({
    url: '/sys/dict/list-parent',
    method: 'get'
  });
}

/** 修改字典状态 */
export function fetchUpdateDictStatus(dictId: string, status: Api.Common.EnableStatus) {
  return request<boolean>({
    url: `/sys/dict/status/${dictId}`,
    method: 'post',
    params: { status }
  });
}

/** 根据字典编码查询表单下拉选择项列表 */
export function fetchGetDictSelectList(code: string) {
  return request<Api.System.DictSelectOption[]>({
    url: '/sys/dict/list-select-options',
    method: 'get',
    params: { code }
  });
}
