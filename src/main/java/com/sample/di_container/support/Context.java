package com.sample.di_container.support;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

public class Context {

    public Context() {
        register();
    }

    static Map<Class, Object> objectPool = new HashMap<>();

    void register() {
        try {
            URL res = Context.class.getResource("/" + Context.class.getName().replace('.', '/') + ".class");
            // file:/C:/pleiades/workspace/di/target/classes/com/expample/di/Application$Context.class
            Path classPath = new File(res.toURI()).toPath().resolve("../../../../../");
            // C:\pleiades\workspace\di\target\classes\com\expample\di\Application$Context.class\..\..\..\..
            // classesの直下
            final List<? extends Class<?>> beanClasses = Files.walk(classPath)
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(".class"))
                    .map(classPath::relativize)
                    .map(p -> p.toString().replace(File.separatorChar, '.'))
                    .map(n -> n.substring(0, n.length() - 6))
                    .map(n -> {
                        try {
                            return Class.forName(n);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(aClass -> aClass.isAnnotationPresent(Named.class))
                    .collect(Collectors.toList());
            beanClasses.forEach(aClass -> registerBeans(beanClasses, aClass));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param オブジェクトプール
     * @param Namedアノテーションがついたクラス
     */
    private void registerBeans(final List<? extends Class<?>> beanClasses, final Class<?> aClass) {
        // publicのコンストラクタをすべて取得
        final List<Constructor<?>> declaredConstructors = Arrays.asList(aClass.getDeclaredConstructors());
        // コンストラクタに@Injecがついたクラス（今回の場合→AdvertiseService）
        final List<Constructor<?>> injectCandidates = declaredConstructors.stream()
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
        // インジェクトできるコンストラクタは一つに限定
        if (1 < injectCandidates.size()) {
            throw new RuntimeException("There are multiple inject constructors. " + injectCandidates);
        }

        if (!injectCandidates.isEmpty()) {
            Arrays.stream(injectCandidates.get(0).getParameterTypes()).map(parameterClazz -> {
                if (parameterClazz.isAnnotationPresent(Named.class)) {
                    return parameterClazz;
                }
                final List<Class> beanCandidates = beanClasses.stream()
                        .filter(parameterClazz::isAssignableFrom) // パラメータのインターフェースを実装しているクラスを抽出
                        .collect(Collectors.toList());
                // 実装したクラスは1つであること
                if (1 < beanCandidates.size()) {
                    throw new RuntimeException("There are multiple bean candidates. " + beanCandidates);
                }
                if (beanCandidates.isEmpty()) {
                    throw new RuntimeException("There are no bean candidates. " + parameterClazz);
                }
                return beanCandidates.get(0);
                // 実装クラスにDIされていないか再帰的に探しに行く
            }).forEach(p -> registerBeans(beanClasses, p));
        }
        // ここの処理でオブジェクトのプールを開始
        objectPool.computeIfAbsent(aClass, c -> {
            try {
                final Constructor<?> constructor = injectCandidates.isEmpty()
                        ? aClass.getDeclaredConstructor()
                        : injectCandidates.get(0);//チェックするためにlistになっている
                // コンストラクタがあるクラスが対象　
                final Object[] constructorParams = Arrays.stream(constructor.getParameterTypes())
                        .map(this::getBean)
                        .toArray(Object[]::new);
                // impl系のクラスはデフォルトコンストラクタでインスタンス生成
                return constructor.newInstance(constructorParams);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * objectPool.containsKey(clazz)は
     * 実装クラスで探す探す場合がtrue
     * インターフェースで探す場合がfalse
     * @param clazz
     * @return
     */
    public <T> T getBean(final Class<T> clazz) {
        final Object o = objectPool.containsKey(clazz)
                ? objectPool.get(clazz)
                : objectPool.entrySet()
                        .stream()
                        .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElseThrow(() -> new IllegalStateException("There are no bean of" + clazz));
        return (T) o;
    }
}