<script setup lang="tsx">
import { computed, reactive, watch } from 'vue';
import { enableStatusOptions } from '@/constants/business';
import { fetchCreateApp, fetchUpdateApp } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'AppOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.App | null;
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
    add: '新增应用',
    edit: '编辑应用'
  };
  return titles[props.operateType];
});

type Model = Api.System.AppFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    identification: '',
    name: '',
    secretKey: '',
    status: 1,
    expire: null,
    sort: 1
  };
}

type RuleKey = Extract<keyof Model, 'identification' | 'name' | 'sort' | 'secretKey' | 'status' | 'expire'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  identification: defaultRequiredRule,
  name: defaultRequiredRule,
  sort: { ...defaultRequiredRule, type: 'number' },
  status: { ...defaultRequiredRule, type: 'number' },
  secretKey: defaultRequiredRule,
  expire: defaultRequiredRule
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

  const { id, identification, name, status, sort, secretKey, expire } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateApp({ identification, name, status, sort, secretKey, expire });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateApp({ id, identification, name, status, sort, secretKey, expire });
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
          <NFormItemGi :span="24" label="应用标识" path="identification">
            <NInput v-model:value="model.identification" placeholder="请输入应用标识" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="应用名称" path="name">
            <NInput v-model:value="model.name" placeholder="请输入应用名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="应用密钥" path="secretKey">
            <NInput v-model:value="model.secretKey" placeholder="请输入应用密钥" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="授权到期日期" path="expire">
            <NDatePicker
              v-model:formatted-value="model.expire"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetime"
              clearable
              placeholder="请选择授权到期日期"
            />
          </NFormItemGi>
          <NFormItemGi :span="24" label="状态" path="status">
            <NRadioGroup v-model:value="model.status">
              <NSpace>
                <NRadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value" :label="item.label" />
              </NSpace>
            </NRadioGroup>
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
