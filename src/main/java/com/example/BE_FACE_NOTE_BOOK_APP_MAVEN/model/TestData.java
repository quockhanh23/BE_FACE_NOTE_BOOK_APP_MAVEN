//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;
//
//import com.alibaba.excel.annotation.ExcelIgnore;
//import com.alibaba.excel.annotation.ExcelProperty;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.apache.commons.lang3.RandomStringUtils;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//@Entity
//@Data
//@NoArgsConstructor
//public class TestData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @ExcelIgnore
//    private Long id;
//    @ExcelProperty("First Name")
//    private String firstName = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Last Name")
//    private String lastName = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Address")
//    private String address = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Education")
//    private String education = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Phone")
//    private String phone = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Country")
//    private String country = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Religion")
//    private String religion = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("License")
//    private String license = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Vaccination")
//    private String vaccination = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Passport")
//    private String passport = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 1")
//    private String testField1 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 2")
//    private String testField2 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 3")
//    private String testField3 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 4")
//    private String testField4 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 5")
//    private String testField5 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 6")
//    private String testField6 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 7")
//    private String testField7 = RandomStringUtils.randomAscii(30);
//    @ExcelProperty("Test Field 8")
//    private String testField8 = RandomStringUtils.randomAscii(30);
//
//    public TestData(String firstName, String lastName, String address, String education, String phone, String country,
//                    String religion, String license) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.address = address;
//        this.education = education;
//        this.phone = phone;
//        this.country = country;
//        this.religion = religion;
//        this.license = license;
//    }
//}
