<script setup lang="ts">
import { enableStatusOptions } from '@/constants/business';
import { useNaiveForm } from '@/hooks/common/form';
import { translateNumberOptions } from '@/utils/common';
import { $t } from '@/locales';

defineOptions({
  name: 'UserSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const { formRef, validate, restoreValidation } = useNaiveForm();

const model = defineModel<Api.System.UserSearchParams>('model', { required: true });

async function reset() {
  await restoreValidation();
  emit('reset');
}

async function search() {
  await validate();
  emit('search');
}
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse>
      <NCollapseItem :title="$t('common.search')" name="user-search">
        <NForm v-if="model.data" ref="formRef" :model="model.data" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:12 m:6" label="用户名" path="username" class="pr-24px">
              <NInput v-model:value="model.data.username" placeholder="请输入用户名" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="昵称" path="nickName" class="pr-24px">
              <NInput v-model:value="model.data.nickName" placeholder="请输入昵称" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="手机号" path="phone" class="pr-24px">
              <NInput v-model:value="model.data.phone" placeholder="请输入手机号" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="状态" path="status" class="pr-24px">
              <NSelect
                v-model:value="model.data.status"
                placeholder="请选择状态"
                :options="translateNumberOptions(enableStatusOptions)"
                clearable
              />
            </NFormItemGi>
            <NFormItemGi :show-feedback="false" span="24" class="pb-6px pr-24px">
              <NSpace class="w-full" justify="end">
                <NButton @click="reset">
                  <template #icon>
                    <icon-ic-round-refresh class="text-icon" />
                  </template>
                  {{ $t('common.reset') }}
                </NButton>
                <NButton type="primary" ghost @click="search">
                  <template #icon>
                    <icon-ic-round-search class="text-icon" />
                  </template>
                  {{ $t('common.search') }}
                </NButton>
              </NSpace>
            </NFormItemGi>
          </NGrid>
        </NForm>
      </NCollapseItem>
    </NCollapse>
  </NCard>
</template>

<style scoped></style>
