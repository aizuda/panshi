/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
  /**
   * namespace System
   *
   * backend api module: "system"
   */
  namespace System {
    /** menu type */
    type MenuType = -1 | 0 | 1 | 2 | 3;

    type MenuButton = {
      /**
       * button code
       *
       * it can be used to control the button permission
       */
      code: string;
      /** button description */
      desc: string;
    };

    /**
     * icon type
     *
     * - "1": iconify icon
     * - "2": local icon
     */
    type IconType = '1' | '2';

    type MenuPropsOfRoute = Pick<
      import('vue-router').RouteMeta,
      | 'i18nKey'
      | 'keepAlive'
      | 'constant'
      | 'order'
      | 'href'
      | 'hideInMenu'
      | 'activeMenu'
      | 'multiTab'
      | 'fixedIndexInTab'
      | 'query'
    >;

    type Menu = Common.CommonRecord<MenuFormParams> & MenuPropsOfRoute;

    /** menu list */
    type MenuList = Common.PaginatingQueryRecord<Menu>;

    /** menu Form */
    type MenuFormParams = {
      id?: string;
      /** parent menu id */
      pid: string;
      /** parent route */
      parentRoute?: string;
      /** menu type */
      type: MenuType;
      /** menu name */
      title: string;
      /** route name */
      alias?: string;
      /** route path */
      path: string;
      /** component */
      component?: string;
      /** iconify icon name or local icon name */
      icon: string | null;
      /** sort */
      sort: number;
      /** icon type */
      iconType?: IconType;
      /** buttons */
      buttons?: MenuButton[] | null;
      /** redirect url */
      redirect?: string;
      /** hidden */
      hidden: boolean;
      /** status */
      status: Api.Common.EnableStatus;
      /** keep alive */
      keepAlive: boolean;
      /** query */
      query: string;
      apiList?: ApiPermission[];
      /** children menu */
      children?: Menu[] | null;
    };

    /** menu list */
    type MenuTreeList = Menu[];

    type MenuTree = {
      id: number;
      label: string;
      pId: number;
      children?: MenuTree[];
    };

    type ApiPermission = {
      id?: string;
      resourceId?: string;
      code: string;
      remark: string;
    };

    type Department = Common.CommonRecord<{
      pid: string;
      name: string;
      code: string;
      sort: number;
      remark: string;
      status: Common.EnableStatus;
      headId?: string;
      headName?: string;
      children: Department[];
    }>;

    /** department list */
    type DepartmentList = Department[];

    /** department search params */
    type DepartmentSearchParams = CommonType.RecordNullable<
      Pick<Api.System.Department, 'name' | 'status' | 'pid'> & Api.Common.CommonSearchParams
    >;

    /** department form */
    type DepartmentFormParams = CommonType.RecordNullable<Department>;

    /** user */
    type User = Common.CommonRecord<{
      username: string;
      password: string;
      realName: string;
      nickName: string;
      sex: string;
      phoneVerified: number;
      emailVerified: number;
      status: Common.EnableStatus;
      phone: string;
      avatar: string;
      email: string;
      jobNum: string;
    }> &
      UserRoles;

    /** user search params */
    type UserSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<
          Pick<Api.System.User, 'username' | 'realName' | 'status' | 'nickName' | 'phone'> & {
            departmentId: string;
          }
        >;
      }
    >;

    /** user form parmas */
    type UserFormParams = CommonType.RecordNullable<
      Pick<
        Api.System.User,
        | 'id'
        | 'username'
        | 'password'
        | 'nickName'
        | 'realName'
        | 'sex'
        | 'status'
        | 'roleIds'
        | 'departmentIds'
        | 'phone'
        | 'email'
        | 'remark'
      >
    >;

    /** user list */
    type UserList = Common.PaginatingQueryRecord<User>;

    type UserRoles = {
      roleIds: string[];
      departmentIds: string[];
    };

    type Role = Common.CommonRecord<{
      name: string;
      alias: string;
      sort: number;
      status: Common.EnableStatus;
    }>;

    /** role search params */
    type RoleSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Api.System.Role, 'name' | 'alias' | 'status'>>;
      }
    >;

    /** role form parmas */
    type RoleFormParams = CommonType.RecordNullable<
      Pick<Api.System.Role, 'id' | 'name' | 'alias' | 'sort' | 'status' | 'remark'> & { resourceIds: string[] }
    >;

    /** role list */
    type RoleList = Common.PaginatingQueryRecord<Role>;

    type Post = Common.CommonRecord<{
      name: string;
      code: string;
      sort: number;
      status: number;
    }>;

    /** post list */
    type PostList = Common.PaginatingQueryRecord<Post>;

    /** post search params */
    type PostSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Api.System.Post, 'name' | 'code' | 'status'>>;
      }
    >;

    /** post form parmas */
    type PostFormParams = CommonType.RecordNullable<
      Pick<Api.System.Post, 'id' | 'name' | 'code' | 'sort' | 'status' | 'remark'>
    >;

    type Config = Common.CommonRecord<{
      category: string;
      keyword: string;
      content: string;
      title: string;
      sort: number;
    }>;

    /** config list */
    type ConfigList = Common.PaginatingQueryRecord<Config>;

    /** config search params */
    type ConfigSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Api.System.Config, 'title' | 'keyword' | 'category'>>;
      }
    >;

    /** config form parmas */
    type ConfigFormParams = CommonType.RecordNullable<
      Pick<Api.System.Config, 'id' | 'title' | 'keyword' | 'sort' | 'category' | 'content'>
    >;

    type App = Common.CommonRecord<{
      identification: string;
      name: string;
      secretKey: string;
      status: number;
      expire: string;
      sort: number;
    }>;

    /** app list */
    type AppList = Common.PaginatingQueryRecord<App>;

    /** app search params */
    type AppSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Api.System.App, 'name' | 'status' | 'identification'>>;
      }
    >;

    /** app form parmas */
    type AppFormParams = CommonType.RecordNullable<
      Pick<Api.System.App, 'id' | 'name' | 'identification' | 'sort' | 'secretKey' | 'expire' | 'status'>
    >;

    type RegionLevel = 0 | 1 | 2;

    type Region = Common.CommonRecord<{
      pid: string;
      name: string;
      code: string;
      sort: number;
      level: RegionLevel;
      children: Department[];
    }>;

    /** region list */
    type RegionList = Region[];

    /** region search params */
    type RegionSearchParams = CommonType.RecordNullable<
      Pick<Api.System.Region, 'name' | 'code' | 'level' | 'pid'> & Api.Common.CommonSearchParams
    >;

    /** region form */
    type RegionFormParams = CommonType.RecordNullable<Region>;

    type Dict = Common.CommonRecord<{
      pid: string;
      name: string;
      code: string;
      status: number;
      sort: number;
      content: string;
    }>;

    /** dict list */
    type DictList = Common.PaginatingQueryRecord<Dict>;

    /** dict search params */
    type DictSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Api.System.Dict, 'name' | 'code' | 'status' | 'pid' | 'remark'>>;
      }
    >;

    /** dict form */
    type DictFormParams = CommonType.RecordNullable<Dict>;

    type DictParent = CommonType.RecordNullable<Pick<Api.System.Dict, 'id' | 'name' | 'code' | 'sort' | 'remark'>>;

    /** dict parent list */
    type DictParentList = DictParent[];

    type DictSelectOption = {
      title: string;
      content: string;
    };

    type MessageType = 0 | 1 | 2;

    type SendStatus = 0 | 1 | 2;

    type Viewed = 0 | 1;

    type Message = Common.CommonRecord<{
      title: string;
      content: string;
      category: MessageType;
      userId: string;
      username: string;
      viewed: Viewed;
      sendStatus: SendStatus;
      sendTime: number;
      sendFailure: string;
      businessType: string;
      businessId: string;
    }>;

    /** message list */
    type MessageList = Common.PaginatingQueryRecord<Message>;

    /** message search params */
    type MessageSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<
          Pick<Api.System.Message, 'title' | 'category' | 'sendStatus' | 'username' | 'createBy'>
        >;
      }
    >;

    type MessageInform = {
      totalNum: string;
      noticeNum: string;
      noticeList: Message[];
      messageNum: string;
      messageList: Message[];
      todoNum: string;
      todoList: Message[];
    };
  }
}
