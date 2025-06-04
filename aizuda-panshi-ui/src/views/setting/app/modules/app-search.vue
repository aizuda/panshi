<script setup lang="ts">
import { enableStatusOptions } from '@/constants/business';
import { useNaiveForm } from '@/hooks/common/form';
import { translateNumberOptions } from '@/utils/common';
import { $t } from '@/locales';

defineOptions({
  name: 'AppSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const { formRef, validate, restoreValidation } = useNaiveForm();

const model = defineModel<Api.System.AppSearchParams>('model', { required: true });

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
      <NCollapseItem :title="$t('common.search')" name="app-search">
        <NForm v-if="model.data" ref="formRef" :model="model.data" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:12 m:6" label="应用标识" path="identification" class="pr-24px">
              <NInput v-model:value="model.data.identification" placeholder="请输入应用标识" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="应用名称" path="name" class="pr-24px">
              <NInput v-model:value="model.data.name" placeholder="请输入应用名称" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="应用状态" path="status" class="pr-24px">
              <NSelect
                v-model:value="model.data.status"
                placeholder="请选择应用状态"
                :options="translateNumberOptions(enableStatusOptions)"
                clearable
              />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" class="pr-24px">
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

<style scoped>
:deep(.n-collapse-item__header) {
  padding-top: 0 !important;
}
</style>
