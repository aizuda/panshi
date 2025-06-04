<script setup lang="tsx">
import { computed, reactive, watch } from 'vue';
import { regionLevelOptions } from '@/constants/business';
import { fetchCreateRegion, fetchUpdateRegion } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateNumberOptions } from '@/utils/common';
import { $t } from '@/locales';

defineOptions({
  name: 'RegionOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Region | null;
  treeData?: Api.System.RegionList;
  pid?: string;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();

const drawerTitle = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增行政区域',
    edit: '编辑行政区域'
  };
  return titles[props.operateType];
});

const treeOptions = computed(() => {
  return [
    {
      id: '0',
      name: '根区域',
      children: props.treeData
    }
  ];
});

type Model = Api.System.RegionFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    pid: props.pid || (props.treeData?.length ? props.treeData![0].id : undefined),
    name: '',
    code: '',
    sort: 1,
    level: 0
  };
}

type RuleKey = Extract<keyof Model, 'pid' | 'name' | 'code' | 'sort' | 'level'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  pid: defaultRequiredRule,
  name: defaultRequiredRule,
  code: defaultRequiredRule,
  level: { ...defaultRequiredRule, type: 'number' },
  sort: { ...defaultRequiredRule, type: 'number' }
};

function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const { id, pid, name, code, level, sort } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateRegion({ pid, name, code, level, sort });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateRegion({ id, pid, name, code, level, sort });
    if (error) {
      return;
    }
    window.$message?.success($t('common.updateSuccess'));
  }

  closeDrawer();
  emit('submitted');
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="drawerTitle" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi :span="24" label="上级区域" path="pid">
            <NTreeSelect
              v-model:value="model.pid"
              :options="treeOptions as []"
              label-field="name"
              key-field="id"
              :default-expanded-keys="treeData?.length ? [treeData[0].id] : []"
              placeholder="请选择上级区域"
            />
          </NFormItemGi>
          <NFormItemGi :span="24" label="区域级别" path="level">
            <NSelect
              v-model:value="model.level"
              placeholder="请选择区域级别"
              :options="translateNumberOptions(regionLevelOptions)"
              clearable
            />
          </NFormItemGi>
          <NFormItemGi :span="24" label="区域名称" path="name">
            <NInput v-model:value="model.name" placeholder="请输入区域名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="区域编码" path="code">
            <NInput v-model:value="model.code" placeholder="请输入区域编码" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="排序" path="sort">
            <NInputNumber v-model:value="model.sort" placeholder="请输入排序" />
          </NFormItemGi>
        </NGrid>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.save') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
