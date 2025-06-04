/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
  /**
   * namespace Flow
   *
   * backend api module: "flow"
   */
  namespace Flow {
    type ProcessType = 'main' | 'child' | 'business';
    type AddSignType = 9 | 11;
    type TransferType = 0 | 1;

    type PerformType = 2 | 3 | 4;

    type FlowGroup = {
      categoryId: string;
      categoryName: string;
      categorySort: number;
      processList: Process[];
    };

    type Process = {
      title: string;
      createTime: string;
      processIcon: string;
      processId: string;
      processKey: string;
      processName: string;
      processSort: number;
      processState: number;
      processType: string;
      processVersion: number;
      remark: string;
      useScope: number;
    };

    type HistoryProcess = Pick<Process, 'createTime' | 'processIcon' | 'processName' | 'processVersion' | 'remark'> & {
      id: string;
    };

    type HistoryProcessSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: {
          processId?: string;
        };
      }
    >;

    type HistoryProcessList = Common.PaginatingQueryRecord<HistoryProcess>;

    type FlowGroupCreateParams = {
      name: string;
      categorySort: number;
    };

    type FlowGroupUpdateParams = {
      id: string;
      name: string;
    };

    type FlowGroupSortParams = {
      categoryId: string;
      processIds: string[];
    }[];

    type ProcessSearchParams = {
      keyword: string;
    };

    type FlowGroupList = FlowGroup[];

    type FlowCreate = {
      processId: string;
      processKey: string;
      processName: string;
      processIcon: string;
      remark: string;
      categoryId: string;
      useScope: number;
      modelContent: string;
      processForm: string;
      processSetting: ProcessSetting;
    };

    type ProcessSetting = {
      allowRevocation: boolean;
      allowRevocationDay: boolean;
      allowUpdateDay: boolean;
      allowDelegate: boolean;
      allowBatchOperate: boolean;
      secondOperatePrompt: boolean;
      repeatOperateSkip: boolean;
    };

    type FormCategory = Api.Common.TreeData<{
      status: number;
      sort: number;
      remark: string;
      pid: string;
    }>;

    /** form category list */
    type FormCategoryList = CommonType.RecordNullable<Api.Flow.FormCategory>[];

    /** form template category form params */
    type FormCategoryFormParams = CommonType.RecordNullable<
      Pick<Api.Flow.FormCategory, 'id' | 'pid' | 'name' | 'status' | 'sort' | 'remark'>
    >;

    type FormEnableStatus = 0 | 1 | 3;

    type FormTemplateType = 0 | 1 | 2;

    type FormTemplate = Common.CommonRecord<object> & {
      id: string;
      code: string;
      type: FormTemplateType | null;
      name: string;
      content: string;
      pcUrl: string;
      formCategoryId: string;
      appUrl: string;
      sort: number;
    } & { status: FormEnableStatus | null };

    /** form template list */
    type FormTemplateList = Api.Common.PaginatingQueryRecord<FormTemplate>;

    /** form template search params */
    type FormTemplateSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Api.Flow.FormTemplate, 'name' | 'status' | 'type' | 'formCategoryId'>>;
      }
    >;

    /** form template form params */
    type FormTemplateFormParams = CommonType.RecordNullable<
      Pick<
        Api.Flow.FormTemplate,
        | 'id'
        | 'formCategoryId'
        | 'name'
        | 'content'
        | 'code'
        | 'type'
        | 'appUrl'
        | 'pcUrl'
        | 'status'
        | 'sort'
        | 'remark'
      >
    >;

    type FormTemplateSimple = {
      id: string;
      name: string;
      code: string;
    };

    /** form template simple search params */
    type FormTemplateSimpleSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: Pick<Api.Flow.FormTemplateSimple, 'name'> & {
          formCategoryId: string;
        };
      }
    >;

    /** form template simple list */
    type FormTemplateSimpleList = Common.PaginatingQueryRecord<FormTemplateSimple>;

    type ChildProcessOptions = {
      id: string;
      processName: string;
    }[];

    type LaunchList = {
      categoryId: string;
      categoryName: string;
      categorySort: number;
      processList: Process[];
    }[];

    type AssigneeMap = {
      type: Workflow.SelectType;
      assigneeList: {
        id: string;
        name: string;
        tenantId?: string;
        weight?: number;
      }[];
    };

    type LaunchFormParams = {
      processId: string;
      processForm: string;
      saveAsDraft: boolean;
      assigneeMap: {
        [key: string]: AssigneeMap;
      };
    };

    type InstanceState = -1 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7;

    type Approval = {
      processId: string;
      processName: string;
      processType: ProcessType;
      instanceId: string;
      instanceState: InstanceState;
      launchBy: string;
      launchTime: string;
      taskId: string;
      createBy: string;
      createTime: string;
      taskName: string;
      taskKey: string;
      taskType: number;
      performType: PerformType;
      remindRepeat: number;
      currentNodeName: string;
      duration: number;
      endTime: string;
    };

    type ApprovalSearchParams = Api.Common.CommonSearchParams & {
      data: CommonType.RecordNullable<
        Pick<Api.Flow.Approval, 'instanceState' | 'processName' | 'instanceId'> & {
          createBy: string;
          beginTime: string;
          endTime: string;
        }
      >;
    };

    type ApprovalInfo = {
      modelContent: string;
      formConfig: Workflow.FormConfig[];
      createBy: string;
      createTime: number;
      formContent: string;
      formTemplate: FormTemplate;
      renderNodes: { [key: string]: 0 | 1 };
      processApprovals: (Pick<Approval, 'taskName' | 'instanceId' | 'taskKey' | 'taskId'> & {
        createBy: string;
        createId: string;
        createTime: number;
        type: number;
        content: {
          opinion: string;
          nodeUserList: { id: string; name: string }[];
          nodeRoleList: { id: string; name: string }[];
        };
      })[];
      allowAppendNode: boolean;
      allowRollback: boolean;
      allowTransfer: boolean;
      allowCc: boolean;
      processSetting: {
        allowRevocation: boolean;
        allowRevocationDay: boolean;
        allowUpdateDay: boolean;
        allowDelegate: boolean;
        allowBatchOperate: boolean;
        secondOperatePrompt: boolean;
        repeatOperateSkip: boolean;
      };
      rejectStrategy?: Workflow.RejectStrategy;
    } & Approval;

    type ApprovalInfoSearchParams = CommonType.RecordNullable<
      Pick<Api.Flow.Approval, 'instanceState' | 'instanceId' | 'taskId'>
    >;

    type ApprovalList = Common.PaginatingQueryRecord<Approval>;

    type PendingTaskComment = {
      content: string;
      taskId: string;
      instanceId: string;
    };

    type PendingTaskConsent = {
      content: string;
      taskId: string;
      processForm: string;
      assigneeMap?: { [key: string]: Api.Flow.AssigneeMap };
    };

    type PendingTaskRejection = {
      nodeKey?: string;
      content: string;
      taskId: string;
      termination: boolean;
    };

    type PendingTaskAppendNode = {
      type?: AddSignType;
      nodeName?: string;
      userId?: string;
      username?: string;
      content: string;
      taskId: string;
      processForm: string;
    };

    type PendingTaskTransfer = {
      type?: TransferType;
      userId?: string;
      username?: string;
      content: string;
      taskId: string;
      processForm: string;
    };

    type PendingTaskJump = {
      nodeName?: string;
      content: string;
      taskId: string;
    };

    type PreviousNode = {
      nodeKey: string;
      nodeName: string;
    };

    type PendingTaskRevoke = {
      content: string;
      taskId: string;
      instanceId: string;
      termination: boolean;
    };

    type NextNodes = {
      nodeType: Workflow.NodeType;
      nodeModels: Workflow.NodeModel[];
    };

    type NextNodesParams = {
      instanceId: string;
      args?: object;
    };

    type CarbonCopyParams = {
      taskId: string;
      userIds: string[];
      content: string;
    };
  }
}
