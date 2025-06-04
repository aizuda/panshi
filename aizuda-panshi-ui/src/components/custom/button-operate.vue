<script setup lang="ts">
import type { PopoverPlacement } from 'naive-ui';

defineOptions({
  name: 'ButtonOperate'
});

interface Props {
  /** Iconify icon name */
  icon?: string;
  /** Tooltip content */
  tooltipContent?: string;
  /** Tooltip placement */
  tooltipPlacement?: PopoverPlacement;
  type?: NaiveUI.ThemeColor;
  zIndex?: number;
}

withDefaults(defineProps<Props>(), {
  class: '',
  icon: '',
  tooltipContent: '',
  tooltipPlacement: 'bottom',
  type: 'default',
  zIndex: 98
});
</script>

<template>
  <NTooltip :placement="tooltipPlacement" :z-index="zIndex" :disabled="!tooltipContent">
    <template #trigger>
      <NButton :type="type" text class="h-[36px] text-icon">
        <template #icon>
          <SvgIcon :icon="icon" />
        </template>
      </NButton>
    </template>
    {{ tooltipContent }}
  </NTooltip>
</template>

<style scoped></style>
