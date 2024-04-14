package com.zjw;

import com.zjw.entity.Repository;
import com.zjw.entity.RepositoryCreate;
import com.zjw.utils.DateUtil;
import com.zjw.utils.SerializationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GitHub project fetcher, save fetched projects to db file and markdown file,
 * need set GITHUB_TOKEN in environment variable.
 *
 * @author zjw
 * @version 1.0.0
 */
public class ProjectFetcher {

    public static void main(String[] args) {

        List<String> projectList = SerializationUtil.loadProjectsResource();
        if (projectList.isEmpty()) {
            return;
        }

        Map<String, Repository> repositoryMap = mapProjects(projectList);
        List<Repository> repositories = new ArrayList<>(repositoryMap.values());
        repositories.sort(Comparator.comparing(o -> o.getName().toLowerCase()));

        SerializationUtil.saveListToFile(repositories);
        SerializationUtil.saveToMarkdown(repositories);
        SerializationUtil.deleteHistoryFiles();
        SerializationUtil.sortProjectsFile(projectList);
    }

    /**
     * load history projects and re-map them, key is project full name, value is Repository object
     */
    @SuppressWarnings("all")
    public static Map<String, Repository> mapProjects(List<String> projectList) {
        List<Repository> loadedRepositories = (List<Repository>) SerializationUtil.loadListFromFile();
        Map<String, Repository> repositoryMap = loadedRepositories.stream()
                .filter(repository -> projectList.contains(repository.getFullName()))
                .collect(Collectors.toMap(
                        Repository::getFullName,
                        repository -> repository
                ));
        RepositoryCreate repositoryCreate = new RepositoryCreate();
        for (String project : projectList) {
            // today has been fetched
            if (repositoryMap.containsKey(project) && DateUtil.currentDate()
                    .equals(DateUtil.dateToStr(repositoryMap.get(project).getFetchDate()))) {
                continue;
            }
            Repository repository = repositoryCreate.createRepository(project);
            repositoryMap.put(project, repository);
        }
        return repositoryMap;
    }



}