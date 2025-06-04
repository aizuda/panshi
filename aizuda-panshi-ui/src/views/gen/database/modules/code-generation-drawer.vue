<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { useBoolean } from '@sa/hooks';
import { fetchGetAllGenTemplateList, fetchGetGenDatabaseSelectOption } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useDownload } from '@/hooks/business/download';
import CodeGenPreviewDrawer from './code-gen-preview-drawer.vue';

defineOptions({
  name: 'CodeGenerationDrawer'
});

interface Props {
  /** the edit row data */
  rowData?: Api.Gen.Database | null;
}

const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { bool: drawerVisible, setTrue: openDrawerVisible } = useBoolean();
const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();

type Model = Api.Gen.GenCode;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    databaseId: null,
    author: '',
    module: '',
    tableName: '',
    templateIds: []
  };
}

type RuleKey = Exclude<keyof Model, 'author'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  databaseId: defaultRequiredRule,
  module: defaultRequiredRule,
  tableName: defaultRequiredRule,
  templateIds: { ...defaultRequiredRule, type: 'array' }
};

const databaseOptions = ref<CommonType.Option<string | undefined>[]>([]);

async function getDatabaseOptions() {
  const { data, error } = await fetchGetGenDatabaseSelectOption();
  if (!error) {
    databaseOptions.value = data.map(item => ({
      label: item.title,
      value: item.id
    }));
  }
}

const templateOptions = ref<CommonType.Option<string>[]>([]);

async function getTemplateOptions() {
  const { data, error } = await fetchGetAllGenTemplateList();
  if (!error) {
    templateOptions.value = data.map(item => ({
      label: item.tplName,
      value: item.id
    }));
  }
}

function handleInitModel() {
  Object.assign(model, createDefaultModel());
  model.databaseId = props.rowData?.id || undefined;
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  useDownload().zip('/gen/table/download', `${model.module}-genCode.zip`, {
    ...model,
    databaseId: model.databaseId === 'sa-local' ? undefined : model.databaseId
  });
}

const previewData = ref<Api.Gen.GenCode>();

async function handlePreview() {
  await validate();
  previewData.value = {
    ...model,
    databaseId: model.databaseId === 'sa-local' ? undefined : model.databaseId
  };
  openDrawerVisible();
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
    getDatabaseOptions();
    getTemplateOptions();
  }
});
</script>

<template>
  <div>
    <NDrawer v-model:show="visible" display-directive="if" :width="800" class="max-w-90%">
      <NDrawerContent title="代码生成" :native-scrollbar="false" closable>
        <NForm ref="formRef" :model="model" :rules="rules">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi :span="24" label="数据源" path="databaseId">
              <NSelect
                v-model:value="model.databaseId"
                placeholder="请选择数据源"
                :options="databaseOptions"
                clearable
              />
            </NFormItemGi>
            <NFormItemGi :span="24" label="作者" path="author">
              <NInput v-model:value="model.author" placeholder="请输入作者" />
            </NFormItemGi>
            <NFormItemGi :span="24" label="项目模块" path="module">
              <NInput v-model:value="model.module" placeholder="请输入项目模块" />
            </NFormItemGi>
            <NFormItemGi :span="24" label="表名" path="tableName">
              <NInput v-model:value="model.tableName" placeholder="请输入表名" />
            </NFormItemGi>
            <NFormItemGi :span="24" label="模板" path="templateIds">
              <NCheckboxGroup v-model:value="model.templateIds">
                <NSpace item-style="display: flex;">
                  <NCheckbox
                    v-for="(option, index) in templateOptions"
                    :key="index"
                    :value="option.value"
                    :label="option.label"
                  />
                </NSpace>
              </NCheckboxGroup>
            </NFormItemGi>
          </NGrid>
        </NForm>
        <template #footer>
          <NSpace :size="16">
            <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
            <NButton type="primary" @click="handlePreview">预览代码</NButton>
            <NButton type="primary" @click="handleSubmit">生成代码</NButton>
          </NSpace>
        </template>
      </NDrawerContent>
    </NDrawer>
    <CodeGenPreviewDrawer v-model:visible="drawerVisible" :data="previewData" @submitted="handleSubmit" />
  </div>
</template>

<style scoped></style>
