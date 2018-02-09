package com.qinchy.shirodemo2dao.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.qinchy.shirodemo2dao.model.Function;
@Transactional
@Service
public interface FunctionDao  extends BaseDao<Function, Long> {
	 
	@Query("from Function")
	 public List<Function> findList();
	
	 public Function findByPermissionName(String permissionName);
	 

	 public Function findByPid(Integer pid);
	 @Query("from Function where pid=?")
	  public List<Function> findListByPid(Integer pid);
} 