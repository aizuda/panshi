<script setup lang="tsx">
import { computed, reactive, watch } from 'vue';
import { fetchCreateConfig, fetchUpdateConfig } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'ConfigOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Config | null;
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
    add: '新增参数',
    edit: '编辑参数'
  };
  return titles[props.operateType];
});

type Model = Api.System.ConfigFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    title: '',
    keyword: '',
    sort: 1,
    content: '',
    category: ''
  };
}

type RuleKey = Extract<keyof Model, 'title' | 'keyword' | 'sort' | 'content' | 'category'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  title: defaultRequiredRule,
  keyword: defaultRequiredRule,
  sort: { ...defaultRequiredRule, type: 'number' },
  content: defaultRequiredRule,
  category: defaultRequiredRule
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

  const { id, title, keyword, content, sort, category } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateConfig({ title, keyword, content, sort, category });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateConfig({ id, title, keyword, content, sort, category });
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
          <NFormItemGi :span="24" label="参数分类" path="category">
            <NInput v-model:value="model.category" placeholder="请输入参数分类" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="参数名称" path="title">
            <NInput v-model:value="model.title" placeholder="请输入岗位名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="参数键值" path="keyword">
            <NInput v-model:value="model.keyword" placeholder="请输入参数键值" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="参数内容" path="content">
            <NInput v-model:value="model.content" placeholder="请输入参数内容" />
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
