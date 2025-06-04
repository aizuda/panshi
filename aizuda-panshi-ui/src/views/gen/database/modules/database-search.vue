<script setup lang="ts">
import { databaseTypeOptions } from '@/constants/gen';
import { useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'DatabaseSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const { formRef, validate, restoreValidation } = useNaiveForm();

const model = defineModel<Api.Gen.DatabaseSearchParams>('model', { required: true });

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
      <NCollapseItem :title="$t('common.search')" name="post-search">
        <NForm v-if="model.data" ref="formRef" :model="model.data" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:12 m:6" label="别名" path="alias" class="pr-24px">
              <NInput v-model:value="model.data.alias" placeholder="请输入别名" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="库名称" path="dbName" class="pr-24px">
              <NInput v-model:value="model.data.dbName" placeholder="请输入库名称" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="类型" path="type" class="pr-24px">
              <NSelect
                v-model:value="model.data.type"
                placeholder="请选择数据源类型"
                :options="databaseTypeOptions"
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
