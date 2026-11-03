package com.lrz.grovecore.utils;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

/**
 * 类查找器 在 <artifactId>springfox-swagger2</artifactId> 依赖下 目前移除这个依赖
 *
 * @author lirenzhi
 * @since 2023/8/15 11:13 AM
 */
public final class ClassFinder {

    private static ClassFinder classFinder = new ClassFinder();
    private static final String findPath = "com.lrz";

    public static ClassFinder getInstance() {
        return classFinder;
    }

    private ClassFinder() {
        this.reflections = new Reflections(findPath);
    }

    @Getter
    private Reflections reflections;

    /**
     * 找到所有继承指定类或实现指定接口的公共非接口子类
     *
     * @param superClassInterface 超类接口
     * @return {@link List}<{@link Class}<{@link ?} {@link extends} {@link T}>>
     */
    public <T> List<Class<? extends T>> findSubClass(Class<T> superClassInterface) {
        return findSubClass(superClassInterface, false, true);
    }

    public <T> List<Class<? extends T>> findSubClass(Class<T> superClassInterface, boolean containInterface, boolean containClass) {
        Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(superClassInterface);
        return filterPublic(subTypesOf, containInterface, containClass);
    }

    public <S, T extends Annotation> List<Class<S>> findClassByAnnotation(Class<T> type) {
        return findClassByAnnotation(type, false, true);
    }

    /**
     * 找到所有具有指定注解的的公共非接口子类
     *
     * @param type             类型
     * @param containInterface 包含接口
     * @param containClass     包含类
     * @return {@link List}<{@link Class}<{@link S}>>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <S, T extends Annotation> List<Class<S>> findClassByAnnotation(Class<T> type, boolean containInterface, boolean containClass) {
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(type, true);
        return (List) filterPublic(typesAnnotatedWith, containInterface, containClass);
    }

    private static <T> List<Class<? extends T>> filterPublic(Set<Class<? extends T>> classSet, boolean containInterface, boolean containClass) {
        List<Class<? extends T>> classList = Lists.newArrayList();
        for (Class<? extends T> aClass : classSet) {
            if (aClass.isInterface()) {
                if (!containInterface) {
                    continue;
                }
            } else {
                if (!containClass) {
                    continue;
                }
                int modifiers = aClass.getModifiers();
                if (!Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers)) {
                    continue;
                }
            }
            classList.add(aClass);
        }
        return classList;
    }
}
