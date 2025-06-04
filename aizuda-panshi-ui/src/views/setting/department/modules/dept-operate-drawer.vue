<script setup lang="tsx">
import { computed, reactive, ref, watch } from 'vue';
import { enableStatusOptions } from '@/constants/business';
import { fetchCreateDept, fetchUpdateDept } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import WorkflowSelect from '@/components/workflow/select/index.vue';

defineOptions({
  name: 'DeptOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.System.Department | null;
  treeData?: Api.System.DepartmentList;
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

const selectVisible = ref<boolean>(false);
const selectData = ref<Workflow.NodeAssignee[]>([]);
const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();

const drawerTitle = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增部门',
    edit: '编辑部门'
  };
  return titles[props.operateType];
});

const treeOptions = computed(() => {
  return [
    {
      id: '0',
      name: '根部门',
      children: props.treeData
    }
  ];
});

type Model = Api.System.DepartmentFormParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    pid: props.pid || (props.treeData?.length ? props.treeData![0].id : undefined),
    name: '',
    code: '',
    sort: 1,
    status: 1,
    remark: '',
    headId: undefined,
    headName: undefined
  };
}

type RuleKey = Extract<keyof Model, 'pid' | 'name' | 'code' | 'sort' | 'status'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  pid: defaultRequiredRule,
  name: defaultRequiredRule,
  code: defaultRequiredRule,
  status: { ...defaultRequiredRule, type: 'number' },
  sort: { ...defaultRequiredRule, type: 'number' }
};

function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
    if (model.headId) {
      selectData.value = [{ id: model.headId!, name: model.headName! }];
    } else {
      selectData.value = [];
    }
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const { id, pid, name, code, status, sort, remark, headId, headName } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateDept({
      pid,
      name,
      code,
      status,
      sort,
      remark,
      headId,
      headName
    });
    if (error) {
      return;
    }
    window.$message?.success($t('common.addSuccess'));
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateDept({
      id,
      pid,
      name,
      code,
      status,
      sort,
      remark,
      headId,
      headName
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

function selectHandle() {
  selectVisible.value = true;
}

function handleSaveSelect(data: Workflow.NodeAssignee[]) {
  if (data.length > 0) {
    model.headId = data[0].id;
    model.headName = data[0].name;
  } else {
    model.headId = undefined;
    model.headName = undefined;
  }
  selectData.value = data;
}
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="drawerTitle" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi :span="24" label="上级部门" path="pid">
            <NTreeSelect
              v-model:value="model.pid"
              :options="treeOptions as []"
              label-field="name"
              key-field="id"
              :default-expanded-keys="treeData?.length ? [treeData[0].id] : []"
              placeholder="请选择上级部门"
            />
          </NFormItemGi>
          <NFormItemGi :span="24" label="部门名称" path="name">
            <NInput v-model:value="model.name" placeholder="请输入部门名称" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="部门编码" path="code">
            <NInput v-model:value="model.code" placeholder="请输入部门编码" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="部门主管" path="headId">
            <div class="flex-center gap-8px">
              <NButton type="primary" size="small" round @click="() => selectHandle()">
                <template #icon>
                  <icon-ep-plus />
                </template>
                选择主管
              </NButton>
              <UserAvatar v-if="selectData.length > 0" :name="selectData[0].name" />
            </div>
          </NFormItemGi>
          <NFormItemGi :span="24" label="状态" path="status">
            <NRadioGroup v-model:value="model.status">
              <NSpace>
                <NRadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value" :label="item.label" />
              </NSpace>
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi :span="24" label="排序" path="sort">
            <NInputNumber v-model:value="model.sort" placeholder="请输入排序" />
          </NFormItemGi>
          <NFormItemGi :span="24" label="备注" path="remark">
            <NInput v-model:value="model.remark" type="textarea" placeholder="请输入备注" />
          </NFormItemGi>
        </NGrid>
      </NForm>
      <WorkflowSelect
        v-model:visible="selectVisible"
        v-model:value="selectData"
        :type="1"
        :max="1"
        @save="handleSaveSelect"
      />
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
