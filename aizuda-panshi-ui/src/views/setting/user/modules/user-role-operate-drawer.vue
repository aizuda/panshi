<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { useLoading } from '@sa/hooks';
import { fetchGetAllRoles, fetchUserAssignRole } from '@/service/api';
import { useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'UserDeptOperateDrawer'
});

interface Props {
  checkedRowKeys: string[];
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { loading: roleLoading, startLoading: startRoleLoading, endLoading: endRoleLoading } = useLoading();
const { formRef, validate, restoreValidation } = useNaiveForm();

type Model = {
  roleIds: string[];
};

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    roleIds: []
  };
}

type RuleKey = Extract<keyof Model, 'roleIds'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  roleIds: { required: true, message: '请选择角色', validator: (_, value) => value.length }
};

/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  startRoleLoading();
  const { error, data } = await fetchGetAllRoles();

  if (!error) {
    roleOptions.value = data.map(item => ({
      label: item.name,
      value: item.id
    }));
  }
  endRoleLoading();
}

async function handleInitModel() {
  Object.assign(model, createDefaultModel());
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  // request
  const { roleIds } = model;
  const { error } = await fetchUserAssignRole(props.checkedRowKeys, roleIds);
  if (error) return;

  window.$message?.success('角色分配成功');

  closeDrawer();
  emit('submitted');
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
    getRoleOptions();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent title="分配角色" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="所属角色" path="roleIds">
          <NSelect
            v-model:value="model.roleIds"
            :loading="roleLoading"
            multiple
            clearable
            :options="roleOptions"
            placeholder="请选择角色"
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
