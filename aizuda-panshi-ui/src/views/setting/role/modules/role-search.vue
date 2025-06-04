<script setup lang="ts">
import { enableStatusOptions } from '@/constants/business';
import { translateNumberOptions } from '@/utils/common';
import { $t } from '@/locales';

defineOptions({
  name: 'RoleSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const model = defineModel<Api.System.RoleSearchParams>('model', { required: true });

function reset() {
  emit('reset');
}

function search() {
  emit('search');
}
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse>
      <NCollapseItem :title="$t('common.search')" name="role-search">
        <NForm v-if="model.data" :model="model.data" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:12 m:6" label="角色名称" path="name" class="pr-24px">
              <NInput v-model:value="model.data.name" placeholder="请输入角色名称" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="角色编码" path="alias" class="pr-24px">
              <NInput v-model:value="model.data.alias" placeholder="请输入角色编码" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="角色状态" path="status" class="pr-24px">
              <NSelect
                v-model:value="model.data.status"
                placeholder="请选择角色状态"
                :options="translateNumberOptions(enableStatusOptions)"
                clearable
              />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" :show-feedback="false" class="pb-6px pr-24px">
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
