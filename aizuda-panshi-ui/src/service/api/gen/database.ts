import { request } from '../../request';

/** 查询数据源分页列表 */
export function fetchGetGenDatabaseList(data?: Api.Gen.DatabaseSearchParams) {
  return request<Api.Gen.DatabaseList>({
    url: '/gen/database/page',
    method: 'post',
    data
  });
}

/** 创建数据源 */
export function fetchCreateGenDatabase(data?: Api.Gen.DatabaseFormParams) {
  return request<boolean>({
    url: '/gen/database/create',
    method: 'post',
    data
  });
}

/** 修改数据源 */
export function fetchUpdateGenDatabase(data?: Api.Gen.DatabaseFormParams) {
  return request<boolean>({
    url: '/gen/database/update',
    method: 'post',
    data
  });
}

/** 批量删除数据源 */
export function fetchBatchDeleteGenDatabase(data: string[]) {
  return request<boolean>({
    url: '/gen/database/delete',
    method: 'post',
    data
  });
}

/** 查询数据源下拉选项列表 */
export function fetchGetGenDatabaseSelectOption() {
  return request<{ id: string; title: string }[]>({
    url: '/gen/database/list-select-options',
    method: 'get'
  });
}
