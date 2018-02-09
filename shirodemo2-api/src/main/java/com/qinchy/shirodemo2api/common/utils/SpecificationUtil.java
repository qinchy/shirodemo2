package com.qinchy.shirodemo2api.common.utils;

import java.util.Map;

import com.qinchy.shirodemo2api.common.page.DynamicSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtil<T> {

	protected Class<T> entityClass;
	

	public SpecificationUtil() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}
	
	/**
	 * 创建分页请求.
	 */
	protected PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		sort = getOrders(sortType, sort);
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	public static Sort getOrders(String sortType, Sort sort) {
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.ASC, "id");
		} else if (!StringUtils.contains(sortType,"_ASC")) {
			sort = new Sort(Direction.DESC, sortType);
		} else {
			sort = new Sort(Direction.ASC, sortType.split("_ASC")[0]);
		}
		return sort;
	}

	/**
	 * 创建动态查询条件组合.
	 */
	public Specification<T> buildSpecification(Map<String, Object> searchParams,String orAnd) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec=null;
		if(orAnd.equals("or")){
			  spec = DynamicSpecifications.bySearchFilterOr(filters.values(), ReflectionUtils.getSuperClassGenricType(getClass()));
		}else{
			  spec = DynamicSpecifications.bySearchFilter(filters.values(), ReflectionUtils.getSuperClassGenricType(getClass()));
		}
		
		return spec;
	}
	
	 
	
}
