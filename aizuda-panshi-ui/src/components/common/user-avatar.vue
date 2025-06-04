<script setup lang="ts">
import { computed, useSlots } from 'vue';

interface Props {
  size?: number;
  name?: string;
  avatar?: string;
  showName?: boolean;
  closable?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  size: 24,
  name: '',
  avatar: '',
  showName: true,
  closable: false
});

interface Emits {
  (e: 'close'): void;
}

const emit = defineEmits<Emits>();

const slots = useSlots();

const AvatarStyle = computed(() => {
  const padding = props.showName ? 4 : 0;
  return {
    height: `${props.size + padding * 2}px`,
    borderRadius: `${(props.size + padding * 2) / 2}px`,
    padding: `${padding}px`
  };
});

const NameStyle = computed(() => {
  return {
    fontSize: `${props.size / 2 + 1}px`,
    paddingLeft: '3px'
  };
});

const CloseStyle = computed(() => {
  return {
    width: props.size,
    height: props.size
  };
});

const handleClose = () => {
  emit('close');
};
</script>

<template>
  <div class="avatar" :style="AvatarStyle">
    <NAvatar v-if="avatar && !slots.avatar" :size="size" class="icon" circle :src="avatar" />
    <NAvatar v-if="!avatar && !slots.avatar" :size="size" circle class="icon">
      <template v-if="name">
        <span :style="NameStyle">{{ name?.charAt(0) || '-' }}</span>
      </template>
      <icon-ep-user-filled v-else />
    </NAvatar>
    <NAvatar v-if="slots.avatar" :size="size" circle class="icon">
      <slot name="avatar"></slot>
    </NAvatar>
    <div v-if="showName" class="name" :style="NameStyle">{{ name }}</div>
    <NButton v-if="closable" :style="CloseStyle" text @click="handleClose">
      <template #icon>
        <icon-ep-close :font-size="props.size / 2" />
      </template>
    </NButton>
  </div>
</template>

<style scoped lang="scss">
.avatar {
  background: #f5f7fa;
  display: flex;
  align-items: center;
  width: fit-content;

  .n-avatar {
    background: rgb(var(--primary-color));
  }

  .icon {
    overflow: hidden;
    flex-shrink: 0;
  }

  .name {
    user-select: none;
    color: #606266;
    margin: 0 4px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    max-width: 64px;
  }
}

.dark {
  .avatar {
    background: #262727;

    .name {
      color: rgba(255, 255, 255, 0.82);
    }
  }
}

.n-avatar {
  display: flex;
  align-items: center;
  justify-content: center;

  :deep(.n-avatar__text) {
    line-height: unset;
  }
}
</style>
