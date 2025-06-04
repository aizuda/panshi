import { request } from '../../request';

/** 查询岗位列表 */
export function fetchGetPostList(data?: Api.System.PostSearchParams) {
  return request<Api.System.PostList>({
    url: '/sys/post/page',
    method: 'post',
    data
  });
}

/** 新增岗位 */
export function fetchCreatePost(data: Api.System.PostFormParams) {
  return request<boolean>({
    url: '/sys/post/create',
    method: 'post',
    data
  });
}

/** 修改岗位 */
export function fetchUpdatePost(data: Api.System.PostFormParams) {
  return request<boolean>({
    url: '/sys/post/update',
    method: 'post',
    data
  });
}

/** 批量删除岗位 */
export function fetchBatchDeletePost(data: string[]) {
  return request<boolean>({
    url: '/sys/post/delete',
    method: 'post',
    data
  });
}

/** 修改岗位状态 */
export function fetchUpdatePostStatus(postId: string, status: Api.Common.EnableStatus) {
  return request<boolean>({
    url: `/sys/post/status/${postId}`,
    method: 'post',
    params: { status }
  });
}
