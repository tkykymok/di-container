package com.sample.di_container;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import com.sample.di_container.application.AdvertiseService;
import com.sample.di_container.application.product.ProductRepositoryImpl;
import com.sample.di_container.application.tweet.TwitterAdapterImpl;

/**
 * Hello world!
 *
 */
public class Application {
    public static void main(String[] args) {
        Context context = new Context();
        AdvertiseService advertiseService = context.getAdvertiseServiceBean();
        advertiseService.advertise(1, "販促メッセージ");
    }

    static class Context {

        public Context() {
            register();
        }

        static Map<Class, Object> objectPool = new HashMap<>();

        void register() {
            try {
                URL res = Context.class.getResource("/" + Context.class.getName().replace('.', '/') + ".class");
                // file:/C:/pleiades/workspace/di/target/classes/com/expample/di/Application$Context.class
                Path classPath = new File(res.toURI()).toPath().resolve("../../../../");
                // C:\pleiades\workspace\di\target\classes\com\expample\di\Application$Context.class\..\..\..\..
                // classesの直下
                Files.walk(classPath)
                        .filter(p -> !Files.isDirectory(p))
                        .filter(p -> p.toString().endsWith(".class"))
                        .map(classPath::relativize)
                        .map(p -> p.toString().replace(File.separatorChar, '.'))
                        .map(n -> n.substring(0, n.length() - 6))
                        .forEach(n -> {
                            try {
                                Class<?> aClass = Class.forName(n);
                                // さっき付けた@Namedのクラスをpoolする
                                if (aClass.isAnnotationPresent(Named.class)) {
                                    objectPool.put(aClass, aClass.newInstance());
                                }
                            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        <T> T getBean(final Class<T> clazz) {
            return (T) objectPool.get(clazz);
        }

        // オブジェクトプールしたものからdiする
        AdvertiseService getAdvertiseServiceBean() {
            return new AdvertiseService(getBean(ProductRepositoryImpl.class), getBean(TwitterAdapterImpl.class));
        }

    }
}
