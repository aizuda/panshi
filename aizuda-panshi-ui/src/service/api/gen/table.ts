import { request } from '../../request';

/** 获取代码生成预览内容 */
export function fetchGetGenTablePreviewList(data?: Api.Gen.GenCode) {
  return request<Api.Gen.GenPreview[]>({
    url: '/gen/table/preview',
    method: 'post',
    data
  });
}
