<script setup lang="tsx">
import { ref } from 'vue';
import { NButton, NPopconfirm, NTag, NTooltip } from 'naive-ui';
import { regionLevelRecord } from '@/constants/business';
import { fetchBatchDeleteRegion, fetchGetRegionTree } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useTreeTable, useTreeTableOperate } from '@/hooks/common/tree-table';
import { $t } from '@/locales';
import ButtonIcon from '@/components/custom/button-icon.vue';
import SvgIcon from '@/components/custom/svg-icon.vue';
import RegionOperateDrawer from './modules/region-operate-drawer.vue';
import RegionSearch from './modules/region-search.vue';

const appStore = useAppStore();

const pid = ref();

const { columns, columnChecks, data, loading, getData, searchParams, resetSearchParams } = useTreeTable({
  apiFn: fetchGetRegionTree,
  apiParams: {
    name: null,
    code: null,
    level: null,
    pid: null
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
      title: ' 区域名称',
      minWidth: 120,
      ellipsis: true
    },
    {
      key: 'code',
      title: '区域编码',
      minWidth: 80,
      ellipsis: true
    },
    {
      key: 'level',
      title: '区域级别',
      align: 'center',
      minWidth: 120,
      render: row => {
        if (row.level === null) return null;

        const tagMap: Record<Api.System.RegionLevel, NaiveUI.ThemeColor> = {
          0: 'primary',
          1: 'success',
          2: 'warning'
        };

        return <NTag type={tagMap[row.level]}>{regionLevelRecord[row.level]} </NTag>;
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
      width: 120,
      fixed: 'right',
      render: row => (
        <div class="flex-center gap-12px">
          <ButtonIcon type="primary" text icon="ep:edit" tooltipContent={$t('common.edit')} onClick={() => edit(row)} />
          <ButtonIcon
            type="primary"
            class="text-22px"
            text
            icon="ic-round-plus"
            tooltipContent="新增子区域"
            onClick={() => handleAddRegion(row.id)}
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
} = useTreeTableOperate(data, getData);

function handleAddRegion(id: string) {
  pid.value = id;
  handleAdd();
}

async function handleBatchDelete() {
  // request
  const { error } = await fetchBatchDeleteRegion(checkedRowKeys.value);
  if (error) return;

  onBatchDeleted();
}

async function handleDelete(id: string) {
  // request
  const { error } = await fetchBatchDeleteRegion([id]);
  if (error) return;

  onDeleted();
}

function edit(row: Api.System.Region) {
  handleEdit(row);
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden sm:h-full lt-sm:overflow-auto">
    <RegionSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getData" />
    <NCard title="行政区域" :bordered="false" size="small" class="h-full sm:flex-1-hidden card-wrapper">
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
        :scroll-x="1088"
        :loading="loading"
        :row-key="row => row.id"
        remote
        class="h-full"
        @update:page="clearCheckedRowKeys"
      />

      <RegionOperateDrawer
        v-model:visible="drawerVisible"
        :operate-type="operateType"
        :row-data="editingData"
        :tree-data="data"
        :pid="pid"
        @submitted="getData"
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
    max-height: calc(100vh - 298px - var(--calc-footer-height, 0px));
  }
}

:deep(.n-card-header__main) {
  min-width: 69px !important;
}
</style>
