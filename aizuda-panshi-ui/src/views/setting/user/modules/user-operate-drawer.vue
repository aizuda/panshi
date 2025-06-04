<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { useLoading } from '@sa/hooks';
import { enableStatusOptions } from '@/constants/business';
import { fetchCreateUser, fetchGetAllRoles, fetchGetUserRole, fetchUpdateUser } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';

defineOptions({
  name: 'UserOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.User | null;
  treeData?: Api.System.DepartmentList;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { loading: roleLoading, startLoading: startRoleLoading, endLoading: endRoleLoading } = useLoading();
const { loading: deptLoading, startLoading: startDeptLoading, endLoading: endDeptLoading } = useLoading();
const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();

const title = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增用户',
    edit: '编辑用户'
  };
  return titles[props.operateType];
});

type Model = Api.System.UserFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    username: '',
    password: '',
    nickName: '',
    realName: '',
    phone: '',
    email: '',
    sex: '男',
    status: 1,
    roleIds: [],
    departmentIds: [],
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'username' | 'password' | 'status'>;

const { patternRules } = useFormRules();

const rules: Record<RuleKey, App.Global.FormRule> = {
  username: { required: true, ...patternRules.username },
  password: { required: true, ...patternRules.pwd },
  status: { ...defaultRequiredRule, type: 'number' }
};

/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  startRoleLoading();
  const { error, data } = await fetchGetAllRoles();

  if (!error) {
    roleOptions.value = data.map(item => ({
      label: item.name,
      value: item.id
    }));
  }
  endRoleLoading();
}

async function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    startDeptLoading();
    Object.assign(model, props.rowData);
    const { data, error } = await fetchGetUserRole(model.id!);
    if (!error) {
      model.roleIds = data.roleIds;
      model.departmentIds = data.departmentIds;
    }
    endDeptLoading();
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const { id, username, nickName, realName, password, sex, status, phone, email, departmentIds, roleIds, remark } =
    model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateUser({
      username,
      nickName,
      realName,
      password,
      sex,
      status,
      phone,
      email,
      departmentIds,
      roleIds,
      remark
    });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateUser({
      id,
      username,
      nickName,
      realName,
      sex,
      status,
      phone,
      email,
      departmentIds,
      roleIds,
      remark
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
    getRoleOptions();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="用户名" path="username">
          <NInput v-model:value="model.username" placeholder="请输入用户名" />
        </NFormItem>
        <NFormItem v-if="operateType === 'add'" label="密码" path="password">
          <NInput v-model:value="model.password" type="password" show-password-on="click" placeholder="请输入密码" />
        </NFormItem>
        <NFormItem label="真实姓名" path="realName">
          <NInput v-model:value="model.realName" placeholder="真实姓名" />
        </NFormItem>
        <NFormItem label="昵称" path="nickName">
          <NInput v-model:value="model.nickName" placeholder="请输入昵称" />
        </NFormItem>
        <NFormItem label="性别" path="sex">
          <NRadioGroup v-model:value="model.sex">
            <NRadio value="男" label="男" />
            <NRadio value="女" label="女" />
          </NRadioGroup>
        </NFormItem>
        <NFormItem label="手机号" :rule="patternRules.phone" path="phone">
          <NInput v-model:value="model.phone" placeholder="请输入手机号" />
        </NFormItem>
        <NFormItem label="邮箱" :rule="patternRules.email" path="email">
          <NInput v-model:value="model.email" placeholder="请输入邮箱" />
        </NFormItem>
        <NFormItem label="状态" path="status">
          <NRadioGroup v-model:value="model.status">
            <NRadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value" :label="item.label" />
          </NRadioGroup>
        </NFormItem>
        <NFormItem label="所属角色" path="roleIds">
          <NSelect
            v-model:value="model.roleIds"
            :loading="roleLoading || deptLoading"
            multiple
            clearable
            :options="roleOptions"
            placeholder="请选择角色"
          />
        </NFormItem>
        <NFormItem :span="24" label="所属部门" path="departmentIds">
          <NTreeSelect
            v-model:value="model.departmentIds"
            multiple
            :loading="deptLoading"
            clearable
            :options="treeData as []"
            label-field="name"
            key-field="id"
            :default-expanded-keys="treeData?.length ? [treeData[0].id] : []"
            placeholder="请选择上级部门"
          />
        </NFormItem>
        <NFormItem label="备注" path="remark">
          <NInput v-model:value="model.remark" type="textarea" placeholder="请输入备注" />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
