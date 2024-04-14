package com.zjw;

/**
 * @author 朱俊伟
 * @since 2024/04/14 06:04
 */
public class TokenTest {
    public static void main(String[] args) {
        String token = "Bearer " + System.getenv("GITHUB_TOKEN");
        System.out.println(token);
    }
}
