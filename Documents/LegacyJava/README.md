# FPT Librarian Module - API part (Legacy)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

# Basic Design

-   We expose REST API at web server side so that multiple client can consume our's API.
-   Advantage of this design is that can scale to large size of our project grow up.
-   Disavantage the complexcity when implement is higher than monolothich application.

# Details Design

| package                  | Responsibility                                                                |
| ------------------------ | ----------------------------------------------------------------------------- |
| com.fpt.edu.controllers  | Store all of controller                                                       |
| com.fpt.edu.services     | contain all of services class that help us do our business logic              |
| com.fpt.edu.repositories | contain all of our repository (DAO) that allow us to manipulate with Database |
| com.fpt.edu.entities     | contain all of our entity or Domain object                                    |
| _.config, _.common       | Contain Spring config and some common function                                |
| com.fpt.edu.app          | contain application app that will help to start the server                    |

# Development Guide

## 1. Defing a controller

![](https://i.ibb.co/yFLxJts/contrroller.png)

1. Anotation to define this java class will be a controller. So that client can request to this class method if do the request mapping.
2. Define Swagger docs, using @ApiOperation and @ApiResponses to declare the information about the. If you start serser then you can see the swagger docs at [SWAGGER_UI](http://localhost:8080/swagger-ui.html) (note that start your local server before you click on this link, Start server by run Application.java)
3. Define and do mapping: Using anotation @RequestMapping mean that you tell spring that hey Spring I declarce a url that can do some thine awesome please expose it to client can access to it. For example:

    ```
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
    ```

    This will map the /test path to current running web app with method GET and return type is application/json. You can test it by run postman via this link : http://localhost:9090/users/test

![](https://i.ibb.co/9qwJ9YS/postman-test.png)
