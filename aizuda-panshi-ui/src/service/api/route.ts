import { request } from '../request';

/** get routes and permissions */
export function fetchGetRoutes() {
  return request<Api.Route.MenuRoute>({
    url: '/sys/resource/list-menu-permissions',
    method: 'get'
  });
}
