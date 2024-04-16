package com.zjw.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zjw.utils.HttpUtil;

import java.util.Date;
import java.util.logging.Logger;

/**
 * @author 朱俊伟
 * @since 2024/04/13 08:28
 */
public class RepositoryCreate {

    Logger logger = Logger.getLogger(RepositoryCreate.class.getName());

    public Repository createRepository(String fullName) {
        Repository repository = new Repository(fullName);
        initRepository(repository);
        return repository;
    }

    private void initRepository(Repository repository) {
        try {
            String json = HttpUtil.get("https://api.github.com/repos/" + repository.getFullName());
            JSONObject jsonObject = JSON.parseObject(json);
            repository.setId(jsonObject.getLong("id"));
            repository.setName(jsonObject.getString("name"));
            repository.setFullName(jsonObject.getString("full_name"));
            repository.setHtmlUrl(jsonObject.getString("html_url"));
            repository.setHomepage(jsonObject.getString("homepage"));
            repository.setDescription(jsonObject.getString("description"));
            repository.setCreatedAt(jsonObject.getDate("created_at"));
            repository.setUpdatedAt(jsonObject.getDate("updated_at"));
            repository.setPushedAt(jsonObject.getDate("pushed_at"));
            repository.setStarCount(jsonObject.getIntValue("stargazers_count"));
            repository.setForkCount(jsonObject.getIntValue("forks_count"));
            repository.setFetchDate(new Date());
            repository.setLanguage(jsonObject.getString("language"));
            // 查找最新版本
            release(repository);
        } catch (Exception e) {

        }
    }

    /**
     * 最新版本
     */
    private void release(Repository repository) {
        String latestRelease = null;
        try {
            String result = HttpUtil.get("https://api.github.com/repos/" + repository.getFullName() + "/releases");
            // 有release
            if (result != null && result.length() > 2) {
                // 过滤掉预览版
                JSONArray jsonArray = JSON.parseArray(result);
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (jsonObject.getBoolean("prerelease")) {
                        continue;
                    } else {
                        release(repository, jsonObject);
                        break;
                    }
                }
            } else {
                // 没有release，尝试查找tag
                result = HttpUtil.get("https://api.github.com/repos/" + repository.getFullName() + "/tags");
                if (result != null && result.length() > 2) {
                    // 过滤掉预览版
                    JSONArray jsonArray = JSON.parseArray(result);
                    release(repository, (JSONObject) jsonArray.get(0));
                }
            }
        } catch (Exception e) {
        }
    }

    private void release(Repository repository, JSONObject jsonObject) {
        repository.setReleaseAt(jsonObject.getDate("published_at"));

        if (jsonObject.getString("tag_name") != null) {
            repository.setLatestRelease(jsonObject.getString("tag_name"));
            repository.setReleaseUrl("https://github.com/" + repository.getFullName() + "/releases");
        } else if (jsonObject.getString("name") != null) {
            repository.setLatestRelease(jsonObject.getString("name"));
            repository.setReleaseUrl("https://github.com/" + repository.getFullName() + "/tags");
        }
        // 没找到
    }
}
