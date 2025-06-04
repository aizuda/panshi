<script setup lang="ts">
defineOptions({
  name: 'HeaderOperation'
});

interface Props {
  itemAlign?: NaiveUI.Align;
}

defineProps<Props>();
</script>

<template>
  <NSpace :align="itemAlign" wrap justify="end" class="lt-sm:w-200px">
    <slot name="default"></slot>
  </NSpace>
</template>

<style scoped></style>
