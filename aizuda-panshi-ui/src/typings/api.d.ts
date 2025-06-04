/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
  namespace Common {
    /** common search params of table */
    interface CommonSearchParams {
      page: number;
      pageSize: number;
    }

    /** common params of paginating */
    interface PaginatingCommonParams {
      /** current page number */
      current: number;
      /** pages number */
      pages: number;
      /** page size */
      size: number;
      /** total count */
      total: number;
    }

    /** common params of paginating query list data */
    interface PaginatingQueryRecord<T = any> extends PaginatingCommonParams {
      records: T[];
    }

    /**
     * enable status
     *
     * - "0": disabled
     * - "1": enabled
     */
    type EnableStatus = 0 | 1;

    /** common record */
    type CommonRecord<T = any> = {
      /** record id */
      id: string;
      /** record creator */
      createBy: string;
      /** record create time */
      createTime: string;
      /** record updater */
      updateBy: string;
      /** record update time */
      updateTime: string;
      /** record remark */
      remark?: string;
    } & T;

    type Options = {
      id: string;
      name: string;
      disabled?: boolean;
      show?: boolean;
      children?: Api.Common.TreeList;
    }[];

    type TreeData<T = any> = {
      id: string;
      name: string;
      children?: TreeList;
    } & T;

    type TreeList<T = any> = TreeData<T>[];
  }

  /**
   * namespace Auth
   *
   * backend api module: "auth"
   */
  namespace Auth {
    interface LoginToken {
      token: string;
      userInfo: {
        userId: string;
        userName: string;
      };
    }

    interface LoginBody {
      username: string;
      password: string;
      code: string;
      autologin: boolean;
    }

    type UserInfo = Common.CommonRecord<{
      username: string;
      userName: string;
      realName: string;
      nickName: string;
      avatar: string;
      sex: string;
      phone: string;
      phoneVerified: number;
      email: string;
      emailVerified: number;
      jobNum: string;
      roles: string[];
      buttons: string[];
    }>;
  }

  /**
   * namespace Route
   *
   * backend api module: "route"
   */
  namespace Route {
    type ElegantConstRoute = import('@elegant-router/types').ElegantConstRoute;

    interface MenuRoute extends ElegantConstRoute {
      menu: ElegantConstRoute[];
      permissions: string[];
    }
  }
}
