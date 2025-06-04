<script setup lang="ts">
import { useRouterPush } from '@/hooks/common/router';

defineOptions({
  name: 'PurButton'
});

const { routerPushByKey } = useRouterPush();

function toAbout() {
  routerPushByKey('about');
}
</script>

<template>
  <NButton type="primary" quaternary :focusable="false" @click="toAbout">
    <div class="flex-center gap-8px">
      <SvgIcon icon="ion:diamond-outline" class="text-20px text-primary font-500" />
      <span class="text-20px font-500">采购授权</span>
    </div>
  </NButton>
</template>

<style scoped></style>
