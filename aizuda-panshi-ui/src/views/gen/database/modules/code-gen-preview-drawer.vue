<script setup lang="ts">
import { ref, watch } from 'vue';
import { useClipboard } from '@vueuse/core';
import { useLoading } from '@sa/hooks';
import { fetchGetGenTablePreviewList } from '@/service/api';
import MonacoEditor from '@/components/common/monaco-editor.vue';

defineOptions({
  name: 'CodeGenPreviewDrawer'
});

interface Props {
  /** the edit row data */
  data?: Api.Gen.GenCode | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const tab = ref<number>(0);
const monacoEditorRef = ref();
const previewData = ref<Api.Gen.GenPreview[]>([]);
const { loading, startLoading, endLoading } = useLoading();

async function getGenPreview() {
  if (!props.data) return;
  startLoading();
  const { data, error } = await fetchGetGenTablePreviewList(props.data);
  if (error) {
    endLoading();
    closeDrawer();
    return;
  }
  previewData.value = data;
  monacoEditorRef.value?.setValue(previewData.value[tab.value]?.tplContent);
  endLoading();
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  closeDrawer();
  emit('submitted');
}

const { copy, isSupported } = useClipboard();

async function handleCopyCode() {
  if (!isSupported) {
    window.$message?.error('您的浏览器不支持Clipboard API');
    return;
  }

  const code = previewData.value[tab.value]?.tplContent;

  if (!code) {
    return;
  }

  await copy(code);
  window.$message?.success('代码复制成功');
}

watch(visible, () => {
  if (visible.value) {
    previewData.value = [];
    getGenPreview();
  }
});

watch(
  tab,
  () => {
    if (previewData.value[tab.value]) {
      monacoEditorRef.value?.setValue(previewData.value[tab.value]?.tplContent);
    }
  },
  { immediate: true }
);

function getGenLanguage(name: string) {
  if (name.endsWith('.java')) {
    return 'java';
  }

  if (name.endsWith('.xml')) {
    return 'xml';
  }

  if (name.endsWith('sql')) {
    return 'sql';
  }

  if (name.endsWith('.ts')) {
    return 'typescript';
  }

  if (name.endsWith('.vue')) {
    return 'html';
  }

  return 'plaintext';
}
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="if" width="100%">
    <NDrawerContent title="代码预览" :native-scrollbar="false" closable>
      <NSpin :show="loading" class="h-full" content-class="h-full">
        <div class="flex flex-row">
          <NTabs v-model:value="tab" type="line" placement="left" class="h-full" pane-class="h-full">
            <NTab v-for="(gen, index) in previewData" :key="index" :name="index" display-directive="show">
              {{ gen.tplName }}
            </NTab>
          </NTabs>
          <MonacoEditor
            v-if="previewData.length"
            ref="monacoEditorRef"
            v-model:model-value="previewData[tab].tplContent"
            class="tab-pane"
            read-only
            :language="getGenLanguage(previewData[tab]?.outFile || '')"
            height="calc(100vh - 162px)"
          />
          <div class="position-absolute right-42px top-2px">
            <NButton text :focusable="false" class="flex-center" @click="handleCopyCode">
              <template #icon>
                <icon-ep-copy-document class="text-14px" />
              </template>
              <span>复制</span>
            </NButton>
          </div>
        </div>
      </NSpin>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton @click="getGenPreview">刷新</NButton>
          <NButton :disabled="loading" type="primary" @click="handleSubmit">生成代码</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped>
:deep(.n-drawer-body-content-wrapper) {
  height: 100%;
}

:deep(.n-tabs) {
  width: unset !important;
}

:deep(.n-tabs.n-tabs--left .n-tabs-bar) {
  width: 5px !important;
}

.tab-pane {
  transition:
    color 0.3s cubic-bezier(0.4, 0, 0.2, 1),
    background-color 0.3s cubic-bezier(0.4, 0, 0.2, 1),
    opacity 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  padding-left: 12px;
}
</style>
