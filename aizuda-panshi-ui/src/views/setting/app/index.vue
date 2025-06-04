<script setup lang="tsx">
import { NButton, NPopconfirm, NTooltip } from 'naive-ui';
import { enableStatusRecord } from '@/constants/business';
import { fetchBatchDeleteApp, fetchGetAppList, fetchUpdateAppStatus } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import StatusSwitch from '@/components/common/status-switch.vue';
import ButtonIcon from '@/components/custom/button-icon.vue';
import SvgIcon from '@/components/custom/svg-icon.vue';
import AppOperateDrawer from './modules/app-operate-drawer.vue';
import AppSearch from './modules/app-search.vue';

const appStore = useAppStore();

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
  apiFn: fetchGetAppList,
  apiParams: {
    page: 1,
    pageSize: 10,
    // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
    // the value can not be undefined, otherwise the property in Form will not be reactive
    data: {
      name: null,
      identification: null,
      status: null
    }
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      titleAlign: 'center',
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
      title: '应用名称',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'identification',
      title: '应用标识',
      minWidth: 80,
      ellipsis: true
    },
    {
      key: 'secretKey',
      title: '应用密钥',
      minWidth: 120,
      ellipsis: true
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
          const { error } = await fetchUpdateAppStatus(row.id, status);
          if (!error) window.$message?.success(`${enableStatusRecord[status]}成功`);
          callback(!error);
        }

        return <StatusSwitch info={`应用 ${row.name} `} v-model:value={row.status} onSubmitted={handleUpdateStatus} />;
      }
    },
    {
      key: 'sort',
      titleAlign: 'center',
      title: '排序',
      align: 'center',
      minWidth: 60
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
      titleAlign: 'center',
      align: 'center',
      fixed: 'right',
      width: 90,
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
  clearCheckedRowKeys
  // closeDrawer
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  // request
  const { error } = await fetchBatchDeleteApp(checkedRowKeys.value);
  if (error) return;

  onBatchDeleted();
}

async function handleDelete(id: string) {
  // request
  const { error } = await fetchBatchDeleteApp([id]);
  if (error) return;

  onDeleted();
}

function edit(id: string) {
  handleEdit(id);
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden sm:h-full lt-sm:overflow-auto">
    <AppSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
    <NCard title="应用列表" :bordered="false" size="small" class="h-full sm:flex-1-hidden card-wrapper">
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @add="handleAdd"
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
        :scroll-x="900"
        :loading="loading"
        remote
        :row-key="row => row.id"
        :pagination="mobilePagination"
        class="h-full"
        @update:page="clearCheckedRowKeys"
      />
      <AppOperateDrawer
        v-model:visible="drawerVisible"
        :operate-type="operateType"
        :row-data="editingData"
        @submitted="getDataByPage"
      />
    </NCard>
  </div>
</template>

<style scoped lang="scss">
:deep(.n-data-table-wrapper),
:deep(.n-data-table-base-table),
:deep(.n-data-table-base-table-body) {
  height: 100%;
}

@media screen and (max-width: 718px) {
  :deep(.n-data-table-base-table-body) {
    max-height: calc(100vh - 338px - var(--calc-footer-height, 0px));
  }
}

:deep(.n-card-header__main) {
  min-width: 69px !important;
}
</style>
