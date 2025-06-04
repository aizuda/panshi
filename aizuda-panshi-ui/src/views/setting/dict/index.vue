<script setup lang="tsx">
import { ref } from 'vue';
import type { TreeOption } from 'naive-ui';
import { NButton, NPopconfirm, NTooltip } from 'naive-ui';
import { useLoading } from '@sa/hooks';
import { enableStatusRecord } from '@/constants/business';
import { fetchBatchDeleteDict, fetchGetDictList, fetchGetDictParentList, fetchUpdateDictStatus } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import StatusSwitch from '@/components/common/status-switch.vue';
import ButtonIcon from '@/components/custom/button-icon.vue';
import SvgIcon from '@/components/custom/svg-icon.vue';
import DictSearch from './modules/dict-search.vue';
import DictOperateDrawer from './modules/dict-operate-drawer.vue';
import { jsonClone } from '~/packages/utils/src';

const appStore = useAppStore();

const pid = ref();

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
  apiFn: fetchGetDictList,
  showTotal: true,
  apiParams: {
    page: 1,
    pageSize: 10,
    // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
    // the value can not be undefined, otherwise the property in Form will not be reactive
    data: {
      status: null,
      name: null,
      code: null,
      pid: '0'
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
      key: 'name',
      title: '字典名称',
      align: 'center',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'code',
      title: '字典编码',
      align: 'center',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'content',
      title: '字典内容',
      align: 'center',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'sort',
      titleAlign: 'center',
      title: '排序',
      align: 'center',
      minWidth: 60
    },
    {
      key: 'status',
      title: '状态',
      titleAlign: 'center',
      align: 'center',
      minWidth: 80,
      render: row => {
        async function handleUpdateStatus(status: Api.Common.EnableStatus, callback: (flag: boolean) => void) {
          // request
          const { error } = await fetchUpdateDictStatus(row.id, status);
          if (!error) window.$message?.success(`${enableStatusRecord[status]}成功`);
          callback(!error);
        }

        return <StatusSwitch info={`字典 ${row.name} `} v-model:value={row.status} onSubmitted={handleUpdateStatus} />;
      }
    },
    {
      key: 'remark',
      title: '备注',
      maxWidth: 180,
      ellipsis: true
    },
    {
      key: 'createTime',
      title: '创建时间',
      titleAlign: 'center',
      align: 'center',
      width: 150
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 90,
      fixed: 'right',
      render: row => (
        <div class="flex-center gap-12px">
          <ButtonIcon
            type="primary"
            text
            icon="ep:edit"
            tooltipContent={$t('common.edit')}
            onClick={() => edit(row.id)}
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
  onDeleted,
  clearCheckedRowKeys,
  openDrawer
  // closeDrawer
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  // request
  const { error } = await fetchBatchDeleteDict(checkedRowKeys.value);
  if (error) return;

  onBatchDeleted();
}

async function handleDelete(id: string) {
  // request
  const { error } = await fetchBatchDeleteDict([id]);
  if (error) return;

  onDeleted();
}

function add() {
  pid.value = searchParams.data?.pid;
  handleAdd();
}

function edit(id: string) {
  handleEdit(id);
}

const { loading: treeLoading, startLoading: startTreeLoading, endLoading: endTreeLoading } = useLoading();
const treePattern = ref<string>();
const treeData = ref<Api.System.DictParentList>([]);

async function getTreeData() {
  startTreeLoading();
  const { data: tree, error } = await fetchGetDictParentList();
  if (!error) {
    treeData.value = tree;
    treeData.value.unshift({ id: '0', name: '所有' });
  }
  endTreeLoading();
}

getTreeData();

function handleClickTree(keys: string[]) {
  pid.value = keys.length ? keys[0] : null;
  searchParams.data!.pid = keys.length ? keys[0] : null;
  checkedRowKeys.value = [];
  getDataByPage();
}

function handleAddDict(id: string = '0') {
  pid.value = id;
  handleAdd();
}

function handleResetTreeData() {
  treePattern.value = undefined;
  getTreeData();
}

const showOperateId = ref<string | null>(null);

function nodeProps({ option }: { option: TreeOption }) {
  return {
    async onmouseover() {
      showOperateId.value = String(option.id);
    },
    async onmouseleave() {
      showOperateId.value = null;
    }
  };
}

function handleEditDict(row: NaiveUI.TableDataWithIndex<Api.System.Dict>) {
  operateType.value = 'edit';
  editingData.value = jsonClone(row);
  pid.value = '0';

  openDrawer();
}

function renderSuffix({ option }: { option: TreeOption }) {
  if (option.id !== showOperateId.value) {
    return null;
  }

  const updateBtn = () => {
    if (option.id === '0') {
      return null;
    }

    return (
      <ButtonIcon
        text
        class="h-18px flex-center text-14px"
        type="primary"
        icon="ep:edit"
        tooltip-content={$t('common.edit')}
        onClick={(event: Event) => {
          event.stopPropagation();
          handleEditDict(option as any);
        }}
      />
    );
  };

  const deleteForm = () => {
    window.$dialog?.warning({
      title: '系统提示',
      content: `确定要删除 ${option.name} 吗？`,
      positiveText: '确定',
      negativeText: '取消',
      onPositiveClick: () => {
        handleDelete(String(option.id));
      },
      onNegativeClick: () => {}
    });
  };

  const deleteBtn = () => {
    if (option.children || option.id === '0') {
      return null;
    }

    return (
      <>
        <ButtonIcon
          text
          type="error"
          icon="ep:delete"
          class="h-18px flex-center text-14px"
          tooltip-content={$t('common.delete')}
          onClick={(event: Event) => {
            event.stopPropagation();
            deleteForm();
          }}
        />
      </>
    );
  };

  return (
    <>
      <div class="flex-center gap-8px">
        <ButtonIcon
          text
          class="h-18px"
          type="primary"
          icon="ic-round-plus"
          tooltip-content={$t('common.add')}
          onClick={(event: Event) => {
            event.stopPropagation();
            handleAddDict(String(option.id));
          }}
        />
        {updateBtn()}
        {deleteBtn()}
      </div>
    </>
  );
}
</script>

<template>
  <TableSiderLayout title="字典分类">
    <template #header-extra>
      <NButton size="small" text class="h-18px p-8px" type="primary" @click.stop="() => handleAddDict()">
        <template #icon>
          <SvgIcon icon="ic-round-plus" />
        </template>
      </NButton>
      <NButton size="small" text class="h-18px p-8px" @click.stop="() => handleResetTreeData()">
        <template #icon>
          <SvgIcon icon="ic:round-refresh" />
        </template>
      </NButton>
    </template>
    <template #sider>
      <NInput v-model:value="treePattern" clearable :placeholder="$t('common.keywordSearch')" />
      <NSpin class="dict-tree" :show="treeLoading">
        <NTree
          block-node
          :cancelable="false"
          :data="treeData as []"
          :default-expanded-keys="treeData?.length ? [treeData[0].id!] : []"
          :show-irrelevant-nodes="false"
          :pattern="treePattern"
          block-line
          class="infinite-scroll h-full min-h-200px py-3"
          key-field="id"
          label-field="name"
          virtual-scroll
          :node-props="nodeProps"
          :render-suffix="renderSuffix"
          :default-selected-keys="['0']"
          @update:selected-keys="handleClickTree"
        >
          <template #empty>
            <NEmpty description="暂无字典分类信息" class="h-full min-h-200px justify-center" />
          </template>
        </NTree>
      </NSpin>
    </template>
    <div class="h-full flex-col-stretch gap-12px">
      <DictSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
      <NCard title="字典列表" :bordered="false" size="small" class="h-full card-wrapper">
        <template #header-extra>
          <TableHeaderOperation
            v-model:columns="columnChecks"
            :disabled-delete="checkedRowKeys.length === 0"
            :loading="loading"
            @add="add"
            @delete="handleBatchDelete"
            @refresh="getData"
          />
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
          @update:page="clearCheckedRowKeys"
        />
        <DictOperateDrawer
          v-model:visible="drawerVisible"
          :operate-type="operateType"
          :row-data="editingData"
          :tree-data="treeData"
          :pid="pid"
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

.dict-tree {
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

@media screen and (max-width: 1024px) {
  :deep(.n-data-table-base-table-body) {
    max-height: calc(100vh - 392px - var(--calc-footer-height, 0px));
  }
}

:deep(.n-card-header__main) {
  min-width: 69px !important;
}
</style>
