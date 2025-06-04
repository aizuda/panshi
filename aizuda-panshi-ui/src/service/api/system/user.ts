import { request } from '../../request';

/** 获取用户分页列表 */
export function fetchGetUserList(data: Api.System.UserSearchParams) {
  return request<Api.System.UserList>({
    url: '/sys/user/page',
    method: 'post',
    data
  });
}

/** 获取满足条件20条记录的用户列表 */
export function fetchGetUserList20(username?: string) {
  return request<Api.System.User[]>({
    url: '/sys/user/list20',
    method: 'post',
    params: { username }
  });
}

/** 获取用户角色信息 */
export function fetchGetUserRole(id: string) {
  return request<Api.System.UserRoles>({
    url: '/sys/user/rel-ids',
    method: 'post',
    params: { id }
  });
}

/** 创建用户 */
export function fetchCreateUser(data: Api.System.UserFormParams) {
  return request<boolean>({
    url: '/sys/user/create',
    method: 'post',
    data
  });
}

/** 修改用户 */
export function fetchUpdateUser(data: Api.System.UserFormParams) {
  return request<boolean>({
    url: '/sys/user/update',
    method: 'post',
    data
  });
}

/** 删除用户 */
export function fetchBatchDeleteUser(data: string[]) {
  return request<boolean>({
    url: '/sys/user/delete',
    method: 'post',
    data
  });
}

/** 重置密码 */
export function fetchUserResetPwd(ids: string[], password: string) {
  return request<boolean>({
    url: '/sys/user/reset-password',
    method: 'post',
    data: { ids, password }
  });
}

/** 分配部门 */
export function fetchUserAssignDept(userIds: string[], departmentIds: string[]) {
  return request<boolean>({
    url: '/sys/user/assign-departments',
    method: 'post',
    data: { userIds, departmentIds }
  });
}

/** 分配角色 */
export function fetchUserAssignRole(userIds: string[], roleIds: string[]) {
  return request<boolean>({
    url: '/sys/user/assign-roles',
    method: 'post',
    data: { userIds, roleIds }
  });
}

/** 修改用户状态 */
export function fetchUpdateUserStatus(userId: string, status: Api.Common.EnableStatus) {
  return request<boolean>({
    url: `/sys/user/status/${userId}`,
    method: 'post',
    params: { status }
  });
}
