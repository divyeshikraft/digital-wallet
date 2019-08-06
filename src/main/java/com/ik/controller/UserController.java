package com.ik.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ik.model.Role;
import com.ik.model.User;
import com.ik.service.UserService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Value("${spring.checkTokenURL}")
	private String checkTokenURL;
    
    @RequestMapping(value="/users", method = RequestMethod.GET)
    public List<User> listUser(){
        return userService.findAll();
    }
    
    Logger logger = Logger.getLogger(UserController.class);

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String create(@RequestBody User user){
    	logger.info("Enter into registration() mehtod");
    	logger.debug("Password ::"+user.getPassword());
    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    	Role person = userService.findOne(2);
    	List<Role> roles = new ArrayList<Role>();
    	roles.add(person);
    	user.setRoles(roles);
    	userService.save(user);
    	return "create user successfully";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") Long id){
        userService.delete(id);
        return "success";
    }
    
    @RequestMapping(value="/user/me", method = RequestMethod.GET)
    public User getUserInfo(@RequestHeader("Authorization") String token){
    	logger.info("Enter into getUserInfo() method");
    	logger.info("Authorization ::"+token);
    	token = token.replace("Bearer ", "");
    	logger.debug("Token ::"+token);
    	try {
    		OkHttpClient client = new OkHttpClient();

    		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "token="+token);
    		Request request = new Request.Builder()
    		  .url(checkTokenURL)
    		  .post(body)
    		  .addHeader("content-type", "application/x-www-form-urlencoded")
    		  .addHeader("cache-control", "no-cache")
    		  .build();

    		Response response = client.newCall(request).execute();
    		
    		Object obj=JSONValue.parse(response.body().string());  
    	    JSONObject jsonObject = (JSONObject) obj;
    	    logger.info("user_name :"+String.valueOf(jsonObject.get("user_name")));
    	    User user = userService.getUser(String.valueOf(jsonObject.get("user_name")));
			
    	    logger.info("User Email ::"+user.getEmail());
    	    
    	    return user;
		} catch (IOException e) {
			logger.error("IOException ::"+e);
		} 
    	return null;
    }
    
    
    
/*    @RequestMapping("/user/me")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }*/

	public BCryptPasswordEncoder getbCryptPasswordEncoder() {
		return bCryptPasswordEncoder;
	}

	public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

}
