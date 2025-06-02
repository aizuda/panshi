package com.aizuda.boot.modules.system.controller;

import com.aizuda.boot.modules.system.entity.SysMessage;
import com.aizuda.boot.modules.system.entity.vo.InformMessageVO;
import com.aizuda.boot.modules.system.entity.vo.SysMessageVO;
import com.aizuda.boot.modules.system.service.ISysMessageService;
import com.aizuda.core.api.ApiController;
import com.aizuda.core.api.PageParam;
import com.aizuda.core.validation.Create;
import com.aizuda.core.validation.Update;
import com.baomidou.kisso.annotation.Permission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息发送表 前端控制器
 *
 * @author 青苗
 * @since 1.0.0
 */
@Tag(name = "系统消息表")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/message")
public class SysMessageController extends ApiController {
    private ISysMessageService sysMessageService;

    @Operation(summary = "分页列表")
    @Permission("sys:message:page")
    @PostMapping("/page")
    public Page<SysMessage> getPage(@RequestBody PageParam<SysMessage> dto) {
        return sysMessageService.page(dto.page(), dto.getData());
    }

    @Operation(summary = "我的分页列表")
    @Permission("sys:message:pageMy")
    @PostMapping("/page-my")
    public Page<SysMessageVO> pageMy(@RequestBody PageParam<SysMessageVO> dto) {
        return sysMessageService.pageMy(dto.page(), dto.getData());
    }

    @Operation(summary = "一键已读")
    @Permission("sys:message:read")
    @PostMapping("/read")
    public boolean read(@RequestParam(required = false) Long id) {
        return sysMessageService.read(id);
    }

    @Operation(summary = "告知消息")
    @Permission(ignore = true)
    @GetMapping("/inform")
    public InformMessageVO inform() {
        return sysMessageService.getInformByUser();
    }

    @Operation(summary = "查询 id 信息")
    @Permission("sys:message:get")
    @GetMapping("/get")
    public SysMessage get(@RequestParam Long id) {
        return sysMessageService.getById(id);
    }

    @Operation(summary = "根据 id 修改信息")
    @Permission("sys:message:update")
    @PostMapping("/update")
    public boolean update(@Validated(Update.class) @RequestBody SysMessage sysMessage) {
        return sysMessageService.updateById(sysMessage);
    }

    @Operation(summary = "创建添加")
    @Permission("sys:message:create")
    @PostMapping("/create")
    public boolean create(@Validated(Create.class) @RequestBody SysMessage sysMessage) {
        return sysMessageService.save(sysMessage);
    }

    @Operation(summary = "根据 ids 删除")
    @Permission("sys:message:delete")
    @PostMapping("/delete")
    public boolean delete(@NotEmpty @RequestBody List<Long> ids) {
        return sysMessageService.removeByIds(ids);
    }
}
