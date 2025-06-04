<script setup lang="tsx">
import { computed, defineComponent, reactive, ref, watch } from 'vue';
import type { SelectOption } from 'naive-ui';
import { NTooltip } from 'naive-ui';
import { enableStatusOptions, menuIconTypeOptions, menuTypeOptions } from '@/constants/business';
import { fetchCreateMenu, fetchUpdateMenu } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { getLocalMenuIcons } from '@/utils/icon';
import { humpToLine, isNotNull } from '@/utils/common';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';

defineOptions({
  name: 'MenuOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Menu | null;
  treeData?: Api.System.Menu[] | null;
  pid?: string;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();
const queryList = ref<{ key: string; value: string }[]>([]);

const drawerTitle = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增菜单',
    edit: '编辑菜单'
  };
  return titles[props.operateType];
});

type Model = Api.System.MenuFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    pid: props.pid || '0',
    title: '',
    type: 0,
    icon: null,
    iconType: '2',
    path: '',
    component: '',
    sort: 1,
    hidden: false,
    keepAlive: false,
    status: 1,
    apiList: [],
    query: '{}'
  };
}

type RuleKey = Extract<keyof Model, 'pid' | 'title' | 'path' | 'type' | 'component' | 'sort' | 'status' | 'redirect'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  pid: defaultRequiredRule,
  title: defaultRequiredRule,
  path: defaultRequiredRule,
  type: { ...defaultRequiredRule, type: 'number' },
  component: defaultRequiredRule,
  status: { ...defaultRequiredRule, type: 'number' },
  sort: { ...defaultRequiredRule, type: 'number' },
  redirect: defaultRequiredRule
};

const isBtn = computed(() => model.type === 3);
const localIcons = getLocalMenuIcons();
const localIconOptions = localIcons.map<SelectOption>(item => ({
  label: () => (
    <div class="flex-y-center gap-16px">
      <SvgIcon localIcon={`menu-${item}`} class="text-icon" />
      <span>{item}</span>
    </div>
  ),
  value: `icon-${item}`
}));

function handleInitModel() {
  queryList.value = [];
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
    model.component = model.component?.replaceAll('_', '/');
    model.iconType = model.icon?.startsWith('icon-') ? '2' : '1';
    if (model.type === 0 && (model.component === 'layout.base' || !model.component)) {
      model.type = -1;
    }
    const queryObj: { [key: string]: string } = JSON.parse(model.query || '{}');
    queryList.value = Object.keys(queryObj).map(item => ({ key: item, value: queryObj[item] }));
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const queryObj: { [key: string]: string } = {};
  queryList.value.forEach(item => (queryObj[item.key] = item.value));
  model.query = JSON.stringify(queryObj);

  const { id, pid, title, status, sort, hidden, keepAlive, query, redirect } = model;

  const path = !model.path.startsWith('/') ? `/${model.path}` : model.path;
  const alias = humpToLine(path.substring(1).replace('/', '_'));

  let icon = null;
  if (model.icon) {
    icon = model.iconType === '1' ? model.icon : model.icon?.replace('menu-', 'icon-');
  }

  let component = model.component;

  if (model.type === 0) {
    component = humpToLine(model.component?.replaceAll('/', '_') || '');
  }

  let type = model.type;

  if (model.type === -1) {
    component = model.pid === '0' ? 'layout.base' : undefined;
    type = 0;
  }

  if ([1, 2].includes(model.type)) {
    component = 'iframe-page';
  }

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateMenu({
      pid,
      title,
      type,
      icon,
      path,
      alias,
      status,
      sort,
      hidden,
      component,
      keepAlive,
      query,
      redirect
    });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateMenu({
      id,
      pid,
      title,
      type,
      icon,
      path,
      alias,
      status,
      sort,
      hidden,
      component,
      keepAlive,
      query,
      redirect
    });
    if (error) {
      return;
    }
    window.$message?.success($t('common.updateSuccess'));
  }

  closeDrawer();
  emit('submitted');
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
  }
});

function onCreate() {
  return {
    key: '',
    value: ''
  };
}

const FormTipComponent = defineComponent({
  name: 'FormTipComponent',
  props: {
    content: {
      type: String,
      default: ''
    }
  },
  setup(tipProps) {
    return () => (
      <NTooltip trigger="hover">
        {{
          default: () => <span>{tipProps.content}</span>,
          trigger: () => (
            <div>
              <SvgIcon icon="ep:warning" />
            </div>
          )
        }}
      </NTooltip>
    );
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="drawerTitle" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi :span="24" label="上级菜单" path="pid">
            <NTreeSelect
              v-model:value="model.pid"
              :options="treeData as []"
              label-field="title"
              key-field="id"
              :default-expanded-keys="['0']"
              placeholder="请选择上级菜单"
            />
          </NFormItemGi>
          <NFormItemGi :span="24" label="菜单名称" path="title">
            <NInput v-model:value="model.title" placeholder="请输入菜单名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="菜单类型" path="type">
            <NRadioGroup v-model:value="model.type" name="type">
              <NRadioButton v-for="item in menuTypeOptions" :key="item.value" :value="item.value" :label="item.label" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="!isBtn" span="24" label="图标类型" path="iconType">
            <NRadioGroup v-model:value="model.iconType">
              <NRadio v-for="item in menuIconTypeOptions" :key="item.value" :value="item.value" :label="item.label" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="!isBtn" span="24" label="菜单图标" path="icon">
            <template v-if="model.iconType === '1'">
              <NInput v-model:value="model.icon" placeholder="请输入图标" class="flex-1">
                <template #suffix>
                  <SvgIcon v-if="model.icon" :icon="model.icon" class="text-icon" />
                </template>
              </NInput>
            </template>
            <template v-if="model.iconType === '2'">
              <NSelect v-model:value="model.icon" placeholder="请选择本地图标" :options="localIconOptions" />
            </template>
          </NFormItemGi>
          <NFormItemGi v-if="!isBtn" :span="24" path="path">
            <template #label>
              <div class="flex-center">
                <FormTipComponent content="路由地址记得以 '/' 开头" />
                <span class="pl-3px">路由地址</span>
              </div>
            </template>
            <NInput v-model:value="model.path" placeholder="请输入路由地址" />
          </NFormItemGi>
          <NFormItemGi v-if="model.type === 0" :span="24" label="组件地址" path="component">
            <NInputGroup>
              <NInputGroupLabel>views/</NInputGroupLabel>
              <NInput v-model:value="model.component" placeholder="请输入组件地址" />
              <NInputGroupLabel>/index.vue</NInputGroupLabel>
            </NInputGroup>
          </NFormItemGi>
          <NFormItemGi
            v-if="model.type === 0"
            span="24"
            :show-feedback="!queryList.length"
            label="路由参数"
            path="query"
          >
            <NDynamicInput v-model:value="queryList" item-style="margin-bottom: 0" :on-create="onCreate">
              <template #default="{ index }">
                <div class="flex">
                  <NFormItem
                    ignore-path-change
                    :show-label="false"
                    :path="`query[${index}].key`"
                    :rule="{ ...defaultRequiredRule, validator: value => isNotNull(value) }"
                  >
                    <NInput v-model:value="queryList[index].key" placeholder="Key" @keydown.enter.prevent />
                  </NFormItem>
                  <div class="mx-8px h-34px lh-34px">=</div>
                  <NFormItem
                    ignore-path-change
                    :show-label="false"
                    :path="`query[${index}].value`"
                    :rule="{ ...defaultRequiredRule, validator: value => isNotNull(value) }"
                  >
                    <NInput v-model:value="queryList[index].value" placeholder="Value" @keydown.enter.prevent />
                  </NFormItem>
                </div>
              </template>
            </NDynamicInput>
          </NFormItemGi>
          <NFormItemGi v-if="[1, 2].includes(model.type)" :span="24" label="外部地址" path="redirect">
            <NInput v-model:value="model.redirect" placeholder="请输入外部地址" />
          </NFormItemGi>
          <NFormItemGi v-if="!isBtn" :span="12" label="是否缓存" path="keepAlive">
            <NRadioGroup v-model:value="model.keepAlive">
              <NSpace>
                <NRadio :value="true" label="是" />
                <NRadio :value="false" label="否" />
              </NSpace>
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="!isBtn" :span="12" label="是否隐藏" path="hidden">
            <NRadioGroup v-model:value="model.hidden">
              <NSpace>
                <NRadio :value="true" label="是" />
                <NRadio :value="false" label="否" />
              </NSpace>
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi :span="12" label="状态" path="status">
            <NRadioGroup v-model:value="model.status">
              <NSpace>
                <NRadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value" :label="item.label" />
              </NSpace>
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi :span="12" label="排序" path="sort">
            <NInputNumber v-model:value="model.sort" placeholder="请输入排序" />
          </NFormItemGi>
        </NGrid>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.save') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
