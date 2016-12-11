package com.gao.nettyinaction.seri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * User: wangchen
 * Date: 2016/12/11
 * Time: 23:26
 */
public class TestUserInfo {
    public static void main(String[] args) throws IOException {

        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("welcome to netty");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);

        outputStream.writeObject(userInfo);

        outputStream.flush();
        outputStream.close();

        byte[] bytes = stream.toByteArray();
        System.out.println("the jdk serizlizable length is : " + bytes.length);

        stream.close();
        System.out.println("------------------------------------");
        System.out.println("the byte array serializable length is : " + userInfo.codeC().length);

    }
}
