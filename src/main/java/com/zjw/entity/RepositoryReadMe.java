package com.zjw.entity;

import lombok.Getter;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 朱俊伟
 * @since 2024/04/13 17:48
 */
public class RepositoryReadMe {

    private static Repository repository;
    private static RepositoryReadMe readMe;
    @Getter
    private String name;
    @Getter
    private String version;
    @Getter
    private String star;
    @Getter
    private String releaseAt;
    @Getter
    private String description;
    @Getter
    private String fetchDate;
    @Getter
    private String language;


    private RepositoryReadMe() {
    }

    public static RepositoryReadMe newInstance(Repository repository) {
        RepositoryReadMe.repository = repository;
        readMe = new RepositoryReadMe();
        return readMe.name().version().star().releaseAt().description().fetchDate().language();
    }

    private RepositoryReadMe name() {
        String homePageUrl = getValue(repository.getHomepage()).trim();
        String htmlUrl = getValue(repository.getHtmlUrl()).trim();
        String url = "".equals(homePageUrl) ? htmlUrl : homePageUrl;
        readMe.name = "[" + getValue(repository.getName()) + "](" + url + ")";
        return readMe;
    }

    private RepositoryReadMe fetchDate() {
        readMe.fetchDate = String.format("%tF", repository.getFetchDate());
        return readMe;
    }

    private RepositoryReadMe version() {
        if (repository.getReleaseAt() == null) {
            readMe.version = "[![GitHub Tag](https://img.shields.io/github/v/tag/"+ repository.getFullName() +"?sort=date)](" + getValue(repository.getReleaseUrl()) + ")";
        } else {
            String versionName = getValue(repository.getName()).replace("-", "--");
            String version = strandVersion(getValue(repository.getLatestRelease())).replace("-", "--");
            readMe.version = "[![](https://img.shields.io/badge/" + versionName + "-" + version + "-green.svg)](" + getValue(repository.getReleaseUrl()) + ")";
        }
        return readMe;
    }

    private static String strandVersion(String version) {
        String strandVersion = "";
        Pattern pattern = Pattern.compile("\\b(?:v)?(\\d+\\.\\d+(\\.\\d+)?(?:-[a-zA-Z0-9]+)?)\\b");
        Matcher matcher = pattern.matcher(version);
        // 提取并输出版本号
        while (matcher.find()) {
            strandVersion = matcher.group(1);
        }
        if (strandVersion.isEmpty()) {
            strandVersion = version;
        }
        return strandVersion;
    }

    private RepositoryReadMe star() {
        readMe.star = "[![GitHub stars](https://img.shields.io/github/stars/" + repository.getFullName()+")](https://github.com/" + repository.getFullName() + ")";
        return readMe;
    }

    private RepositoryReadMe releaseAt() {
        Date releaseOrUpdate = repository.getReleaseAt() == null ? repository.getPushedAt() : repository.getReleaseAt();
        readMe.releaseAt = releaseOrUpdate == null ? "" : String.format("%tF", releaseOrUpdate);
        return readMe;
    }

    private RepositoryReadMe description() {
        readMe.description = getValue(repository.getDescription());
        return readMe;
    }

    private RepositoryReadMe language() {
        readMe.language = getValue(repository.getLanguage());
        return readMe;
    }

    private String getValue(String value) {
        return value == null ? "" : value;
    }

}
