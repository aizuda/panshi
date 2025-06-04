import { request } from '../../request';

/** 查询代码生成模板分页列表 */
export function fetchGetGenTemplateList(data?: Api.Gen.TemplateSearchParams) {
  return request<Api.Gen.TemplateList>({
    url: '/gen/template/page',
    method: 'post',
    data
  });
}

/** 创建代码生成模板 */
export function fetchCreateGenTemplate(data?: Api.Gen.TemplateFormParams) {
  return request<boolean>({
    url: '/gen/template/create',
    method: 'post',
    data
  });
}

/** 修改代码生成模板 */
export function fetchUpdateGenTemplate(data?: Api.Gen.TemplateFormParams) {
  return request<boolean>({
    url: '/gen/template/update',
    method: 'post',
    data
  });
}

/** 批量删除代码生成模板 */
export function fetchBatchDeleteGenTemplate(data: string[]) {
  return request<boolean>({
    url: '/gen/template/delete',
    method: 'post',
    data
  });
}

/** 查询所有代码生成模板列表 */
export function fetchGetAllGenTemplateList(data?: Api.Gen.TemplateSearchParams) {
  return request<Api.Gen.Template[]>({
    url: '/gen/template/list-all',
    method: 'post',
    data
  });
}
