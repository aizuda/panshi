import { request } from '../../request';

/** 查询参数列表 */
export function fetchGetConfigList(data?: Api.System.ConfigSearchParams) {
  return request<Api.System.ConfigList>({
    url: '/sys/configure/page',
    method: 'post',
    data
  });
}

/** 新增参数 */
export function fetchCreateConfig(data: Api.System.ConfigFormParams) {
  return request<boolean>({
    url: '/sys/configure/create',
    method: 'post',
    data
  });
}

/** 修改参数 */
export function fetchUpdateConfig(data: Api.System.ConfigFormParams) {
  return request<boolean>({
    url: '/sys/configure/update',
    method: 'post',
    data
  });
}

/** 批量删除参数 */
export function fetchBatchDeleteConfig(data: string[]) {
  return request<boolean>({
    url: '/sys/configure/delete',
    method: 'post',
    data
  });
}
