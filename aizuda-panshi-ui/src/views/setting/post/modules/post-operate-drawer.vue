<script setup lang="tsx">
import { computed, reactive, watch } from 'vue';
import { enableStatusOptions } from '@/constants/business';
import { fetchCreatePost, fetchUpdatePost } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'PostOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Post | null;
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
    add: '新增岗位',
    edit: '编辑岗位'
  };
  return titles[props.operateType];
});

type Model = Api.System.PostFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    name: '',
    code: '',
    sort: 1,
    status: 1,
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'name' | 'code' | 'sort' | 'status'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  name: defaultRequiredRule,
  code: defaultRequiredRule,
  status: { ...defaultRequiredRule, type: 'number' },
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

  const { id, name, code, status, sort, remark } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreatePost({
      name,
      code,
      status,
      sort,
      remark
    });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdatePost({
      id,
      name,
      code,
      status,
      sort,
      remark
    });
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
          <NFormItemGi :span="24" label="岗位名称" path="name">
            <NInput v-model:value="model.name" placeholder="请输入岗位名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="岗位编码" path="code">
            <NInput v-model:value="model.code" placeholder="请输入岗位编码" />
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
          <NFormItemGi :span="24" label="备注" path="remark">
            <NInput v-model:value="model.remark" type="textarea" placeholder="请输入备注" />
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
