/**
 * @name: User.java
 * @copyright: soyea©2018
 * @description:
 * @author: ywm
 * @createtime: 2018/6/22 12:52
 */

package cn.com.soyea.amq.consumer.auth.entity;

import java.io.Serializable;

/**
 * @name User
 * @description
 * @auther ywm
 * @see
 * @since
 */
public class User implements Serializable {

    private Long id;
    private String name;
    private String password;
    private String groups;
    private String writeTopics;
    private String readTopics;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getWriteTopics() {
        return writeTopics;
    }

    public void setWriteTopics(String writeTopics) {
        this.writeTopics = writeTopics;
    }

    public String getReadTopics() {
        return readTopics;
    }

    public void setReadTopics(String readTopics) {
        this.readTopics = readTopics;
    }
}
