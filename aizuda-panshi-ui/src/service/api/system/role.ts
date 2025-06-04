import { request } from '../../request';

export function fetchGetRoleList(data: Api.System.RoleSearchParams) {
  return request<Api.System.RoleList>({
    url: '/sys/role/page',
    method: 'post',
    data
  });
}

export function fetchGetAllRoles() {
  return request<Api.System.Role[]>({
    url: '/sys/role/list-all',
    method: 'get'
  });
}

export function fetchGetRoleResourceIds(id: string) {
  return request<string[]>({
    url: '/sys/role/resource-ids',
    method: 'get',
    params: { id }
  });
}

/** 创建角色 */
export function fetchCreateRole(data: Api.System.RoleFormParams) {
  return request<boolean>({
    url: '/sys/role/create',
    method: 'post',
    data
  });
}

/** 修改角色 */
export function fetchUpdateRole(data: Api.System.RoleFormParams) {
  return request<boolean>({
    url: '/sys/role/update',
    method: 'post',
    data
  });
}

/** 删除角色 */
export function fetchBatchDeleteRole(data: string[]) {
  return request<boolean>({
    url: '/sys/role/delete',
    method: 'post',
    data
  });
}

/** 分配资源 */
export function fetchRoleSetResource(roleId: string, resourceIds: string[]) {
  return request<boolean>({
    url: '/sys/role/resource-ids',
    method: 'post',
    params: { roleId, resourceIds }
  });
}

/** 修改角色状态 */
export function fetchUpdateRoleStatus(roleId: string, status: Api.Common.EnableStatus) {
  return request<boolean>({
    url: `/sys/role/status/${roleId}`,
    method: 'post',
    params: { status }
  });
}

/** 根据 id 修改信息设置权限 */
export function fetchRoleUpdateSetResource(data: Api.System.RoleFormParams) {
  return request<boolean>({
    url: '/sys/role/update-resource-set',
    method: 'post',
    data
  });
}
