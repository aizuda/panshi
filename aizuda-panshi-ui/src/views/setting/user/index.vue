<script setup lang="tsx">
import { ref } from 'vue';
import { NButton, NPopconfirm, NTooltip } from 'naive-ui';
import { useBoolean, useLoading } from '@sa/hooks';
import { enableStatusRecord } from '@/constants/business';
import { fetchBatchDeleteUser, fetchGetDeptTree, fetchGetUserList, fetchUpdateUserStatus } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import StatusSwitch from '@/components/common/status-switch.vue';
import ButtonIcon from '@/components/custom/button-icon.vue';
import SvgIcon from '@/components/custom/svg-icon.vue';
import UserSearch from './modules/user-search.vue';
import UserOperateDrawer from './modules/user-operate-drawer.vue';
import UserResetPwdDrawer from './modules/user-reset-pwd-drawer.vue';
import UserRoleOperateDrawer from './modules/user-role-operate-drawer.vue';
import UserDeptOperateDrawer from './modules/user-dept-operate-drawer.vue';

const appStore = useAppStore();
const resetPwdRowKeys = ref<string[]>([]);
const { bool: resetPwdVisible, setTrue: openResetPwdDrawer } = useBoolean();
const { bool: deptVisible, setTrue: openDeptDrawer } = useBoolean();
const { bool: roleVisible, setTrue: openRoleDrawer } = useBoolean();

const {
  columns,
  columnChecks,
  data,
  getData,
  getDataByPage,
  loading,
  mobilePagination,
  searchParams,
  resetSearchParams
} = useTable({
  apiFn: fetchGetUserList,
  showTotal: true,
  apiParams: {
    page: 1,
    pageSize: 10,
    // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
    // the value can not be undefined, otherwise the property in Form will not be reactive
    data: {
      status: null,
      username: null,
      departmentId: null
    }
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'index',
      title: $t('common.index'),
      align: 'center',
      width: 64
    },
    {
      key: 'username',
      title: '用户名',
      align: 'center',
      minWidth: 100,
      ellipsis: true
    },
    {
      key: 'realName',
      title: '姓名',
      align: 'center',
      minWidth: 100,
      ellipsis: true
    },
    {
      key: 'nickName',
      title: '昵称',
      align: 'center',
      minWidth: 100,
      ellipsis: true
    },
    {
      key: 'phone',
      title: '手机号',
      align: 'center',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'status',
      title: '状态',
      align: 'center',
      minWidth: 100,
      render: row => {
        async function handleUpdateStatus(status: Api.Common.EnableStatus, callback: (flag: boolean) => void) {
          // request
          const { error } = await fetchUpdateUserStatus(row.id, status);
          if (!error) window.$message?.success(`${enableStatusRecord[status]}成功`);
          callback(!error);
        }

        return (
          <StatusSwitch info={`用户 ${row.username} `} v-model:value={row.status} onSubmitted={handleUpdateStatus} />
        );
      }
    },
    {
      key: 'createTime',
      title: '创建时间',
      align: 'center',
      width: 150
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 120,
      fixed: 'right',
      render: row => (
        <div class="flex-center gap-16px">
          <ButtonIcon
            type="primary"
            text
            icon="ep:edit"
            tooltipContent={$t('common.edit')}
            onClick={() => edit(row.id)}
          />
          <ButtonIcon
            type="primary"
            text
            icon="carbon:password"
            tooltipContent="重置密码"
            onClick={() => {
              resetPwdRowKeys.value = [row.id];
              openResetPwdDrawer();
            }}
          />
          <NTooltip placement="bottom">
            {{
              trigger: () => (
                <NPopconfirm onPositiveClick={() => handleDelete(row.id)}>
                  {{
                    default: () => $t('common.confirmDelete'),
                    trigger: () => (
                      <NButton class="h-36px text-icon" type="error" text>
                        {{
                          default: () => (
                            <div class="flex-center gap-8px">
                              <SvgIcon icon="ep:delete" />
                            </div>
                          )
                        }}
                      </NButton>
                    )
                  }}
                </NPopconfirm>
              ),
              default: () => <>{$t('common.delete')}</>
            }}
          </NTooltip>
        </div>
      )
    }
  ]
});

const {
  drawerVisible,
  operateType,
  editingData,
  handleAdd,
  handleEdit,
  checkedRowKeys,
  onBatchDeleted,
  onDeleted
  // closeDrawer
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  // request
  const { error } = await fetchBatchDeleteUser(checkedRowKeys.value);
  if (error) return;

  onBatchDeleted();
}

async function handleDelete(id: string) {
  // request
  const { error } = await fetchBatchDeleteUser([id]);
  if (error) return;

  onDeleted();
}

function edit(id: string) {
  handleEdit(id);
}

const { loading: treeLoading, startLoading: startTreeLoading, endLoading: endTreeLoading } = useLoading();
const treePattern = ref<string>();
const treeData = ref<Api.System.DepartmentList>([]);

async function getTreeData() {
  startTreeLoading();
  const { data: tree, error } = await fetchGetDeptTree({});
  if (!error) {
    treeData.value = tree;
  }
  endTreeLoading();
}

getTreeData();

function handleClickTree(keys: string[]) {
  searchParams.data!.departmentId = keys.length ? keys[0] : null;
  checkedRowKeys.value = [];
  getDataByPage();
}

function handleResetTreeData() {
  treePattern.value = undefined;
  getTreeData();
}

function handleResetPassword() {
  resetPwdRowKeys.value = checkedRowKeys.value;
  openResetPwdDrawer();
}

function handleAssignDept() {
  openDeptDrawer();
}

const moreOptions = [
  {
    label: '分配角色',
    key: 'role',
    icon: () => <SvgIcon local-icon="menu-role" />,
    click: () => openRoleDrawer()
  },
  {
    type: 'divider'
  },
  {
    label: '分配部门',
    key: 'dept',
    icon: () => <SvgIcon icon="ep:wind-power" />,
    click: () => handleAssignDept()
  },
  {
    type: 'divider'
  },
  {
    label: '重置密码',
    key: 'pwd',
    icon: () => <SvgIcon icon="carbon:password" />,
    click: () => handleResetPassword()
  }
];

const handleSelect = (key: string) => {
  moreOptions.find(item => item.key === key)?.click?.();
};
</script>

<template>
  <TableSiderLayout title="部门列表">
    <template #header-extra>
      <NButton size="small" text class="h-18px" @click.stop="() => handleResetTreeData()">
        <template #icon>
          <SvgIcon icon="ic:round-refresh" />
        </template>
      </NButton>
    </template>
    <template #sider>
      <NInput v-model:value="treePattern" clearable :placeholder="$t('common.keywordSearch')" />
      <NSpin class="dept-tree" :show="treeLoading">
        <NTree
          block-node
          show-line
          :data="treeData as []"
          :default-expanded-keys="treeData?.length ? [treeData[0].id!] : []"
          :show-irrelevant-nodes="false"
          :pattern="treePattern"
          block-line
          class="infinite-scroll h-full min-h-200px py-3"
          key-field="id"
          label-field="name"
          virtual-scroll
          @update:selected-keys="handleClickTree"
        >
          <template #empty>
            <NEmpty description="暂无部门信息" class="h-full min-h-200px justify-center" />
          </template>
        </NTree>
      </NSpin>
    </template>
    <div class="h-full flex-col-stretch gap-12px">
      <UserSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
      <NAlert type="info">
        <span v-if="checkedRowKeys.length">
          已选择{{ checkedRowKeys.length }}条记录
          <NButton class="pl-6px" text type="primary" @click="() => (checkedRowKeys = [])">清空</NButton>
        </span>
        <span v-else>未选中任何记录</span>
      </NAlert>
      <NCard title="用户列表" :bordered="false" size="small" class="h-full card-wrapper">
        <template #header-extra>
          <TableHeaderOperation
            v-model:columns="columnChecks"
            :disabled-delete="checkedRowKeys.length === 0"
            :loading="loading"
            @add="handleAdd"
            @delete="handleBatchDelete"
            @refresh="getData"
          >
            <template #suffix>
              <NDropdown
                trigger="hover"
                :options="moreOptions"
                :disabled="checkedRowKeys.length === 0"
                @select="handleSelect"
              >
                <NButton size="small" ghost icon-placement="right" :disabled="checkedRowKeys.length === 0">
                  <template #icon>
                    <icon-ep:more class="text-icon" />
                  </template>
                  更多
                </NButton>
              </NDropdown>
            </template>
          </TableHeaderOperation>
        </template>
        <NDataTable
          v-model:checked-row-keys="checkedRowKeys"
          :columns="columns"
          :data="data"
          size="small"
          :flex-height="!appStore.isMobile"
          :scroll-x="962"
          :loading="loading"
          remote
          :row-key="row => row.id"
          :pagination="mobilePagination"
          class="h-full"
        />
        <UserOperateDrawer
          v-model:visible="drawerVisible"
          :operate-type="operateType"
          :row-data="editingData"
          :tree-data="treeData"
          @submitted="getDataByPage"
        />
        <UserResetPwdDrawer v-model:visible="resetPwdVisible" :checked-row-keys="resetPwdRowKeys" />
        <UserRoleOperateDrawer
          v-model:visible="roleVisible"
          :checked-row-keys="checkedRowKeys"
          @submitted="getDataByPage"
        />
        <UserDeptOperateDrawer
          v-model:visible="deptVisible"
          :checked-row-keys="checkedRowKeys"
          :tree-data="treeData"
          @submitted="getDataByPage"
        />
      </NCard>
    </div>
  </TableSiderLayout>
</template>

<style scoped lang="scss">
.n-alert {
  --n-padding: 5px 13px !important;
  --n-icon-margin: 6px 8px 0 12px !important;
  --n-icon-size: 20px !important;
}

.dept-tree {
  .n-button {
    --n-padding: 8px !important;
  }

  :deep(.n-tree__empty) {
    height: 100%;
    justify-content: center;
  }

  :deep(.n-spin-content) {
    height: 100%;
  }

  :deep(.infinite-scroll) {
    height: calc(100vh - 228px - var(--calc-footer-height, 0px)) !important;
    max-height: calc(100vh - 228px - var(--calc-footer-height, 0px)) !important;
  }

  @media screen and (max-width: 1024px) {
    :deep(.infinite-scroll) {
      height: calc(100vh - 227px - var(--calc-footer-height, 0px)) !important;
      max-height: calc(100vh - 227px - var(--calc-footer-height, 0px)) !important;
    }
  }

  :deep(.n-tree-node) {
    height: 33px;
  }

  :deep(.n-tree-node-switcher) {
    height: 33px;
  }

  :deep(.n-tree-node-switcher__icon) {
    font-size: 16px !important;
    height: 16px !important;
    width: 16px !important;
  }
}

:deep(.n-data-table-wrapper),
:deep(.n-data-table-base-table),
:deep(.n-data-table-base-table-body) {
  height: 100%;
}

@media screen and (max-width: 800px) {
  :deep(.n-data-table-base-table-body) {
    max-height: calc(100vh - 400px - var(--calc-footer-height, 0px));
  }
}

@media screen and (max-width: 802px) {
  :deep(.n-data-table-base-table-body) {
    max-height: calc(100vh - 473px - var(--calc-footer-height, 0px));
  }
}

:deep(.n-card-header__main) {
  min-width: 69px !important;
}
</style>
