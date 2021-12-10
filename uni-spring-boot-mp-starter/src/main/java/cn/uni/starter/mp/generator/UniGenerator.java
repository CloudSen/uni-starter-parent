package cn.uni.starter.mp.generator;

import cn.uni.starter.autoconfigure.exception.UniException;
import cn.uni.starter.mp.BaseColumnDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.fill.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * 代码生成器二次封装
 * <p>父包名（默认） = cn.uni.项目名</p>
 * <p>DO包名 = 父包名.entity</p>
 * <p>mvc包名 = 父包名.模块名.业务域名.mvc层</p>
 *
 * @author clouds3n
 * @date 2021-12-10
 */
public class UniGenerator {

    /**
     * 生成代码
     *
     * @param property 生成器参数
     */
    public static void generate(GeneratorProperty property) {
        checkProperty(property);
        String parentPackage = property.getParentPackage();
        String packagePrefix = property.getPackagePrefix();
        String projectName = property.getProjectName();
        Scanner sc = new Scanner(System.in);
        System.out.println("> 请输入作者名称:");
        String author = Optional.ofNullable(sc.nextLine()).orElseThrow(() -> new UniException("作者名不能为空"));
        System.out.println("> 请输入业务模块名");
        String businessDomain = Optional.ofNullable(sc.nextLine()).orElseThrow(() -> new UniException("业务模块名不能为空"));
        sc.close();
        FastAutoGenerator.create(property.getDataSourceBuilder())
            // 全局配置
            .globalConfig((scanner, builder) -> builder.author(author))
            // 包配置
            .packageConfig((scanner, builder) -> builder.parent(StringUtils.isNotBlank(parentPackage) ? parentPackage : packagePrefix + projectName)
                .service("business." + businessDomain + ".service")
                .serviceImpl("business." + businessDomain + ".service.impl")
                .mapper("business." + businessDomain + ".mapper")
                .controller("web." + businessDomain)
            )
            // 策略配置
            .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("> 请输入表名，多个英文逗号分隔，所有输入 all:")))
                .controllerBuilder().enableRestStyle().enableHyphenStyle()
                .entityBuilder().enableLombok().enableChainModel()
                .enableRemoveIsPrefix().enableTableFieldAnnotation()
                .logicDeleteColumnName("deleted").versionColumnName("lockVersion")
                .superClass(BaseColumnDO.class)
                .addTableFills(
                    new Column("create_time", FieldFill.INSERT)
                ).build())
            .execute();
    }

    private static void checkProperty(GeneratorProperty property) {
        if (property.getDataSourceBuilder() == null) {
            throw new UniException("缺少数据库配置");
        }
        if (StringUtils.isBlank(property.getProjectName())) {
            throw new UniException("缺少项目名");
        }
    }

    /**
     * 获取表名
     *
     * @param tables 逗号分割的表名，可以为"all"
     * @return 表名列表，如果为"all"，则返回空列表
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratorProperty {
        private DataSourceConfig.Builder dataSourceBuilder;
        private String packagePrefix = "cn.uni.";
        private String projectName;
        private String parentPackage;
    }
}
