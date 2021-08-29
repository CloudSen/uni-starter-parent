package cn.uni.starter.jpa.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;
import java.util.Set;

/**
 * @author CloudS3n
 */
@Converter(autoApply = true)
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class BooleanYNConverter implements AttributeConverter<Boolean, String> {
    private static final Set<String> YES_VALUES = new HashSet<>(){{
        add("Y");
        add("y");
        add("true");
        add("TRUE");
    }};

    @Override
    public String convertToDatabaseColumn(Boolean objectValue) {
        if (objectValue == null) {
            return "N";
        } else {
            return objectValue ? "Y" : "N";
        }
    }

    @Override
    public Boolean convertToEntityAttribute(String dataValue) {
        return dataValue != null && YES_VALUES.contains(dataValue);
    }
}
