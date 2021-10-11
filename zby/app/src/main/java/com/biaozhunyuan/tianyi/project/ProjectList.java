package com.biaozhunyuan.tianyi.project;

import java.io.Serializable;
import java.util.List;

/**
 * 用于页面传递数据
 */

public class ProjectList implements Serializable {

    private List<Project> users;

    public List<Project> getProjects() {
        return users;
    }

    public void setProjects(List<Project> users) {
        this.users = users;
    }
}
