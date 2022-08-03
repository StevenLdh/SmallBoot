package com.supper.smallboot.infrastructure.utils;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by 2021/8/4
 *
 * @author xuguang
 * @version V1.0
 * @description: 常用的jdk8 stream的工具类
 */
@UtilityClass
public class StreamUtils {


    /**
     * 抽取集合中的某个属性为一个集合
     *
     * @param list    集合
     * @param mapping 查询属性的方法
     * @param <E>     集合中的元素类型
     * @param <T>     属性类型
     * @return 属性的集合
     */
    public <E, T> List<T> fetchProperty(List<E> list, Function<E, T> mapping) {
        ArrayList<Function<E, T>> mapList = new ArrayList<>();
        mapList.add(mapping);
        return fetchProperty(list, mapList);
    }

    public <E, T> List<T> fetchProperty(List<E> list, List<Function<E, T>> mappingList) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return mappingList.stream().flatMap(map -> list.stream().map(map))
                .filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据某个属性进行减操作
     * list1-list2
     *
     * @param <E> 集合元素
     * @param <T> 元素中的某个属性
     */
    public <E, T> List<E> subtract(final List<E> list1, final List<? extends E> list2, Function<E, T> getProperty) {
        if (CollectionUtils.isEmpty(list2)) {
            return list1;
        }

        return list1.stream().filter(item -> !list2.stream().map(getProperty).filter(Objects::nonNull).collect(Collectors.toList()).contains(getProperty.apply(item)))
                .collect(Collectors.toList());
    }

    /**
     * 根据某个属性进行并集操作
     *
     * @param <E> 集合元素
     * @param <T> 元素中的某个属性
     */
    public <E, T> List<E> retainAll(final List<E> list1, final List<? extends E> list2, Function<E, T> getProperty) {
        if (CollectionUtils.isEmpty(list1) || CollectionUtils.isEmpty(list2)) {
            return Collections.emptyList();
        }
        return list1.stream().filter(item -> list2.stream().map(getProperty).filter(Objects::nonNull).collect(Collectors.toList()).contains(getProperty.apply(item)))
                .collect(Collectors.toList());
    }

    /**
     * 从集合中选中该id,且设置上下id
     */
    public <T> T setBeforeAndAfterId(List<T> list, Long id, Function<T, Long> mapping,
                                     BiConsumer<T, Long> beforeSetter, BiConsumer<T, Long> afterSetter) {
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            Long currentId = mapping.apply(t);
            if (Objects.equals(id, currentId)) {
                Long beforeId = i - 1 < 0 ? null : mapping.apply(list.get(i - 1));
                beforeSetter.accept(t, beforeId);

                Long afterId = i + 1 >= list.size() ? null : mapping.apply(list.get(i + 1));
                afterSetter.accept(t, afterId);
                return t;
            }
        }
        return null;
    }

    /**
     * 转为map, 注意:key重复时, 取第一次key对应的value
     */
    public <E, K, V> Map<K, V> toMap(List<E> list, Function<E, K> keyMapping, Function<E, V> valueMapping) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(keyMapping, valueMapping, ObjectUtils::firstNonNull));
    }

    /**
     * 二维数值转集合list
     */
    public <E> List<E> array2ToList(E[][] array2) {
        return Arrays.stream(array2).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    /**
     * 一维list变成树
     */
    public <E> List<E> listToTree(List<E> list,
                                  Function<E, String> nodeFunction,
                                  Function<E, String> parentFunction,
                                  BiConsumer<E, List<E>> childListFunction) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        //取第一层节点
        List<E> firstLevel = getFirstLevel(list, nodeFunction, parentFunction);
        Map<String, List<E>> parent2ChildList = list.stream()
                //排除刚才取了的第一层节点
                .filter(a -> !fetchProperty(firstLevel, nodeFunction).contains(nodeFunction.apply(a)))
                .collect(Collectors.groupingBy(parentFunction));
        firstLevel.forEach(a -> forEach(parent2ChildList, a, nodeFunction, childListFunction));
        return firstLevel;
    }

    /**
     * 获取第一级, 可能是顶点, 也可能是多个第一级节点
     */
    public <E> List<E> getFirstLevel(List<E> list,
                                     Function<E, String> nodeFunction,
                                     Function<E, String> parentFunction) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<String> nodes = fetchProperty(list, nodeFunction);
        List<String> parentNods = fetchProperty(list, parentFunction);
        List<String> topNodes = ListUtils.subtract(parentNods, nodes);

        Predicate<E> predicate;
        if (CollectionUtils.isEmpty(topNodes)) {
            //如果没有顶点, 可能顶点等于本身
            predicate = a -> Objects.equals(nodeFunction.apply(a), parentFunction.apply(a));
        } else {
            predicate = a -> topNodes.contains(parentFunction.apply(a));
        }
        return list.stream().filter(predicate).collect(Collectors.toList());
    }


    /**
     * 递归给子类赋值集合
     */
    private <E> void forEach(Map<String, List<E>> allNode,
                             E parentNode,
                             Function<E, String> nodeFunction,
                             BiConsumer<E, List<E>> childListFunction) {
        List<E> childNodeList = allNode.get(nodeFunction.apply(parentNode));
        if (CollectionUtils.isNotEmpty(childNodeList)) {
            //排序
            List<E> sortedTreeNodeList = childNodeList.stream()
                    .sorted(Comparator.comparing(nodeFunction)).collect(Collectors.toList());
            childListFunction.accept(parentNode, sortedTreeNodeList);
            sortedTreeNodeList.forEach(t -> forEach(allNode, t, nodeFunction, childListFunction));
        }
    }
}
