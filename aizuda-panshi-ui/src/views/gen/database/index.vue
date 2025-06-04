<script setup lang="tsx">
import { ref } from 'vue';
import { NButton, NPopconfirm, NTooltip } from 'naive-ui';
import { useBoolean } from '@sa/hooks';
import { fetchBatchDeleteGenDatabase, fetchGetGenDatabaseList } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import ButtonIcon from '@/components/custom/button-icon.vue';
import SvgIcon from '@/components/custom/svg-icon.vue';
import DatabaseOperateDrawer from './modules/database-operate-drawer.vue';
import DatabaseSearch from './modules/database-search.vue';
import CodeGenerationDrawer from './modules/code-generation-drawer.vue';

const genDatabase = ref<Api.Gen.Database>();
const { bool: codeGenVisible, setTrue: openCodeGenDrawer } = useBoolean();

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
  apiFn: fetchGetGenDatabaseList,
  apiParams: {
    page: 1,
    pageSize: 10,
    // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
    // the value can not be undefined, otherwise the property in Form will not be reactive
    data: {
      dbName: null,
      alias: null,
      type: null
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
      key: 'alias',
      title: '别名',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'dbName',
      title: '库名称',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'type',
      titleAlign: 'center',
      title: '类型',
      align: 'center',
      minWidth: 80
    },
    {
      key: 'username',
      title: '用户名',
      align: 'center',
      minWidth: 120
    },
    {
      key: 'createTime',
      title: '创建时间',
      titleAlign: 'center',
      align: 'center',
      minWidth: 150
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
          <ButtonIcon
            type="primary"
            text
            icon="hugeicons:source-code-square"
            tooltipContent="生成代码"
            onClick={() => genCode(row)}
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
  const { error } = await fetchBatchDeleteGenDatabase(checkedRowKeys.value);
  if (error) return;

  onBatchDeleted();
}

async function handleDelete(id: string) {
  // request
  const { error } = await fetchBatchDeleteGenDatabase([id]);
  if (error) return;

  onDeleted();
}

function edit(id: string) {
  handleEdit(id);
}

function genCode(row?: Api.Gen.Database) {
  genDatabase.value = row || undefined;
  openCodeGenDrawer();
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden sm:h-full lt-sm:overflow-auto">
    <DatabaseSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
    <NCard title="数据源列表" :bordered="false" size="small" class="h-full sm:flex-1-hidden card-wrapper">
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @add="handleAdd"
          @delete="handleBatchDelete"
          @refresh="getData"
        >
          <template #prefix>
            <NButton size="small" ghost type="primary" @click="() => genCode()">
              <template #icon>
                <icon-hugeicons:source-code-square class="text-icon" />
              </template>
              生成代码
            </NButton>
          </template>
        </TableHeaderOperation>
      </template>
      <NDataTable
        v-model:checked-row-keys="checkedRowKeys"
        :columns="columns"
        :data="data"
        size="small"
        :flex-height="!appStore.isMobile"
        :scroll-x="702"
        :loading="loading"
        remote
        :row-key="row => row.id"
        :pagination="mobilePagination"
        class="h-full"
        @update:page="clearCheckedRowKeys"
      />
      <DatabaseOperateDrawer
        v-model:visible="drawerVisible"
        :operate-type="operateType"
        :row-data="editingData"
        @submitted="getDataByPage"
      />
      <CodeGenerationDrawer v-model:visible="codeGenVisible" :row-data="genDatabase" />
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
