<script setup lang="tsx">
import { computed, reactive, ref, watch } from 'vue';
import { fetchCreateGenTemplate, fetchUpdateGenTemplate } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'TemplateOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.Gen.Template | null;
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
    add: '新增模板',
    edit: '编辑模板'
  };
  return titles[props.operateType];
});

type Model = Api.Gen.TemplateFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    tplName: '',
    tplContent: '',
    outFile: '',
    remark: ''
  };
}

type RuleKey = Exclude<keyof Model, 'id' | 'remark'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  tplName: defaultRequiredRule,
  tplContent: defaultRequiredRule,
  outFile: defaultRequiredRule
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

  const { id, tplName, tplContent, outFile, remark } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateGenTemplate({ tplName, tplContent, outFile, remark });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateGenTemplate({ id, tplName, tplContent, outFile, remark });
    if (error) {
      return;
    }
    window.$message?.success($t('common.updateSuccess'));
  }

  // closeDrawer();
  emit('submitted');
}

const monacoEditorRef = ref();

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
    monacoEditorRef.value?.setValue(model.tplContent);
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" width="100%">
    <NDrawerContent :title="drawerTitle" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NGrid responsive="screen" item-responsive :x-gap="16">
          <NFormItemGi :span="18" :show-label="false" path="type">
            <MonacoEditor
              ref="monacoEditorRef"
              v-model="model.tplContent"
              language="verilog"
              height="calc(100vh - 186px)"
            />
          </NFormItemGi>
          <NGi :span="6">
            <NFormItem label="模板名称" path="tplName">
              <NInput v-model:value="model.tplName" placeholder="请输入模板名称" />
            </NFormItem>
            <NFormItem label="输出文件" path="outFile">
              <NInput v-model:value="model.outFile" placeholder="请输入输出文件" />
            </NFormItem>
            <NFormItem label="模板描述" path="remark">
              <NInput v-model:value="model.remark" placeholder="请输入模板描述" />
            </NFormItem>
          </NGi>
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
