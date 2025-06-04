import { request } from '../../request';

/** 查询部门树列表 */
export function fetchGetDeptTree(data?: Api.System.DepartmentSearchParams) {
  return request<Api.System.DepartmentList>({
    url: '/sys/department/list-tree',
    method: 'post',
    data
  });
}

/** 新增部门 */
export function fetchCreateDept(data: Api.System.DepartmentFormParams) {
  return request<boolean>({
    url: '/sys/department/create',
    method: 'post',
    data
  });
}

/** 修改部门 */
export function fetchUpdateDept(data: Api.System.DepartmentFormParams) {
  return request<boolean>({
    url: '/sys/department/update',
    method: 'post',
    data
  });
}

/** 批量删除部门 */
export function fetchBatchDeleteDept(data: string[]) {
  return request<boolean>({
    url: '/sys/department/delete',
    method: 'post',
    data
  });
}

/** 修改部门状态 */
export function fetchUpdateDeptStatus(deptId: string, status: Api.Common.EnableStatus) {
  return request<boolean>({
    url: `/sys/department/status/${deptId}`,
    method: 'post',
    params: { status }
  });
}
