<script setup lang="ts">
import { computed, reactive } from 'vue';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useCaptcha } from '@/hooks/business/captcha';
import { $t } from '@/locales';

defineOptions({
  name: 'Register'
});

const { formRef, validate } = useNaiveForm();
const { label, isCounting, loading, getCaptcha } = useCaptcha();

interface FormModel {
  phone: string;
  code: string;
  password: string;
  confirmPassword: string;
}

const model: FormModel = reactive({
  phone: '',
  code: '',
  password: '',
  confirmPassword: ''
});

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  const { formRules, createConfirmPwdRule } = useFormRules();

  return {
    phone: formRules.phone,
    code: formRules.code,
    password: formRules.pwd,
    confirmPassword: createConfirmPwdRule(model.password)
  };
});

async function handleSubmit() {
  await validate();
  // request to register
  window.$message?.success($t('page.login.common.validateSuccess'));
}
</script>

<template>
  <NForm ref="formRef" :model="model" :rules="rules" size="large" :show-label="false" @keyup.enter="handleSubmit">
    <NFormItem path="phone">
      <NInput v-model:value="model.phone" :placeholder="$t('page.login.common.phonePlaceholder')" />
    </NFormItem>
    <NFormItem path="code">
      <div class="w-full flex-y-center gap-16px">
        <NInput v-model:value="model.code" :placeholder="$t('page.login.common.codePlaceholder')" />
        <NButton size="large" :disabled="isCounting" :loading="loading" @click="getCaptcha(model.phone)">
          {{ label }}
        </NButton>
      </div>
    </NFormItem>
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
    <NSpace vertical :size="18" class="w-full">
      <div class="mb-2px flex-y-center justify-between">
        <NCheckbox>
          <span class="color-inherit font-400">
            <span class="color-#aaa">{{ $t('page.login.register.agreement') }}</span>
            <span class="p-x-3px color-#333">{{ $t('page.login.register.protocol') }}</span>
            <span class="color-#aaa">{{ $t('page.login.register.and') }}</span>
            <span class="p-x-3px color-#333">{{ $t('page.login.register.policy') }}</span>
          </span>
        </NCheckbox>
      </div>
      <NButton type="primary" size="large" block @click="handleSubmit">
        {{ $t('page.login.register.title') }}
      </NButton>
    </NSpace>
  </NForm>
</template>

<style scoped></style>
