import { request } from '../../request';

/** 查询应用列表 */
export function fetchGetAppList(data?: Api.System.AppSearchParams) {
  return request<Api.System.AppList>({
    url: '/sys/app/page',
    method: 'post',
    data
  });
}

/** 新增应用 */
export function fetchCreateApp(data: Api.System.AppFormParams) {
  return request<boolean>({
    url: '/sys/app/create',
    method: 'post',
    data
  });
}

/** 修改应用 */
export function fetchUpdateApp(data: Api.System.AppFormParams) {
  return request<boolean>({
    url: '/sys/app/update',
    method: 'post',
    data
  });
}

/** 批量删除应用 */
export function fetchBatchDeleteApp(data: string[]) {
  return request<boolean>({
    url: '/sys/app/delete',
    method: 'post',
    data
  });
}

/** 修改应用状态 */
export function fetchUpdateAppStatus(appId: string, status: Api.Common.EnableStatus) {
  return request<boolean>({
    url: `/sys/app/status/${appId}`,
    method: 'post',
    params: { status }
  });
}
