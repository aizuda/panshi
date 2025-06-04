<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import type { TreeSelectInst } from 'naive-ui';
import { useBoolean, useLoading } from '@sa/hooks';
import { enableStatusOptions } from '@/constants/business';
import { fetchGetMenuTreeList, fetchGetRoleResourceIds, fetchRoleUpdateSetResource } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'RoleOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Role | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const resourceTreeRef = ref<TreeSelectInst | null>(null);
const { loading, startLoading, endLoading } = useLoading();
const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();
const { bool: expandAll } = useBoolean();
const { bool: cascade } = useBoolean(true);
const { bool: checkAll } = useBoolean();

const title = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增角色',
    edit: '编辑角色'
  };
  return titles[props.operateType];
});

type Model = Api.System.RoleFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    name: '',
    alias: '',
    sort: 1,
    status: 1,
    resourceIds: []
  };
}

type RuleKey = Extract<keyof Model, 'name' | 'alias' | 'sort' | 'status'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  name: defaultRequiredRule,
  alias: defaultRequiredRule,
  sort: { ...defaultRequiredRule, type: 'number' },
  status: { ...defaultRequiredRule, type: 'number' }
};

/** the enabled menu options */
const menuOptions = ref<Api.System.MenuTreeList>([]);

async function getMenuOptions() {
  menuOptions.value = [];
  startLoading();
  const { error, data } = await fetchGetMenuTreeList();
  if (!error) {
    menuOptions.value = data;
  }
  endLoading();
}

async function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
    const { data, error } = await fetchGetRoleResourceIds(model.id!);
    if (error) return;
    model.resourceIds = data;
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const { id, name, alias, status, sort } = model;

  const resourceIds = [...model.resourceIds!];
  const indeterminateData = resourceTreeRef.value?.getIndeterminateData();
  if (cascade.value) {
    const parentIds: string[] = indeterminateData?.keys.filter(
      item => !resourceIds?.includes(String(item))
    ) as string[];
    resourceIds?.push(...parentIds);
  }

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchRoleUpdateSetResource({ name, alias, status, sort, resourceIds });
    if (error) return;
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchRoleUpdateSetResource({ id, name, alias, status, sort, resourceIds });
    if (error) return;
    window.$message?.success($t('common.updateSuccess'));
  }

  closeDrawer();
  emit('submitted');
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
    getMenuOptions();
  }
});

function getAllMenuIds(menu: Api.System.MenuTreeList) {
  const resourceIds: string[] = [];
  menu.forEach(item => {
    resourceIds.push(item.id);
    if (item.children) {
      resourceIds.push(...getAllMenuIds(item.children));
    }
  });
  return resourceIds;
}

function handleCheckedTreeNodeAll(checked: boolean) {
  if (checked) {
    model.resourceIds = getAllMenuIds(menuOptions.value);
    return;
  }
  model.resourceIds = [];
}
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="角色名称" path="name" class="pr-24px">
          <NInput v-model:value="model.name" placeholder="请输入角色名称" />
        </NFormItem>
        <NFormItem label="角色编码" path="alias" class="pr-24px">
          <NInput v-model:value="model.alias" placeholder="请输入角色编码" />
        </NFormItem>
        <NFormItem label="排序" path="sort">
          <NInputNumber v-model:value="model.sort" placeholder="请输入排序" />
        </NFormItem>
        <NFormItem label="角色状态" path="status" class="pr-24px">
          <NRadioGroup v-model:value="model.status">
            <NRadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value" :label="item.label" />
          </NRadioGroup>
        </NFormItem>
        <NFormItem label="菜单权限" path="resourceIds" class="pr-24px">
          <div class="w-full flex-col gap-12px">
            <div class="w-full flex-center">
              <NCheckbox v-model:checked="expandAll" :checked-value="true" :unchecked-value="false">
                展开/折叠
              </NCheckbox>
              <NCheckbox
                v-model:checked="checkAll"
                :checked-value="true"
                :unchecked-value="false"
                @update:checked="handleCheckedTreeNodeAll"
              >
                全选/反选
              </NCheckbox>
              <NCheckbox v-model:checked="cascade" :checked-value="true" :unchecked-value="false">父子联动</NCheckbox>
            </div>
            <NSpin class="resource h-full w-full py-6px pl-3px" content-class="h-full" :show="loading">
              <NTree
                ref="resourceTreeRef"
                v-model:checked-keys="model.resourceIds!"
                multiple
                checkable
                key-field="id"
                label-field="title"
                :data="menuOptions as []"
                :cascade="cascade"
                :loading="loading"
                virtual-scroll
                check-strategy="all"
                :default-expand-all="expandAll"
              />
            </NSpin>
          </div>
        </NFormItem>
        <NFormItem label="备注" path="remark">
          <NInput v-model:value="model.remark" type="textarea" placeholder="请输入备注" />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped lang="scss">
.resource {
  border-radius: 6px;
  border: 1px solid rgb(224, 224, 230);

  .n-tree {
    min-height: 200px;
    max-height: 300px;
    width: 100%;
    height: 100%;

    :deep(.n-tree__empty) {
      min-height: 200px;
      justify-content: center;
    }
  }

  .n-empty {
    justify-content: center;
  }
}
</style>
