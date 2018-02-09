package com.qinchy.shirodemo2api.service;

import com.qinchy.shirodemo2dao.model.Function;
import com.qinchy.shirodemo2api.common.page.Pagination;
import com.qinchy.shirodemo2api.common.page.PaginationResult;

import java.util.List;

public interface FunctionService extends EntityService<Function, Long> {

    PaginationResult<Function> findAllByPage(Pagination<Function> pagination);

    List<Function> findList();

    Function findById(Integer id);

    Function findByPermissionName(String permissionName);

    Function findByPid(Integer pid);

    List<Function> findListByPid(Integer pid);
} 
