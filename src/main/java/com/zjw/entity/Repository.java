package com.zjw.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Repository implements Serializable {
    
    public Repository(String fullName){
        this.fullName = fullName;
    }

    /**
     * 仓库id
     */
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 完整名字
     */
    private String fullName;

    /**
     * 仓库地址
     */
    private String htmlUrl;

    /**
     * 主页
     */
    private String homepage;

    /**
     * 描述
     */
    private String description;

    /**
     * 最新版本
     * https://api.github.com/repos/mybatis/mybatis-3/releases/{id}
     * https://api.github.com/repos/mybatis/mybatis-3/releases?per_page=1
     *
     */
    private String latestRelease;

    /**
     * release下载地址
     */
    private String releaseUrl;

    /**
     * 发布时间
     */
    private Date ReleaseAt;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 星标数
     */
    private Integer starCount;

    /**
     * fork数
     */
    private Integer forkCount;

    /**
     * 抓取时间
     */
    private Date fetchDate;

    /**
     * 语言
     */
    private String language;

}