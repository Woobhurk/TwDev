package com.tyfanch.dynamicjs.executor;

import java.lang.reflect.Method;
import java.util.Map;
import com.tyfanch.dynamicjs.model.MethodConfig;
import com.tyfanch.dynamicjs.model.ScriptConfig;
import com.tyfanch.dynamicjs.parser.DefaultScriptParser;
import com.tyfanch.dynamicjs.parser.ScriptParser;
import com.tyfanch.dynamicjs.utils.JavaScriptUtils;

/**
 * 默认的脚本执行类
 */
public class DefaultScriptExecutor implements ScriptExecutor {
    private ScriptConfig scriptConfig;
    private ScriptParser scriptParser = new DefaultScriptParser();

    /**
     * 必须传入Script配置
     * @param scriptConfig Script配置
     */
    public DefaultScriptExecutor(ScriptConfig scriptConfig) {
        this.scriptConfig = scriptConfig;
    }

    @Override
    public Object execute(Method method, Object... args) throws Exception {
        String compiledScript;
        Object result;

        this.initEngine(method);
        compiledScript = this.compileScript(method, args);
        result = this.executeScript(compiledScript);

        return result;
    }

    /**
     * 初始化引擎，根据方法指定的引擎来给后续执行
     * @param method 要执行的方法
     */
    private void initEngine(Method method) {
        Map<String, MethodConfig> methodConfigMap = this.scriptConfig.getMethods();
        MethodConfig methodConfig = methodConfigMap.get(method.getName());
        String engine;

        if (methodConfig != null) {
            engine = methodConfig.getEngine();
            JavaScriptUtils.withEngine(engine);
        } else {
            String errorMsg = String.format(
                "Method `%s` in `%s` has no implementation in configuration",
                method.getName(), this.scriptConfig.getNamespace());

            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * 编译脚本
     * @param method 要执行的方法
     * @param args 传入的参数
     * @return 编译后的脚本
     */
    private String compileScript(Method method, Object... args) {
        Map<String, MethodConfig> methodConfigMap = this.scriptConfig.getMethods();
        MethodConfig methodConfig = methodConfigMap.get(method.getName());
        String script = methodConfig.getScript();
        String compiledScript;

        compiledScript = this.scriptParser.compile(method, script, args);

        return compiledScript;
    }

    /**
     * 执行脚本
     * @param script 脚本
     * @return 执行结果
     * @throws Exception 异常
     */
    private Object executeScript(String script) throws Exception {
        Object result;

        result = JavaScriptUtils.eval(script);

        return result;
    }
}
