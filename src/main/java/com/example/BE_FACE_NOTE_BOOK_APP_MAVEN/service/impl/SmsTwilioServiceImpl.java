//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;
//
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.SmsTwilioService;
//import com.twilio.Twilio;
//import com.twilio.base.ResourceSet;
//import com.twilio.rest.api.v2010.account.OutgoingCallerId;
//import com.twilio.rest.api.v2010.account.ValidationRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controllertest.SmsController.ACCOUNT_SID;
//import static com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controllertest.SmsController.AUTH_TOKEN;
//
//@Service
//public class SmsTwilioServiceImpl implements SmsTwilioService {
//
//    @Override
//    public void sendSMS() {
//
//    }
//
//    @Override
//    public void createNewPhoneNumberInTwilio(String phoneNumber) {
//        ValidationRequest.creator(new com.twilio.type.PhoneNumber(phoneNumber))
//                .setFriendlyName("My Home Phone Number")
//                .create();
//    }
//
//    @Override
//    public void deletePhoneNumberInTwilio(String phoneNumber) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        ResourceSet<OutgoingCallerId> outgoingCallerIds = OutgoingCallerId.reader().read();
//        String sid = "";
//        for (OutgoingCallerId record : outgoingCallerIds) {
//            if (record.getPhoneNumber().toString().equals(phoneNumber)) {
//                sid = record.getSid();
//                break;
//            }
//        }
//        OutgoingCallerId.deleter(sid).delete();
//    }
//
//    @Override
//    public List<String> listNumberPhoneTwilio() {
//        List<String> stringList = new ArrayList<>();
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        ResourceSet<OutgoingCallerId> outgoingCallerIds = OutgoingCallerId.reader().read();
//        for (OutgoingCallerId record : outgoingCallerIds) {
//            stringList.add(String.valueOf(record.getPhoneNumber()));
//        }
//        return stringList;
//    }
//}
