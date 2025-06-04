<script setup lang="ts">
import { useAttrs } from 'vue';
import type { TagProps } from 'naive-ui';
import { isNotNull } from '@/utils/common';

defineOptions({
  name: 'BooleanTag'
});

const flag = defineModel<boolean>('value', { required: true });

const tagMap: Record<string, NaiveUI.ThemeColor> = {
  true: 'success',
  false: 'error'
};

const attrs: TagProps = useAttrs();
</script>

<template>
  <NTag v-if="isNotNull(flag)" :type="tagMap[String(flag)]" v-bind="attrs">{{ flag ? '是' : '否' }}</NTag>
</template>

<style scoped></style>
