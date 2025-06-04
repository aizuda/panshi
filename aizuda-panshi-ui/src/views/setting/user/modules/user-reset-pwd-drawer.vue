<script setup lang="ts">
import { computed, reactive, watch } from 'vue';
import { fetchUserResetPwd } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'UserResetPwdDrawer'
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

const { formRef, validate, restoreValidation } = useNaiveForm();

type Model = {
  password: string;
  confirmPassword: string;
};

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    password: '',
    confirmPassword: ''
  };
}

type RuleRecord = Partial<Record<keyof Model, App.Global.FormRule[]>>;

const rules = computed<RuleRecord>(() => {
  const { patternRules, createConfirmPwdRule } = useFormRules();

  return {
    password: [{ required: true, ...patternRules.pwd }],
    confirmPassword: createConfirmPwdRule(model.password)
  };
});

async function handleInitModel() {
  Object.assign(model, createDefaultModel());
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  // request
  const { password } = model;
  const { error } = await fetchUserResetPwd(props.checkedRowKeys, password);
  if (error) return;

  window.$message?.success('密码重置成功');

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
    <NDrawerContent title="重置密码" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem path="password">
          <NInput
            v-model:value="model.password"
            type="password"
            show-password-on="click"
            :placeholder="$t('page.login.common.passwordPlaceholder')"
          />
        </NFormItem>
        <NFormItem path="confirmPassword">
          <NInput
            v-model:value="model.confirmPassword"
            type="password"
            show-password-on="click"
            :placeholder="$t('page.login.common.confirmPasswordPlaceholder')"
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
