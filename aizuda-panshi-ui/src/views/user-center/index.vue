<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { jsonClone } from '@sa/utils';
import { fetchUpdateUser } from '@/service/api';
import { useAppStore } from '@/store/modules/app';
import { useAuthStore } from '@/store/modules/auth';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useRouterPush } from '@/hooks/common/router';
import { $t } from '@/locales';

const tab = ref<'info' | 'password'>('info');
const appStore = useAppStore();
const { userInfo, initUserInfo } = useAuthStore();
const { routerPushByKey } = useRouterPush();

const { patternRules } = useFormRules();

type Model = Api.System.UserFormParams;

const model: Model = reactive(jsonClone(userInfo));

const { formRef, validate } = useNaiveForm();

interface FormModel {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

const pwdModel: FormModel = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

type RuleRecord = Partial<Record<keyof FormModel, App.Global.FormRule[]>>;

const rules = computed<RuleRecord>(() => {
  const { createConfirmPwdRule } = useFormRules();

  return {
    oldPassword: [{ required: true, ...patternRules.pwd }],
    newPassword: [{ required: true, ...patternRules.pwd }],
    confirmPassword: createConfirmPwdRule(pwdModel.newPassword)
  };
});

function closePage() {
  routerPushByKey(import.meta.env.VITE_ROUTE_HOME);
}

async function handleSubmit() {
  if (tab.value === 'password') {
    await validate();
    // request to reset password
    window.$message?.success('密码修改成功');
    return;
  }
  const { id, username, nickName, sex, phone, email } = model;
  const { error } = await fetchUpdateUser({
    id,
    username,
    nickName,
    sex,
    phone,
    email
  });
  if (error) return;
  initUserInfo();
  window.$message?.success($t('common.updateSuccess'));
}
</script>

<template>
  <div
    :class="appStore.isMobile ? 'flex-col-stretch' : 'flex items-stretch'"
    class="min-h-500px gap-16px overflow-hidden sm:h-full lt-sm:overflow-auto"
  >
    <NCard class="w-100% sm:w-360px">
      <template #header>
        <span>个人信息</span>
        <NDivider class="pt-12px" />
      </template>
      <div class="w-full flex-col items-center">
        <NAvatar class="mb-16px bg-primary font-size-36px" round :size="69">
          {{ userInfo.username?.substring(0, 1) }}
        </NAvatar>
        <strong class="text-16px">{{ userInfo.nickName || userInfo.username }}</strong>
        <span class="text-14px color-#777">{{ userInfo.username }}</span>
      </div>
      <NDivider class="pb-12px pt-16px" />
      <div class="w-full flex justify-between px-3px">
        <span class="flex-center gap-5px">
          <icon-material-symbols:person-outline class="text-18px" />
          真实姓名
        </span>
        <span>{{ userInfo.realName }}</span>
      </div>
      <NDivider class="py-12px" />
      <div class="w-full flex justify-between px-3px">
        <span class="flex-center gap-5px">
          <icon-streamline:travel-wayfinder-toilet-sign-man-woman-toilet-sign-restroom-bathroom-user-human-person
            class="text-16px"
          />
          用户性别
        </span>
        <span>{{ userInfo.sex }}</span>
      </div>
      <NDivider class="py-12px" />
      <div class="w-full flex justify-between px-3px">
        <span class="flex-center gap-5px">
          <icon-material-symbols:phone-android-outline class="text-16px" />
          手机号码
        </span>
        <span>{{ userInfo.phone }}</span>
      </div>
      <NDivider class="py-12px" />
      <div class="w-full flex justify-between px-3px">
        <span class="flex-center gap-5px">
          <icon-material-symbols:mail-outline class="text-16px" />
          用户邮箱
        </span>
        <span>{{ userInfo.email }}</span>
      </div>
      <NDivider class="py-12px" />
      <div class="w-full flex justify-between px-3px">
        <span class="flex-center gap-5px">
          <icon-material-symbols:calendar-month-outline class="text-16px" />
          创建时间
        </span>
        <span>{{ userInfo.createTime }}</span>
      </div>
      <NDivider class="pt-12px" />
    </NCard>
    <NCard class="w-full">
      <NTabs v-model:value="tab" type="line" animated>
        <NTabPane name="info" tab="基本资料">
          <NForm class="pt-12px" :model="model">
            <NFormItem label="昵称" path="nickName">
              <NInput v-model:value="model.nickName" placeholder="请输入昵称" />
            </NFormItem>
            <NFormItem label="真实姓名" path="realName">
              <NInput v-model:value="model.realName" placeholder="请输入真实姓名" />
            </NFormItem>
            <NFormItem label="性别" path="sex">
              <NRadioGroup v-model:value="model.sex">
                <NRadio value="男" label="男" />
                <NRadio class="pl-6px" value="女" label="女" />
              </NRadioGroup>
            </NFormItem>
            <NFormItem label="手机号" :rule="patternRules.phone" path="phone">
              <NInput v-model:value="model.phone" placeholder="请输入手机号" />
            </NFormItem>
            <NFormItem label="邮箱" :rule="patternRules.email" path="email">
              <NInput v-model:value="model.email" placeholder="请输入邮箱" />
            </NFormItem>
          </NForm>
        </NTabPane>
        <NTabPane name="password" tab="修改密码">
          <NForm ref="formRef" :model="pwdModel" :rules="rules">
            <NFormItem label="旧密码" path="oldPassword">
              <NInput v-model:value="pwdModel.oldPassword" placeholder="请输入旧密码" />
            </NFormItem>
            <NFormItem label="新密码" path="newPassword">
              <NInput
                v-model:value="pwdModel.newPassword"
                type="password"
                show-password-on="click"
                placeholder="请输入新密码"
              />
            </NFormItem>
            <NFormItem label="确认密码" path="confirmPassword">
              <NInput
                v-model:value="pwdModel.confirmPassword"
                type="password"
                show-password-on="click"
                placeholder="请再次输入新密码"
              />
            </NFormItem>
          </NForm>
        </NTabPane>
      </NTabs>
      <template #footer>
        <NDivider class="py-16px" />
        <NSpace :size="16" justify="end">
          <NButton @click="closePage">
            {{ $t('common.close') }}
          </NButton>
          <NButton type="primary" @click="handleSubmit">
            {{ $t('common.save') }}
          </NButton>
        </NSpace>
      </template>
    </NCard>
  </div>
</template>

<style scoped>
.n-divider {
  margin: 0;
}
</style>
