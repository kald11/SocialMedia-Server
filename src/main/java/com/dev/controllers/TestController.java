package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.PostObject;
import com.dev.objects.UserObject;

import org.jcp.xml.dsig.internal.dom.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {

    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.getTokenByUsernameAndPassword(username, password);
        return token;
    }

    @RequestMapping("create-account")
    public String createAccount (String username, String password) {
        String token =null;
        boolean alreadyExists = persist.getTokenByUsernameAndPassword(username, password) != null;
        if (!alreadyExists) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            String hash =createHash(username, password);
            userObject.setToken(hash);
           if( persist.addAccount(userObject)){
               token=hash;
           }
        }

        return token;
    }



    @RequestMapping("add-post")
    public boolean addPost (String token, String content) {
        return persist.addPost(token, content);
    }


    @RequestMapping("get-posts")
    public List<PostObject> getPosts (String token) {
        return persist.getPostsByUser(token);
    }

    @RequestMapping("remove-post")
    public boolean removePost (String token, int postId) {
        return persist.removePost(token, postId);
    }

    private String createHash (String username, String password) {
        String myHash = null;
        try {
            String hash = "35454B055CC325EA1AF2126E27707052";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((username + password).getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return myHash;
    }
    private boolean haveSymbols(String s){
        return true;
    }





}
