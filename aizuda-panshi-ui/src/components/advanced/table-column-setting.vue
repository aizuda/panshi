<script setup lang="ts" generic="T extends Record<string, unknown>, K = never">
import { watch } from 'vue';
import { useRoute } from 'vue-router';
import { VueDraggable } from 'vue-draggable-plus';
import { localStg } from '@/utils/storage';
import { $t } from '@/locales';

defineOptions({
  name: 'TableColumnSetting'
});

const route = useRoute();
const columns = defineModel<NaiveUI.TableColumnCheck[]>('columns', {
  required: true
});

watch(
  columns,
  value => {
    const tableColumnSetting = localStg.get('tableColumnSetting') || {};
    tableColumnSetting[String(route.name!)] = value;
    localStg.set('tableColumnSetting', tableColumnSetting);
  },
  { deep: true }
);

function initColums() {
  const tableColumnSetting = localStg.get('tableColumnSetting') || {};
  const setting = tableColumnSetting[String(route.name!)];
  if (!setting) {
    return;
  }
  columns.value = setting;
}

initColums();
</script>

<template>
  <NPopover placement="bottom-end" trigger="click">
    <template #trigger>
      <NButton size="small">
        <template #icon>
          <icon-ant-design-setting-outlined class="text-icon" />
        </template>
        {{ $t('common.columnSetting') }}
      </NButton>
    </template>
    <VueDraggable v-model="columns" :animation="150" filter=".none_draggable">
      <div v-for="item in columns" :key="item.key" class="h-36px flex-y-center rd-4px hover:(bg-primary bg-opacity-20)">
        <icon-mdi-drag class="mr-8px h-full cursor-move text-icon" />
        <NCheckbox v-model:checked="item.checked" class="none_draggable flex-1">
          <template v-if="typeof item.title === 'function'">
            <component :is="item.title" />
          </template>
          <template v-else>{{ item.title }}</template>
        </NCheckbox>
      </div>
    </VueDraggable>
  </NPopover>
</template>

<style scoped></style>
