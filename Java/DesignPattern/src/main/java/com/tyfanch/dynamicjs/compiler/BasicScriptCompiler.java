package com.tyfanch.dynamicjs.compiler;

/**
 * 简易脚本编译类，使用String.format来格式化
 */
public class BasicScriptCompiler implements ScriptCompiler {
    @Override
    public String compile(String script, Object... args) {
        String compiledScript;

        // 只用来格式化
        compiledScript = String.format(script, args);

        return compiledScript;
    }
}
