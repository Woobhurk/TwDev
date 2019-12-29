package com.tyfanch.dynamicjs.oldengine.config;

import com.tyfanch.dynamicjs.oldengine.model.ScriptConfig;
import com.tyfanch.dynamicjs.oldengine.parser.ClassConfigParser;
import com.tyfanch.dynamicjs.oldengine.parser.ConfigParser;
import com.tyfanch.dynamicjs.oldengine.parser.NamespaceConfigParser;

/**
 * 解析Script配置文件
 */
public class ScriptConfigFactory {
    private static ConfigParser configParser;

    /**
     * 从类中读取配置
     *
     * @param configClass 配置类
     * @return Script配置
     */
    public static ScriptConfig readFromClass(Class<?> configClass) {
        ScriptConfig scriptConfig;

        if (ScriptConfigManager.exists(configClass)) {
            scriptConfig = ScriptConfigManager.get(configClass);
        } else {
            configParser = new ClassConfigParser(configClass);
            scriptConfig = configParser.parseScriptConfig();
            ScriptConfigManager.add(scriptConfig);
        }

        return scriptConfig;
    }

    /**
     * 指定一个类，从类的路径中找到配置文件，并读取Script配置
     * 例如：com.tyfanch.Abc则会自动找到/com/tyfanch/Abc.json来读取配置
     *
     * @param tClass 类
     * @return Script配置
     */
    public static ScriptConfig readFromNamespace(Class<?> tClass) {
        ScriptConfig scriptConfig;

        if (ScriptConfigManager.exists(tClass)) {
            scriptConfig = ScriptConfigManager.get(tClass);
        } else {
            configParser = new NamespaceConfigParser(tClass);
            scriptConfig = configParser.parseScriptConfig();
            ScriptConfigManager.add(scriptConfig);
        }

        return scriptConfig;
    }

    /**
     * 指定一个命名空间，从命名空间对应的路径中找到配置文件，并读取Script配置
     * 例如：com.tyfanch.Abc则会自动找到/com/tyfanch/Abc.json来读取配置
     *
     * @param namespace 命名空间
     * @return Script配置
     */
    public static ScriptConfig readFromNamespace(String namespace) {
        ScriptConfig scriptConfig;

        if (ScriptConfigManager.exists(namespace)) {
            scriptConfig = ScriptConfigManager.get(namespace);
        } else {
            configParser = new NamespaceConfigParser(namespace);
            scriptConfig = configParser.parseScriptConfig();
            ScriptConfigManager.add(scriptConfig);
        }

        return scriptConfig;
    }

    ///**
    // * 从文件中读取
    // *
    // * @param configFile 文件路径
    // * @return Script配置
    // */
    //public static ScriptConfig readFromFile(String configFile) {
    //    ScriptConfig scriptConfig;
    //
    //    if (ScriptConfigManager.exists(configFile)) {
    //        scriptConfig = ScriptConfigManager.get(configFile);
    //    } else {
    //        configParser = new FileConfigParser(configFile);
    //        scriptConfig = configParser.parseScriptConfig();
    //    }
    //
    //    return scriptConfig;
    //}
}
