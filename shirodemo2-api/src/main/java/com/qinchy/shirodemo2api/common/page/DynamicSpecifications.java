package com.qinchy.shirodemo2api.common.page;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.qinchy.shirodemo2api.common.utils.SearchFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;

public class DynamicSpecifications {
    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> clazz) {
        return new Specification<T>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (CollectionUtils.isNotEmpty(filters)) {

                    List<Predicate> predicates = Lists.newArrayList();
                    parse(root, builder, predicates, filters);

                    // 将所有条件用 and 联合起来
                    if (predicates.size() > 0) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }
        };
    }

    private static <T> void parse(Root<T> root, CriteriaBuilder builder, List<Predicate> predicates, Collection<SearchFilter> filters) {
        for (SearchFilter filter : filters) {
            // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
            String[] names = StringUtils.split(filter.fieldName, ".");
            Path expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }

            // logic operator
            switch (filter.operator) {
                case NEQ:
                    predicates.add(builder.notEqual(expression, filter.value));
                    break;
                case EQ:
                    predicates.add(builder.equal(expression, filter.value));
                    break;
                case LIKE:
                    predicates.add(builder.like(expression, "%" + filter.value + "%"));
                    break;
                case GT:
                    predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                    break;
                case LT:
                    predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                    break;
                case GTE:
                    predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                    break;
                case LTE:
                    predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                    break;
                case ISNULL:
                    predicates.add(builder.isNull(expression));
                    break;
                case ISNOTNULL:
                    predicates.add(builder.isNotNull(expression));
                    break;
            }
        }
    }


    public static <T> Specification<T> bySearchFilterOr(final Collection<SearchFilter> filters, final Class<T> clazz) {
        return new Specification<T>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (CollectionUtils.isNotEmpty(filters)) {

                    List<Predicate> predicates = Lists.newArrayList();
                    parse(root, builder, predicates, filters);

                    // 将所有条件用 or 联合起来
                    if (predicates.size() > 0) {
                        return builder.or(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }
        };
    }
}