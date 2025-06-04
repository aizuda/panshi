<script setup lang="tsx">
import { ref } from 'vue';
import { type DataTableColumns, NButton, NIcon, NInput, type TreeInst, type TreeOption } from 'naive-ui';
import { useBoolean, useLoading } from '@sa/hooks';
import { menuTypeRecord } from '@/constants/business';
import {
  fetchCreateResourceApi,
  fetchDeleteMenu,
  fetchDeleteResourceApi,
  fetchGetMenuApiList,
  fetchGetMenuTreeList,
  fetchUpdateResourceApi
} from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { isNotNull } from '@/utils/common';
import SvgIcon from '@/components/custom/svg-icon.vue';
import ButtonIcon from '@/components/custom/button-icon.vue';
import { $t } from '@/locales';
import MenuOperateDrawer from './modules/menu-operate-drawer.vue';

const appStore = useAppStore();
const operateType = ref<NaiveUI.TableOperateType>('add');
const { loading, startLoading, endLoading } = useLoading();
const { loading: btnLoading, startLoading: startBtnLoading, endLoading: endBtnLoading } = useLoading();
const { bool: drawerVisible, setTrue: openDrawer } = useBoolean();
/** tree pattern name , use tree search */
const name = ref<string>();
const createPid = ref<string>('0');
const currentMenu = ref<Api.System.Menu>();
const treeData = ref<Api.System.Menu[]>([]);
const checkedKeys = ref<string[]>(['0']);
const expandedKeys = ref<string[]>(['0']);

const menuTreeRef = ref<TreeInst>();
const btnEditMap = ref<{ [key: number]: boolean }>({});
const btnData = ref<Api.System.ApiPermission[]>([]);

const getMeunTree = async () => {
  startLoading();
  const { data, error } = await fetchGetMenuTreeList();
  if (!error) {
    treeData.value = [
      { id: '0', title: '根目录', icon: 'material-symbols:home-outline-rounded', children: data }
    ] as Api.System.Menu[];
  }
  endLoading();
};

getMeunTree();

async function handleSubmitted() {
  await getMeunTree();
  if (operateType.value === 'edit') {
    currentMenu.value = menuTreeRef.value?.getCheckedData().options[0] as Api.System.Menu;
  }
}

function handleAddMenu(pid: string) {
  createPid.value = pid;
  operateType.value = 'add';
  openDrawer();
}

function handleUpdateMenu() {
  operateType.value = 'edit';
  openDrawer();
}

async function handleDeleteMenu() {
  const { error } = await fetchDeleteMenu(checkedKeys.value);
  if (error) return;
  window.$message?.success($t('common.deleteSuccess'));
  expandedKeys.value.filter(item => !checkedKeys.value.includes(item));
  currentMenu.value = undefined;
  checkedKeys.value = [];
  getMeunTree();
}

function renderPrefix({ option }: { option: TreeOption }) {
  const renderLocalIcon = String(option.icon).startsWith('icon-');
  const icon = renderLocalIcon ? undefined : String(option.icon);
  const localIcon = renderLocalIcon ? String(option.icon).replace('icon-', 'menu-') : undefined;
  return <SvgIcon icon={icon} localIcon={localIcon} />;
}

function renderSuffix({ option }: { option: TreeOption }) {
  if (![-1, 0].includes(Number(option.type))) {
    return <></>;
  }
  return (
    <>
      <div class="flex-center gap-8px">
        <ButtonIcon
          text
          class="h-18px"
          icon="ic-round-plus"
          tooltip-content="新增子菜单"
          onClick={(event: Event) => {
            event.stopPropagation();
            handleAddMenu(String(option.id));
          }}
        />
      </div>
    </>
  );
}

function reset() {
  name.value = undefined;
  getMeunTree();
}

function handleClickTree(option: Array<TreeOption | null>) {
  checkedKeys.value = option.map(item => String(item?.id));

  const menu = option[0] as Api.System.Menu;
  if (menu?.id === '0') {
    return;
  }
  currentMenu.value = menu;
  getApiList();
}

async function getApiList() {
  if (!currentMenu.value?.id) {
    return;
  }
  startBtnLoading();
  btnEditMap.value = {};
  btnData.value = [];
  const { data, error } = await fetchGetMenuApiList(currentMenu.value.id);
  if (error) return;
  btnData.value = data || [];
  endBtnLoading();
}

function addApiBtn() {
  btnData.value?.push({ code: '', remark: '' });
  btnEditMap.value[btnData.value.length - 1] = true;
}

async function handleDeleteMenuApi(id: string) {
  const { error } = await fetchDeleteResourceApi([id]);
  if (error) {
    return;
  }
  await getApiList();
  window.$message?.success($t('common.deleteSuccess'));
}

function handleUpdateMenuApi(index: number) {
  btnEditMap.value[index] = true;
}

async function handleSaveMenuApi(row: Api.System.ApiPermission) {
  if (!isNotNull(row.code)) {
    window.$message?.error('编码不能为空');
    return;
  }
  if (row.id) {
    const { error } = await fetchUpdateResourceApi(row);
    if (error) {
      return;
    }
  } else {
    row.resourceId = currentMenu.value?.id;
    const { error } = await fetchCreateResourceApi(row);
    if (error) {
      return;
    }
  }
  btnEditMap.value = {};
  await getApiList();
  window.$message?.success($t('common.saveSuccess'));
}

const btnColumns: DataTableColumns<Api.System.ApiPermission> = [
  {
    key: 'index',
    width: 36,
    align: 'center',
    title() {
      return (
        <NButton circle type="primary" size="small" onClick={() => addApiBtn()}>
          {{
            icon: () => (
              <NIcon>
                <SvgIcon icon="ic-round-plus" />
              </NIcon>
            )
          }}
        </NButton>
      );
    },
    render(_, index) {
      return index + 1;
    }
  },
  {
    title: '编码',
    key: 'code',
    width: 120,
    render(row, index) {
      function onUpdateValue(value: string) {
        btnData.value[index].code = value;
      }
      return (
        <>
          {btnEditMap.value[index] ? (
            <NInput value={row.code} onUpdate:value={onUpdateValue} />
          ) : (
            <span>{row.code}</span>
          )}
        </>
      );
    }
  },
  {
    title: '备注',
    key: 'remark',
    width: 300,
    render(row, index) {
      function onUpdateValue(value: string) {
        btnData.value[index].remark = value;
      }
      return (
        <>
          {btnEditMap.value[index] ? (
            <NInput value={row.remark} onUpdate:value={onUpdateValue} />
          ) : (
            <span>{row.remark}</span>
          )}
        </>
      );
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 80,
    align: 'center',
    render(row, index) {
      return (
        <>
          {btnEditMap.value[index] ? (
            <ButtonIcon
              type="success"
              text
              icon="ep:check"
              tooltipContent="完成"
              onClick={() => handleSaveMenuApi(row)}
            />
          ) : (
            <>
              <ButtonIcon
                type="primary"
                text
                icon="ep:edit"
                tooltipContent="修改"
                onClick={() => handleUpdateMenuApi(index)}
              />
              <ButtonIcon
                class="ml-16px"
                type="error"
                text
                icon="ep:delete"
                tooltipContent="删除"
                onClick={() => handleDeleteMenuApi(row.id!)}
              />
            </>
          )}
        </>
      );
    }
  }
];
</script>

<template>
  <TableSiderLayout default-expanded>
    <template #header>菜单列表</template>
    <template #header-extra>
      <ButtonIcon
        size="small"
        icon="ic-round-plus"
        class="h-28px text-icon"
        tooltip-content="新增菜单"
        @click.stop="handleAddMenu('0')"
      />
      <ButtonIcon
        size="small"
        icon="ic-round-refresh"
        class="h-28px text-icon"
        tooltip-content="刷新"
        @click.stop="reset"
      />
    </template>
    <template #sider>
      <div class="flex gap-6px">
        <NInput v-model:value="name" size="small" placeholder="请输入菜单名称" />
      </div>
      <NSpin :show="loading" class="infinite-scroll">
        <NTree
          ref="menuTreeRef"
          v-model:checked-keys="checkedKeys"
          v-model:expanded-keys="expandedKeys"
          show-line
          :cancelable="false"
          :data="treeData as []"
          :show-irrelevant-nodes="false"
          :pattern="name"
          block-line
          class="menu-tree h-full min-h-200px py-3"
          key-field="id"
          label-field="title"
          virtual-scroll
          checkable
          :render-prefix="renderPrefix"
          :render-suffix="renderSuffix"
          @update:selected-keys="(_: Array<string & number>, option: Array<TreeOption | null>) => handleClickTree(option)"
        >
          <template #empty>
            <NEmpty description="暂无菜单" class="h-full min-h-200px justify-center" />
          </template>
        </NTree>
      </NSpin>
    </template>
    <div class="h-full flex-col-stretch gap-16px">
      <template v-if="currentMenu">
        <NCard
          title="菜单详情"
          :bordered="false"
          size="small"
          class="max-h-50% card-wrapper"
          content-class="overflow-auto mb-12px"
        >
          <template #header-extra>
            <NSpace>
              <NButton
                v-if="currentMenu.type === 0"
                size="small"
                ghost
                type="primary"
                @click="handleAddMenu(currentMenu.id)"
              >
                <template #icon>
                  <icon-ic-round-plus />
                </template>
                新增子菜单
              </NButton>
              <NButton size="small" ghost type="primary" @click="handleUpdateMenu">
                <template #icon>
                  <icon-ic-round-edit />
                </template>
                编辑
              </NButton>
              <NPopconfirm @positive-click="() => handleDeleteMenu()">
                <template #trigger>
                  <NButton size="small" ghost type="error">
                    <template #icon>
                      <icon-ic-round-delete />
                    </template>
                    {{ $t('common.delete') }}
                  </NButton>
                </template>
                {{ $t('common.confirmDelete') }}
              </NPopconfirm>
            </NSpace>
          </template>
          <NDescriptions
            label-placement="left"
            size="small"
            bordered
            :column="appStore.isMobile ? 1 : 2"
            label-class="w-20% min-w-88px"
          >
            <NDescriptionsItem label="菜单类型">
              <NTag size="small" type="primary">{{ menuTypeRecord[currentMenu.type] }}</NTag>
            </NDescriptionsItem>
            <NDescriptionsItem label="菜单状态">
              <StatusTag size="small" :value="currentMenu.status" />
            </NDescriptionsItem>
            <NDescriptionsItem label="菜单名称">{{ currentMenu.title }}</NDescriptionsItem>
            <NDescriptionsItem label="组件路径">{{ currentMenu.component }}</NDescriptionsItem>
            <NDescriptionsItem label="路由名称">{{ currentMenu.alias }}</NDescriptionsItem>
            <NDescriptionsItem label="路由路径">{{ currentMenu.path }}</NDescriptionsItem>
            <NDescriptionsItem label="路由参数">{{ currentMenu.query }}</NDescriptionsItem>
            <NDescriptionsItem label="菜单排序">{{ currentMenu.sort }}</NDescriptionsItem>
            <NDescriptionsItem label="是否隐藏">
              <BooleanTag size="small" :value="currentMenu.hidden" />
            </NDescriptionsItem>
            <NDescriptionsItem label="是否缓存">
              <BooleanTag size="small" :value="currentMenu.keepAlive!" />
            </NDescriptionsItem>
          </NDescriptions>
        </NCard>

        <NCard
          title="接口权限列表"
          :bordered="false"
          size="small"
          class="h-full overflow-auto card-wrapper"
          content-class="overflow-auto mb-12px"
        >
          <template #header-extra>
            <NButton size="small" ghost type="primary" @click="getApiList">
              <template #icon>
                <icon-ic-round-refresh />
              </template>
              刷新
            </NButton>
          </template>
          <NDataTable class="h-full" max-height="100%" :loading="btnLoading" :columns="btnColumns" :data="btnData" />
        </NCard>
      </template>
      <NCard v-else :bordered="false" size="small" class="h-full card-wrapper">
        <NEmpty class="h-full flex-center" size="large" />
      </NCard>
    </div>
    <MenuOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="currentMenu"
      :tree-data="treeData"
      :pid="createPid"
      @submitted="handleSubmitted"
    />
  </TableSiderLayout>
</template>

<style scoped lang="scss">
:deep(.infinite-scroll) {
  height: calc(100vh - 224px - var(--calc-footer-height, 0px)) !important;
  max-height: calc(100vh - 224px - var(--calc-footer-height, 0px)) !important;
}

@media screen and (max-width: 1024px) {
  :deep(.infinite-scroll) {
    height: calc(100vh - 227px - var(--calc-footer-height, 0px)) !important;
    max-height: calc(100vh - 227px - var(--calc-footer-height, 0px)) !important;
  }
}

:deep(.n-spin-content) {
  height: 100%;
}

:deep(.n-tree-node-checkbox) {
  display: none;
}

:deep(.n-data-table-base-table) {
  height: 100% !important;
}

.menu-tree {
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

:deep(.n-data-table-empty) {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
