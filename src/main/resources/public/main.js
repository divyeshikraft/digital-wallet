$(function() {
    console.log('page load');


    var user = '';
    if ("token" in localStorage) {
        console.log('token found');
        var tokenJson = JSON.parse(localStorage.getItem('token'));
        var token = tokenJson.access_token;

        console.log('token ::' + token);

        var settings = {
            "async": true,
            "crossDomain": true,
            "url": "http://localhost:8082/oauth/check_token?token=" + token,
            "method": "POST",
            "headers": {
                "cache-control": "no-cache",
            }
        }

        $.ajax(settings).done(function(response) {
            console.log("valid token token ::" + response.user_name);
            user = response.user_name;
            $('span#name').append(user);
            authenticatedUser();

        }).fail(function(data, textStatus, xhr) {
            console.log("error JSON ::", data.responseText);
            preLogin();
        }).always(function() {
            console.log("ended");
        });



    } else {
        console.log('token not found');
        preLogin();
    }


    //login form
    $('#loginFrm').on('submit', function(e) {

        console.log('loginFrm form submit ::' + $('input#username').val());
        e.preventDefault();
        var settings = {
            "async": true,
            "crossDomain": true,
            "url": "http://localhost:8082/oauth/token",
            "method": "POST",
            "headers": {
                "content-type": "application/x-www-form-urlencoded",
                "authorization": "Basic " + btoa('Inexture' + ":" + 'inexture@123'),
                "cache-control": "no-cache"
            },
            "data": {
                "username": $('input#username').val(),
                "password": $('input#pwd').val(),
                "grant_type": "password"
            }
        }

        $.ajax(settings).done(function(response) {
            console.log('user found ::' + response);
            localStorage.setItem('token', JSON.stringify(response));
            authenticatedUser();
            $('span#name').empty();
            $('span#name').append(user);
            
            
            var settings1 = {
            		  "async": true,
            		  "crossDomain": true,
            		  "url": "http://localhost:8082/user/me",
            		  "method": "GET",
            		  "headers": {
            		    "authorization": "Bearer "+token,
            		    "cache-control": "no-cache",
            		  }
            		}

            		$.ajax(settings1).done(function (response) {
            		  console.log(response);
            		  
            		  var data = "<tr><th>Firstname</th><th>Lastname</th><th>Email</th></tr><tr>"+
            		  "<td>" + response.firstName + "<td>" +
            		  "<td>" + response.lastName + "<td>" +
            		  "<td>" + response.email + "<td></tr>";
            		  
            		  $('#userInfoTbl').empty();
                      $('#userInfoTbl').append(data);
            		 
                      $('#allUserInfoTbl').hide();
                      if(response.role[0].name == 'ADMIN') {
                    	  $('#allUserInfoTbl').show();
                    	  data = '';
                    	  
                    	  var settings2 = {
                        		  "async": true,
                        		  "crossDomain": true,
                        		  "url": "http://localhost:8082/user/me",
                        		  "method": "GET",
                        		  "headers": {
                        			"authorization": "Bearer "+token,
                        		    "cache-control": "no-cache",
                        		  }
                        		}

                        		$.ajax(settings2).done(function (response) {
                        		  console.log(response);
                        		  
                        		  var data = "<tr><th>Firstname</th><th>Lastname</th><th>Email</th></tr>";
                                  
                                  response.forEach(function(entry) {
                                	  data= data+ "<tr><td>" + response.firstName + "<td>" +
                            		  "<td>" + response.lastName + "<td>" +
                            		  "<td>" + response.email + "<td></tr>";
                                	});
                                  
                                  $('#allUserInfoTbl').empty();
                                  $('#allUserInfoTbl').append(data)
                        		
                        		});
                      }
            		});
            
            

        }).fail(function(data, textStatus, xhr) {
            console.log("error JSON ::", data.responseText);
            localStorage.removeItem('token');
            $('div#invalidMsg').show();

        }).always(function() {
            //TO-DO after fail/done request.
            console.log("ended");
        });

    });

    $('#registrationContainer').on('submit', function(e) {
        console.log('registrationContainer form submit ::' + $('input#username').val());
        e.preventDefault();
        
        var userDate = {
                "firstName": $('input#fname').val(),
                "lastName": $('input#lname').val(),
                "username": $('input#uname').val(),
                "password": $('input#newPassword').val(),
                "email": $('input#email').val()
            };
        
        console.log('userDate ::' + userDate);
        console.log('userDate ::' + JSON.stringify(userDate));
        
        var settings = {
                "async": true,
                "crossDomain": true,
                "url": "http://localhost:8082/registration",
                "method": "POST",
                "headers": {
                    "content-type": "application/json",
                    "cache-control": "no-cache",
                },
                "processData": false,
                "data": JSON.stringify(userDate)
            };

            $.ajax(settings).done(function(response) {
                console.log(response);
                authenticatedUser();
                $('#successMsg').show();
            });

    });
    
});

function logout() {
    console.log('logout function call');
    localStorage.removeItem('token');
    preLogin();
    $('.unauth').show();
    $('.auth').hide();
}

function preRegistration() {
    console.log('preRegistration function call');
    $('li.registration').addClass('active');
    $('li.login').removeClass('active');
    $('#loginContainer').hide();
    $('#registrationContainer').show();
}

function preLogin() {
    console.log('preLogin function call');
    $('li.login').addClass('active');
    $('li.registration').removeClass('active');
    $('#loginContainer').show();
    $('#registrationContainer').hide();
    $('div#invalidMsg').hide();
}

function authenticatedUser() {
    console.log('authenticatedUser function call');
    $('#loginContainer').hide();
    $('#registrationContainer').hide();
    $('.auth').show();
    $('.unauth').hide();
    
    
}