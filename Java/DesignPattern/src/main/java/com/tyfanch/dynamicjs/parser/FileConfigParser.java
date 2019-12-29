package com.tyfanch.dynamicjs.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.tyfanch.dynamicjs.config.ScriptConfigFactory;
import com.tyfanch.dynamicjs.model.ScriptConfig;
import com.tyfanch.dynamicjs.utils.JsonUtils;

/**
 * 通过文件解析Script配置
 */
public class FileConfigParser implements ConfigParser {
    private String configFile;
    private ScriptConfig scriptConfig;

    /**
     * 必须指定文件名
     *
     * @param configFile 配置文件名
     */
    public FileConfigParser(String configFile) {
        this.configFile = configFile;
    }

    @Override
    public ScriptConfig parseScriptConfig() {
        InputStream inputStream = ScriptConfigFactory.class.getResourceAsStream(
            this.configFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String configFileContent;

        reader.lines().forEach(s -> stringBuilder.append(s).append("\n"));
        configFileContent = stringBuilder.toString();
        this.scriptConfig = JsonUtils.fromJson(configFileContent, ScriptConfig.class);

        return this.scriptConfig;
    }
}
