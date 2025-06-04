<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { useLoading } from '@sa/hooks';
import { random } from '@sa/utils';
import { useAuthStore } from '@/store/modules/auth';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useRouterPush } from '@/hooks/common/router';
import { isNotNull } from '@/utils/common';
import { getServiceBaseURL } from '@/utils/service';
import { $t } from '@/locales';

defineOptions({
  name: 'PwdLogin'
});

const { MODE, VITE_LOGIN_CODE } = import.meta.env;
const isProd = MODE === 'prod';
const isHttpProxy = import.meta.env.DEV && import.meta.env.VITE_HTTP_PROXY === 'Y';
const { baseURL } = getServiceBaseURL(import.meta.env, isHttpProxy);
const authStore = useAuthStore();
const { toggleLoginModule } = useRouterPush();
const { formRef, validate } = useNaiveForm();

const code = ref<string>('');
const codeUrl = ref<string>('');
const ticket = ref<number>(random());
const { loading: codeLoading, startLoading: startCodeLoading, endLoading: endCodeLoading } = useLoading();

const model: Api.Auth.LoginBody = reactive({
  username: isProd ? '' : 'admin',
  password: isProd ? '' : 'Aa!123',
  code: '',
  autologin: false
});

const rules = computed<Record<keyof Api.Auth.LoginBody, App.Global.FormRule[]>>(() => {
  // inside computed to make locale reactive, if not apply i18n, you can define it without computed
  const { formRules } = useFormRules();

  return {
    username: formRules.username,
    password: formRules.pwd,
    code: [{ required: true, validator: () => validatorCode() }],
    autologin: []
  };
});

async function handleSubmit() {
  await validate();
  await authStore.login(model);
}

async function getCaptcha() {
  if (VITE_LOGIN_CODE === 'N') {
    return;
  }
  startCodeLoading();
  try {
    ticket.value = random();
    const response = await fetch(`${baseURL}/v1/captcha/image?ticket=${ticket.value}`);
    if (response.status !== 200) throw new Error('验证码获取失败');
    const blob = await response.blob();
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    reader.onloadend = () => (codeUrl.value = `data:image/png;base64,${(reader.result as string)?.split(',')[1]}`);
  } catch {
    window.$message?.error('验证码获取失败');
  } finally {
    endCodeLoading();
  }
}

function validatorCode() {
  return new Promise<void>((resolve, reject) => {
    if (VITE_LOGIN_CODE === 'N') {
      if (!isNotNull(model.code)) {
        reject(new Error('请输入验证码'));
        return;
      }
      resolve();
      return;
    }
    if (!isNotNull(code.value)) {
      reject(new Error('请输入验证码'));
      return;
    }
    const formData = new FormData();
    formData.append('ticket', String(ticket.value));
    formData.append('code', code.value);
    fetch(`${baseURL}/v1/captcha/verification`, { method: 'post', body: formData })
      .then(response => {
        if (response.status !== 200) throw new Error('Internal Server Error');
        return response.json();
      })
      .then(response => {
        if (!response.data) throw new Error('验证码不正确');
        resolve();
      })
      .catch(e => {
        window.$message?.error(e.message);
        getCaptcha();
        reject(e);
      });
  });
}

getCaptcha();

function handleGiteeLogin() {
  window.location.href = `https://star.aizuda.com/v1/auth-check?platform=nui&t=${Date.now()}`;
}

onMounted(async () => {
  const route = useRoute();
  const token = route.query.token;
  if (!token) return;
  await authStore.loginByAuthToken(String(token));
});
</script>

<template>
  <NForm ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
    <NFormItem path="username">
      <NInput v-model:value="model.username" size="large" :placeholder="$t('page.login.common.usernamePlaceholder')">
        <template #prefix>
          <icon-ep:user class="mr-6px color-#c2c2c2ff" />
        </template>
      </NInput>
    </NFormItem>
    <NFormItem path="password">
      <NInput
        v-model:value="model.password"
        type="password"
        size="large"
        show-password-on="click"
        :placeholder="$t('page.login.common.passwordPlaceholder')"
      >
        <template #prefix>
          <icon-ep:lock class="mr-6px color-#c2c2c2ff" />
        </template>
      </NInput>
    </NFormItem>
    <NFormItem v-if="VITE_LOGIN_CODE === 'Y'" path="code">
      <div class="w-full flex-y-center gap-16px">
        <NInput v-model:value="code" :placeholder="$t('page.login.common.codePlaceholder')">
          <template #prefix>
            <SvgIcon local-icon="valid-code" class="mr-6px color-#c2c2c2ff" />
          </template>
        </NInput>
        <NSpin :show="codeLoading" :size="28" class="h-42px">
          <NButton :focusable="false" class="code h-42px w-116px" @click="getCaptcha">
            <img v-if="codeUrl" :src="codeUrl" />
            <NEmpty v-else :show-icon="false" description="暂无验证码" />
          </NButton>
        </NSpin>
      </div>
    </NFormItem>
    <NFormItem v-else path="code">
      <div class="w-full flex-y-center gap-16px">
        <NInput v-model:value="model.code" :placeholder="$t('page.login.common.codePlaceholder')">
          <template #prefix>
            <SvgIcon local-icon="valid-code" class="mr-6px color-#c2c2c2ff" />
          </template>
        </NInput>
        <NPopover trigger="hover" placement="bottom">
          <template #trigger>
            <NButton class="h-42px" size="large">{{ $t('page.login.codeLogin.getCode') }}</NButton>
          </template>
          <img src="@/assets/imgs/qrcode.png" width="200" />
        </NPopover>
      </div>
    </NFormItem>
    <NSpace vertical :size="20">
      <div class="mb-2px flex-y-center justify-between">
        <NCheckbox v-model:checked="model.autologin">
          <span class="color-inherit font-400">{{ $t('page.login.pwdLogin.rememberMe') }}</span>
        </NCheckbox>
        <NButton class="text-14px" text type="primary" @click="toggleLoginModule('reset-pwd')">
          {{ $t('page.login.pwdLogin.forgetPassword') }}
        </NButton>
      </div>
      <NButton type="default" size="large" class="h-42px" block :loading="authStore.loginLoading" @click="handleSubmit">
        {{ $t('common.login') }}
      </NButton>
      <NButton
        type="primary"
        size="large"
        class="h-42px"
        block
        :loading="authStore.loginLoading"
        @click="handleGiteeLogin"
      >
        <template #icon>
          <icon-simple-icons:gitee />
        </template>
        使用 Gitee 账号 Star 免密登录
      </NButton>
    </NSpace>
  </NForm>
</template>

<style scoped lang="scss">
.code {
  &.n-button {
    --n-padding: 0 8px !important;
  }

  img {
    height: 42px;
  }
}

.n-input {
  --n-height: 42px !important;
  --n-font-size: 16px !important;
}
</style>
