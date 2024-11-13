package com.zjw.utils;

import com.zjw.entity.Repository;
import com.zjw.entity.RepositoryReadMe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SerializationUtil {

    public static final String File_DIR_PATH = System.getProperty("user.dir") + File.separator + "db"; // 获取当前项目的根目录
    private static final String PROJECTS_NAME = "projects.txt";
    private static File latestFile = null;

    /**
     * 保存List对象到文件
     */
    public static void saveListToFile(List<?> list){
        try {
            Path parentPath = Paths.get(File_DIR_PATH);
            Files.createDirectories(parentPath);
            String fileName = File_DIR_PATH + File.separator + "fetch-" + DateUtil.currentDate();
            latestFile = new File(fileName);
            try (ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(fileName))) {
                objectOut.writeObject(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件加载List对象
     */
    public static List<?> loadListFromFile(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            return (List<?>) objectIn.readObject();
        }
    }

    public static List<?> loadListFromFile() {
        List<?> loadData = new ArrayList<>();
        File file = null;
        try {
            file = findLatestFile(File_DIR_PATH);
            loadData = loadListFromFile(file);
            if (file == null) {
                return new ArrayList<>();
            }
        } catch (Exception e) {
        }
        return loadData;
    }

    /**
     * 把对象格式化为markdown文件
     */
    public static void saveToMarkdown(List<Repository> repositoryList) {
        saveToMarkdown(repositoryList, "README.md");
    }

    /**
     * 把对象格式化为markdown文件
     */
    public static void saveToMarkdown(List<Repository> repositoryList, String fileName) {
        List<RepositoryReadMe> repositoryReadMeList = parseRepositoryReadMe(repositoryList);
        String header = """
                # Project Information
                                
                fetch date：**%tF**
                                
                | Name                |         Release        |   Release Date    |    Github      |    Language      | description                                                                            |
                |---------------------|------------------------|-------------------|----------------|------------------|----------------------------------------------------------------------------------------|
                """;
        header = header.formatted(new Date());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(header);
            for (RepositoryReadMe readMe : repositoryReadMeList) {
                String formatted = String.format("|%s |%s| %s|%s|%s| <div style=\"width: 150pt\">%s</div> |",
                        readMe.getName(),
                        readMe.getVersion(),
                        readMe.getReleaseAt(),
                        readMe.getStar(),
                        readMe.getLanguage(),
                        readMe.getDescription()
                );
                writer.write(formatted);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("save to markdown failed :" + e.getMessage());
        }
    }

    /**
     * 解析Repository对象为RepositoryReadMe对象
     */
    private static List<RepositoryReadMe> parseRepositoryReadMe(List<Repository> repositoryList) {
        List<RepositoryReadMe> repositoryReadMeList = repositoryList.stream()
                .map(RepositoryReadMe::newInstance)
                .collect(Collectors.toList());
        return repositoryReadMeList;
    }

    /**
     * 查找最新文件
     */
    public static File findLatestFile(String folderPath) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        Date latestDate = null;
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith("fetch-") && fileName.length() == 14) {
                String dateString = fileName.substring(6); // 提取日期部分
                Date date = dateFormat.parse(dateString);
                if (latestDate == null || date.after(latestDate)) {
                    latestDate = date;
                    latestFile = file;
                }
            }
        }
        return latestFile;
    }

    /**
     * 删除非最新文件
     */
    public static void deleteHistoryFiles(){
        if (latestFile == null) {
            return;
        }
        File folder = new File(File_DIR_PATH);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (!file.equals(latestFile)) {
                file.delete();
            }
        }
    }

    /**
     * read projects from file
     */
    public static List<String> loadProjectsResource() {
        List<String> projectList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROJECTS_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                projectList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectList;
    }

    /**
     * sort projects and save to file
     */
    public static void sortProjectsFile(List<String> projects) {
        // 排序
        List<String> projectsCopy = new ArrayList<>(projects);
        projectsCopy.sort(Comparator.comparing(String::toLowerCase));
        if (!projectsCopy.equals(projects)) {
            // 保存到文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECTS_NAME))) {
                for (String project : projectsCopy) {
                    writer.write(project);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}