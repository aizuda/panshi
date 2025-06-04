import { request } from '../../request';

/** 新增接口权限 */
export function fetchCreateResourceApi(data: Api.System.ApiPermission) {
  return request<boolean>({
    url: '/sys/resource-api/create',
    method: 'post',
    data
  });
}

/** 修改接口权限 */
export function fetchUpdateResourceApi(data: Api.System.ApiPermission) {
  return request<boolean>({
    url: '/sys/resource-api/update',
    method: 'post',
    data
  });
}

/** 删除接口权限 */
export function fetchDeleteResourceApi(data: string[]) {
  return request<boolean>({
    url: '/sys/resource-api/delete',
    method: 'post',
    data
  });
}
