<script setup lang="ts">
import { reactive, watch } from 'vue';
import { fetchUserAssignDept } from '@/service/api';
import { useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'UserDeptOperateDrawer'
});

interface Props {
  checkedRowKeys: string[];
  treeData?: Api.System.DepartmentList;
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

type Model = {
  departmentIds: string[];
};

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    departmentIds: []
  };
}

type RuleKey = Extract<keyof Model, 'departmentIds'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  departmentIds: { required: true, message: '请选择部门', validator: (_, value) => value.length }
};

async function handleInitModel() {
  Object.assign(model, createDefaultModel());
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  // request
  const { departmentIds } = model;
  const { error } = await fetchUserAssignDept(props.checkedRowKeys, departmentIds);
  if (error) return;

  window.$message?.success('部门分配成功');

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
    <NDrawerContent title="分配部门" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem :span="24" label="所属部门" path="departmentIds">
          <NTreeSelect
            v-model:value="model.departmentIds"
            multiple
            clearable
            :options="treeData as []"
            label-field="name"
            key-field="id"
            :default-expanded-keys="treeData?.length ? [treeData[0].id] : []"
            placeholder="请选择上级部门"
          />
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
