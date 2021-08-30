//package cn.uni.starter.jpa.utils;
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
//public class DateWithYearConverter implements AttributeConverter<String, Date> {
//
//    @Override
//    public Date convertToDatabaseColumn(String objectValue) {
//        if (objectValue == null || "".equals(objectValue)) {
//            return null;
//        }
//
//        try {
//            return new SimpleDateFormat("yyyy").parse(objectValue);
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
//        return new SimpleDateFormat("yyyy").format(dateValue);
//    }
//}
