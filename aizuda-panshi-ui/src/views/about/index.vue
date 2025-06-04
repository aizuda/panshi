<script setup lang="ts">
import { computed } from 'vue';
import { useAppStore } from '@/store/modules/app';
import pkg from '~/package.json';

const appStore = useAppStore();

const column = computed(() => (appStore.isMobile ? 1 : 2));

interface PkgJson {
  name: string;
  version: string;
  dependencies: PkgVersionInfo[];
  devDependencies: PkgVersionInfo[];
}

interface PkgVersionInfo {
  name: string;
  version: string;
}

const { name, version, dependencies, devDependencies } = pkg;

function transformVersionData(tuple: [string, string]): PkgVersionInfo {
  const [$name, $version] = tuple;
  return {
    name: $name,
    version: $version
  };
}

const pkgJson: PkgJson = {
  name,
  version,
  dependencies: Object.entries(dependencies)
    .map(item => transformVersionData(item))
    .filter(item => !item.name.startsWith('@sa/')),
  devDependencies: Object.entries(devDependencies)
    .map(item => transformVersionData(item))
    .filter(item => !item.name.startsWith('@sa/'))
};

const latestBuildTime = BUILD_TIME;
</script>

<template>
  <NSpace vertical :size="16">
    <NCard title="关于" :bordered="false" size="small" segmented class="card-wrapper">
      <p>
        爱组搭
        <span class="font-600">Aizuda</span>
        , 像搭积木一样进行低代码甚至零代码快速构建应用
      </p>
      <p>
        飞龙工作流
        <span class="font-600">FlowLong</span>
        🐉, 真正的国产工作流引擎, 比飞书钉钉审批流程更加强大 🚩 为中国特色审批匠心打造❗
      </p>
    </NCard>
    <NCard title="项目信息" :bordered="false" size="small" segmented class="card-wrapper">
      <NDescriptions label-placement="left" bordered size="small" :column="column">
        <NDescriptionsItem label="版本">
          <NTag type="primary">{{ pkgJson.version }}</NTag>
        </NDescriptionsItem>
        <NDescriptionsItem label="最新构建时间">
          <NTag type="primary">{{ latestBuildTime }}</NTag>
        </NDescriptionsItem>
        <NDescriptionsItem label="仓库地址">
          <span class="flex gap-16px">
            <a class="text-primary" :href="pkg.homepage" target="_blank" rel="noopener noreferrer">Gitee 地址</a>
            <a class="text-primary" :href="pkg.github" target="_blank" rel="noopener noreferrer">Github 地址</a>
          </span>
        </NDescriptionsItem>
        <NDescriptionsItem label="采购地址">
          <a class="text-primary" :href="pkg.website" target="_blank" rel="noopener noreferrer">采购地址</a>
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
    <NCard title="采购授权" :bordered="false" size="small" segmented class="card-wrapper">
      <div class="flex-center">
        <img src="@/assets/imgs/wx153666.jpg" width="180" />
      </div>
      <NDescriptions class="pur-desc" title="常见问题：" label-placement="top" bordered size="small" :column="1">
        <NDescriptionsItem label="01. 商用授权如何保障？">
          AiZuDa官方提供的付费软件享有版权保障，您可放心商用；
          购买付费版软件后，我们将为您签订授权合同，提供商用授权书，保障您的商用权益；
        </NDescriptionsItem>
        <NDescriptionsItem label="02. 是否可以开发票？在哪里开？">
          可以，我们支持开具增值税电子普通发票。购买后加上述微信进行索取。
        </NDescriptionsItem>
        <NDescriptionsItem label="03. 更新权是什么意思？">
          获取授权后，您将拥有AiZuDa永久使用权，但仅拥有一年的更新权，一年以后支付订阅费继续可以享受更新技术服务。
          例如：您在2024年2月22日，购买了一年更新权的版本，那么只要在2025年2月22日之前发布的版本，您都可以升级并免费使用，但在2025年2月22日之后发布的版本是不可以升级使用的，
          如果需要，您可以支付订阅费
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
    <NCard title="生产依赖" :bordered="false" size="small" segmented class="card-wrapper">
      <NDescriptions label-placement="left" bordered size="small" :column="column">
        <NDescriptionsItem v-for="item in pkgJson.dependencies" :key="item.name" :label="item.name">
          {{ item.version }}
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
    <NCard title="开发依赖" :bordered="false" size="small" segmented class="card-wrapper">
      <NDescriptions label-placement="left" bordered size="small" :column="column">
        <NDescriptionsItem v-for="item in pkgJson.devDependencies" :key="item.name" :label="item.name">
          {{ item.version }}
        </NDescriptionsItem>
      </NDescriptions>
    </NCard>
  </NSpace>
</template>

<style scoped>
.pur-desc :deep(.n-descriptions-table-header) {
  font-weight: 600 !important;
}
</style>
