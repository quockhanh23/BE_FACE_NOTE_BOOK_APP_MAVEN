package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption.InvalidException;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.object.FieldsCheckWords;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Common {

    public static boolean checkRegex(String input, String regex) {
        if (StringUtils.isEmpty(input) || StringUtils.isEmpty(regex)) {
            return false;
        }
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void executionTime(double startTime, double elapsedTimeMillis) {
        log.info(Constants.MESSAGE_STRIKE_THROUGH);
        log.warn("Total execution time(ms): " + (elapsedTimeMillis - startTime));
        log.warn("Total execution time(s): " + (elapsedTimeMillis - startTime) / 1000);
        log.info(Constants.MESSAGE_STRIKE_THROUGH);
    }

    public static String formatToken(String authorization) {
        String tokenRequest;
        if (authorization.startsWith("Bearer ")) {
            tokenRequest = authorization.substring(7);
        } else {
            tokenRequest = authorization;
        }
        return tokenRequest;
    }

    public static void handlerWordsLanguage(Object ob) {
        FieldsCheckWords obTransfer = new FieldsCheckWords();
        BeanUtils.copyProperties(ob, obTransfer);
        try {
            Field[] fieldsOfFieldClass = FieldsCheckWords.class.getDeclaredFields();
            String[] dirtyWords = {"FUCK", "ĐỊT", "LỒN", "ĐMM", "ĐCM", "CU", "CẶC", "DÁI"};
            for (int i = 0; i < fieldsOfFieldClass.length; i++) {
                Field field = fieldsOfFieldClass[i];
                field.setAccessible(true);
                Object value = field.get(obTransfer);
                if (null == value) value = "";
                int check = handleWords(value.toString().toUpperCase(), dirtyWords);
                if (check == 1) throw new InvalidException(convertFieldName(field.getName()));
            }
        } catch (Exception e) {
            throw new InvalidException(e.getMessage());
        }
    }

    private static int handleWords(String value, String[] dirtyWords) {
        boolean containsDirtyWord = StringUtils.containsAny(value, dirtyWords);
        if (containsDirtyWord) return 1;
        return 0;
    }

    private static String convertFieldName(String field) {
        String content = " chứa những từ ngữ không phù hợp";
        if (field.equals(Constants.FieldsCheckWords.FIELD_USER_NAME)) {
            return "Tên đăng nhập" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_FULL_NAME)) {
            return "Tên đầy đủ" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_EMAIL)) {
            return "Email" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_ADDRESS)) {
            return "Địa chỉ" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_EDUCATION)) {
            return "Học vấn" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_FAVORITE)) {
            return "Sở thích" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_CONTENT)
                || field.equals(Constants.FieldsCheckWords.FIELD_DESCRIPTION)) {
            return "Nội dung" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_GROUP_NAME)) {
            return "Tên nhóm" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_WORK)) {
            return "Công việc" + content;
        }
        return field + content;
    }

    public static String addEscapeOnSpecialCharactersWhenSearch(String input) {
        if (StringUtils.isEmpty(input)) return input;
        if (input.contains("%") && !input.contains("\\%")) {
            input = input.replace("%", "\\%");
        }
        if (input.contains("_") && !input.contains("\\_")) {
            input = input.replace("_", "\\_");
        }
        return input;
    }

    public static Date changeLocalDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        Date date;
        try {
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public static String formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ObjectMapper intObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
