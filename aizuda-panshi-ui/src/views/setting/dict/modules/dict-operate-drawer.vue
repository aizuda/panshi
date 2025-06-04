<script setup lang="ts">
import { computed, reactive, watch } from 'vue';
import { enableStatusOptions } from '@/constants/business';
import { fetchCreateDict, fetchUpdateDict } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'DictOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Dict | null;
  treeData?: Api.System.DictParentList;
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

const title = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: props.pid === '0' ? '新增字典分类' : '新增字典',
    edit: props.pid === '0' ? '编辑字典分类' : '编辑字典'
  };
  return titles[props.operateType];
});

type Model = Api.System.DictFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    pid: props.pid || (props.treeData?.length ? props.treeData![0].id : undefined),
    name: '',
    code: '',
    status: 1,
    sort: 1,
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'pid' | 'name' | 'code' | 'sort' | 'status'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  pid: defaultRequiredRule,
  name: defaultRequiredRule,
  code: defaultRequiredRule,
  sort: { ...defaultRequiredRule, type: 'number' },
  status: { ...defaultRequiredRule, type: 'number' }
};

async function handleInitModel() {
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

  const { id, pid, name, code, sort, status, remark } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateDict({ pid, name, code, sort, status, remark });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateDict({ id, pid, name, code, sort, status, remark });
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
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem v-if="model.pid !== '0'" :span="24" label="字典分类" path="pid">
          <NTreeSelect
            v-model:value="model.pid"
            :options="treeData as []"
            label-field="name"
            key-field="id"
            placeholder="请选择字典分类"
          />
        </NFormItem>
        <NFormItem label="字典名称" path="name">
          <NInput v-model:value="model.name" placeholder="请输入字典名称" />
        </NFormItem>
        <NFormItem label="字典编码" path="code">
          <NInput v-model:value="model.code" placeholder="请输入字典编码" />
        </NFormItem>
        <NFormItem v-if="model.pid !== '0'" label="字典内容" path="content">
          <NInput v-model:value="model.content" placeholder="请输入字典内容" />
        </NFormItem>
        <NFormItem label="排序" path="sort">
          <NInputNumber v-model:value="model.sort" placeholder="请输入排序" />
        </NFormItem>
        <NFormItem label="状态" path="status">
          <NRadioGroup v-model:value="model.status">
            <NRadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value" :label="item.label" />
          </NRadioGroup>
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

<style scoped></style>
