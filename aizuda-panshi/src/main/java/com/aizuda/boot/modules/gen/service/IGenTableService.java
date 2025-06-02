package com.aizuda.boot.modules.gen.service;

import com.aizuda.boot.modules.gen.entity.dto.GenDTO;
import com.aizuda.boot.modules.gen.entity.vo.GenVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 代码生成业务表 服务类
 *
 * @author 青苗
 * @since 1.0.0
 */
public interface IGenTableService {

    List<GenVO> preview(GenDTO dto);

    void download(HttpServletResponse response, GenDTO dto);
}
