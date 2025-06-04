import { request } from '../request';

/** Login */
export function fetchLogin(data: Api.Auth.LoginBody) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/login/system',
    method: 'post',
    data
  });
}

/** Get user info */
export function fetchGetUserInfo() {
  return request<Api.Auth.UserInfo>({
    url: '/sys/user/info',
    method: 'get'
  });
}

/**
 * Refresh token
 *
 * @param refreshToken Refresh token
 */
export function fetchRefreshToken(refreshToken: string) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/refreshToken',
    method: 'post',
    data: {
      refreshToken
    }
  });
}

/**
 * return custom backend error
 *
 * @param code error code
 * @param msg error message
 */
export function fetchCustomBackendError(code: string, msg: string) {
  return request({ url: '/auth/error', params: { code, msg } });
}

export function fetchAuthLoginToken(token: string) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/login/token',
    method: 'post',
    data: { token }
  });
}
