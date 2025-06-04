<script setup lang="tsx">
import { computed, reactive, watch } from 'vue';
import { databaseTypeOptions } from '@/constants/gen';
import { fetchCreateGenDatabase, fetchUpdateGenDatabase } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { isNotNull } from '@/utils/common';
import { $t } from '@/locales';

defineOptions({
  name: 'DatabaseOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.Gen.Database | null;
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

const drawerTitle = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增数据源',
    edit: '编辑数据源'
  };
  return titles[props.operateType];
});

type Model = Api.Gen.DatabaseFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    type: 'pgsql',
    alias: '',
    username: '',
    password: '',
    port: 5432,
    host: '',
    dbName: ''
  };
}

type RuleKey = Exclude<keyof Model, 'id'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  type: defaultRequiredRule,
  alias: defaultRequiredRule,
  username: defaultRequiredRule,
  password: defaultRequiredRule,
  host: defaultRequiredRule,
  dbName: defaultRequiredRule,
  port: { ...defaultRequiredRule, type: 'number' }
};

const updatePassword = '••••••••••••';

function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
    model.password = updatePassword;
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const { id, type, alias, username, password, host, dbName, port } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateGenDatabase({ type, alias, username, password, host, dbName, port });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const newPassword = model.password === updatePassword ? undefined : model.password;
    const { error } = await fetchUpdateGenDatabase({
      id,
      type,
      alias,
      username,
      password: newPassword,
      host,
      dbName,
      port
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

function blurPassword() {
  if (props.operateType === 'edit' && !isNotNull(model.password)) {
    model.password = updatePassword;
  }
}

function focusPassword() {
  if (props.operateType === 'edit' && model.password === updatePassword) {
    model.password = '';
  }
}
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="drawerTitle" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi :span="24" label="类型" path="type">
            <NSelect
              v-model:value="model.type"
              placeholder="请选择数据源类型"
              :options="databaseTypeOptions"
              clearable
            />
          </NFormItemGi>
          <NFormItemGi :span="24" label="别名" path="alias">
            <NInput v-model:value="model.alias" placeholder="请输入别名" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="主机" path="host">
            <NInput v-model:value="model.host" placeholder="请输入主机地址" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="端口" path="port">
            <NInputNumber v-model:value="model.port" placeholder="请输入端口号" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="库名称" path="dbName">
            <NInput v-model:value="model.dbName" placeholder="请输入数据库名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="用户名" path="username">
            <NInput v-model:value="model.username" placeholder="请输入用户名" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="密码" path="password">
            <NInput
              v-model:value="model.password"
              show-password-on="click"
              :type="model.password === updatePassword ? 'text' : 'password'"
              placeholder="请输入密码"
              @blur="() => blurPassword()"
              @focus="() => focusPassword()"
            />
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
