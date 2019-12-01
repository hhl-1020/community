package com.baizhi.provider;

import com.alibaba.fastjson.JSON;
import com.baizhi.entity.AccessTokenDTO;
import com.baizhi.entity.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json;charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String s = response.body().string();
            String[] split1 = s.split("&")[0].split("=");
            return split1[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getGithubUser(String accessToken) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String string = response.body().string();
            return JSON.parseObject(string, GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
//599e4c56e785847c1fc85f8211b33623e2347394
//https://github.com/login/oauth/access_token
//https://api.github.com/user?access_token=599e4c56e785847c1fc85f8211b33623e2347394