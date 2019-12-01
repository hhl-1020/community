package com.baizhi.controller;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.AccessTokenDTO;
import com.baizhi.entity.GithubUser;
import com.baizhi.entity.User;
import com.baizhi.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserDao userDao;

    @Value("${github.client.clientId}")
    private String clientId;
    @Value("${github.client.redirectUri}")
    private String redirectUri;
    @Value("${github.client.secret}")
    private String secret;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(secret);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser githubUser = githubProvider.getGithubUser(accessToken);

        if (githubUser != null) {
            //登录成功
            request.getSession().setAttribute("user", githubUser);
            User user = new User();
            String token = UUID.randomUUID().toString();

            user.setToken(token)
                    .setLogin(githubUser.getLogin())
                    .setAccountId(String.valueOf(githubUser.getId()))
                    .setGmtCreate(System.currentTimeMillis())
                    .setGmtModified(user.getGmtCreate());

            userDao.insertUser(user);
            response.addCookie(new Cookie("token", token));

            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }
    }
}
