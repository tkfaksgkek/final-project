package com.icia.sdsd.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CMService {

    @Value("${coolsms.api.key}")
    private String COOLSMS_API_ID;

    @Value("${coolsms.api.secret}")
    private String COOLSMS_API_SECRET;

    public void phoneCheck(String smPhone, int randomNumber) {
        Message coolsms = new Message(COOLSMS_API_ID, COOLSMS_API_SECRET);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", smPhone); // 수신전화번호
        params.put("from", "본인휴대폰번호"); // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "SDSD에서 요청하신 인증번호는" + "[" + randomNumber + "]" + "입니다."); // 문자 내용 입력
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    public void phoneCheck2(String soPhone, int randomNumber) {
        Message coolsms = new Message(COOLSMS_API_ID, COOLSMS_API_SECRET);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", soPhone); // 수신전화번호
        params.put("from", "본인휴대폰번호"); // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "인증번호는" + "[" + randomNumber + "]" + "입니다."); // 문자 내용 입력
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
