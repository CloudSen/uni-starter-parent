package cn.uni.starter.autoconfigure.log4j2;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author CloudS3n
 * @date 2021-06-30 16:53
 */
@SuppressWarnings("rawtypes")
public class Log4j2Initializer {

    public static void initialize() {
        Yaml yaml = new Yaml();
        InputStream inputStream = Log4j2Initializer.class.getClassLoader()
            .getResourceAsStream("bootstrap.yml");
        if (inputStream == null) {
            inputStream = Log4j2Initializer.class.getClassLoader()
                .getResourceAsStream("application.yml");
        }
        if (inputStream == null) {
            throw new RuntimeException("请添加bootstrap.yml或application.yml");
        }
        Map<String, Object> load = yaml.load(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("读取配置文件发生异常", e);
        }
        System.setProperty("spring.application.name", String.valueOf(((Map) ((Map) load.get("spring")).get("application")).get("name")));
    }
}
