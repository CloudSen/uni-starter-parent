package cn.uni.starter.jpa.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CloudS3n
 */
@Converter(autoApply = true)
public class StringDateWithYear implements AttributeConverter<String, Date> {

    @Override
    public Date convertToDatabaseColumn(String objectValue) {
        if (objectValue == null || "".equals(objectValue)) {
            return null;
        }

        try {
            Date date = new SimpleDateFormat("yyyy").parse(objectValue);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String convertToEntityAttribute(Date dateValue) {
        if (dateValue == null || "".equals(dateValue)) {
            return "";
        }
        return new SimpleDateFormat("yyyy").format(dateValue);
    }
}
