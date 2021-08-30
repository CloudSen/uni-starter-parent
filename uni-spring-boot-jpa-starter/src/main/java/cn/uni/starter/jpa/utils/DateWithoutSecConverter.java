//package cn.uni.starter.jpa.utils;
//
//import org.apache.commons.lang3.StringUtils;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author CloudS3n
// */
//@Converter
//public class DateWithoutSecConverter implements AttributeConverter<String, Date> {
//
//    @Override
//    public Date convertToDatabaseColumn(String objectValue) {
//        if (StringUtils.isBlank(objectValue)) {
//            return null;
//        }
//
//        try {
//            return new SimpleDateFormat("yyyy-MM-dd").parse(objectValue);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    public String convertToEntityAttribute(Date dateValue) {
//        if (dateValue == null) {
//            return "";
//        }
//        return new SimpleDateFormat("yyyy-MM-dd").format(dateValue);
//    }
//}
