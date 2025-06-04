import { request } from '../../request';

/** 查询行政区域树列表 */
export function fetchGetRegionTree(data?: Api.System.RegionSearchParams) {
  return request<Api.System.RegionList>({
    url: '/sys/region/list-tree',
    method: 'post',
    data
  });
}

/** 新增行政区域 */
export function fetchCreateRegion(data: Api.System.RegionFormParams) {
  return request<boolean>({
    url: '/sys/region/create',
    method: 'post',
    data
  });
}

/** 修改行政区域 */
export function fetchUpdateRegion(data: Api.System.RegionFormParams) {
  return request<boolean>({
    url: '/sys/region/update',
    method: 'post',
    data
  });
}

/** 批量删除行政区域 */
export function fetchBatchDeleteRegion(data: string[]) {
  return request<boolean>({
    url: '/sys/region/delete',
    method: 'post',
    data
  });
}
