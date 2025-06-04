import { request } from '../../request';

/** 获取路由列表 */
export function fetchGetMenuList() {
  return request<Api.System.MenuTreeList>({
    url: '/sys/resource/list-menu-permissions',
    method: 'get'
  });
}

/** 获取路由列表 */
export function fetchGetMenuTreeList() {
  return request<Api.System.MenuTreeList>({
    url: '/sys/resource/list-tree',
    method: 'get',
    params: { menu: true }
  });
}

/** 获取接口权限列表 */
export function fetchGetMenuApiList(id: string) {
  return request<Api.System.ApiPermission[]>({
    url: '/sys/resource/list-api',
    method: 'get',
    params: { id }
  });
}

/** 新增菜单 */
export function fetchCreateMenu(data: Api.System.MenuFormParams) {
  return request<boolean>({
    url: '/sys/resource/create',
    method: 'post',
    data
  });
}

/** 修改菜单 */
export function fetchUpdateMenu(data: Api.System.MenuFormParams) {
  return request<boolean>({
    url: '/sys/resource/update',
    method: 'post',
    data
  });
}

/** 删除菜单 */
export function fetchDeleteMenu(data: string[]) {
  return request<boolean>({
    url: '/sys/resource/delete',
    method: 'post',
    data
  });
}
