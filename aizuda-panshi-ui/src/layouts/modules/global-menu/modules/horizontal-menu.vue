<script setup lang="ts">
import { GLOBAL_HEADER_MENU_ID } from '@/constants/app';
import { useRouteStore } from '@/store/modules/route';
import { useRouterPush } from '@/hooks/common/router';
import { useMenu } from '../../../context';

defineOptions({
  name: 'HorizontalMenu'
});

const routeStore = useRouteStore();
const { routerPushByPathWithMetaQuery } = useRouterPush();
const { selectedKey } = useMenu();
</script>

<template>
  <Teleport :to="`#${GLOBAL_HEADER_MENU_ID}`">
    <NMenu
      mode="horizontal"
      :value="selectedKey"
      :options="routeStore.menus"
      :indent="18"
      responsive
      @update:value="routerPushByPathWithMetaQuery"
    />
  </Teleport>
</template>

<style scoped></style>
