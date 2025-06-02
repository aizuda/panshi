/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.boot.modules.common.controller;

import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.core.api.ApiController;
import com.aizuda.monitor.DiskInfo;
import com.aizuda.monitor.OshiMonitor;
import com.baomidou.kisso.annotation.LoginIgnore;
import com.baomidou.kisso.annotation.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "监控")
@Slf4j
@RestController
@RequestMapping("/v1/monitor")
public class MonitorController extends ApiController {
    @Resource
    private OshiMonitor oshiMonitor;

    @GetMapping("/log")
    @Permission(ignore = true) // 测试接口权限放开
    @LoginIgnore
    public String log() {
        log.error("ERROR测试");
        log.warn("WARN测试");
        log.info("INFO测试");
        log.debug("DEBUG测试");
        return "ok";
    }

    @Operation(summary = "服务器监控")
    @Permission("monitor:server")
    @PostMapping("/server")
    public Map<String, Object> monitor() {
        Map<String, Object> server = new HashMap<>(5);
        server.put("sysInfo", oshiMonitor.getSysInfo());
        server.put("cupInfo", oshiMonitor.getCpuInfo());
        server.put("memoryInfo", oshiMonitor.getMemoryInfo());
        server.put("jvmInfo", oshiMonitor.getJvmInfo());
        List<DiskInfo> diskInfos = oshiMonitor.getDiskInfos();
        server.put("diskInfos", diskInfos);
        if (CollectionUtils.isNotEmpty(diskInfos)) {
            long usableSpace = 0;
            long totalSpace = 0;
            for (DiskInfo diskInfo : diskInfos) {
                usableSpace += diskInfo.getUsableSpace();
                totalSpace += diskInfo.getTotalSpace();
            }
            double usedSize = (totalSpace - usableSpace);
            server.put("diskUsePercent", oshiMonitor.formatDouble(usedSize / totalSpace * 100));
        }

        // 系统前 10 个进程
//        List<OSProcess> processList = oshiMonitor.getOperatingSystem().getProcesses(null,
//                OperatingSystem.ProcessSorting.CPU_DESC, 10);
//        List<Map<String, Object>> processMapList = new ArrayList<>();
//        for (OSProcess process : processList) {
//            Map<String, Object> processMap = new HashMap<>(5);
//            processMap.put("name", process.getName());
//            processMap.put("pid", process.getProcessID());
//            processMap.put("cpu", oshiMonitor.formatDouble(process.getProcessCpuLoadCumulative()));
//            processMapList.add(processMap);
//        }
//        server.put("processList", processMapList);
        return server;
    }
}
