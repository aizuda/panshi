import { request } from '../../request';

export function fetchGetCaptcha(ticket: number) {
  return request<Blob>({
    url: '/v1/captcha/image',
    method: 'get',
    params: { ticket }
  });
}

export function fetchVerificationCaptcha(ticket: number, code: string) {
  return request<boolean>({
    url: '/v1/captcha/verification',
    method: 'post',
    params: { ticket, code }
  });
}
