package com.gao.nettyinaction.seri;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * User: wangchen
 * Date: 2016/12/11
 * Time: 23:16
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 6675826130359255400L;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户ID
     */
    private int userId;

    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo buildUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 默认的字节解析
     * @return
     */
    public byte[] codeC() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] bytes = this.userName.getBytes();
        byteBuffer.putInt(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.putInt(this.userId);

        byteBuffer.flip();
        bytes = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);

        return result;
    }
}
