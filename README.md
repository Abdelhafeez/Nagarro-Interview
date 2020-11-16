# Nagarro Interview Challenge

![nagarro](https://upload.wikimedia.org/wikipedia/en/c/c0/Nagarro_logo_new.svg)


This assignment is a simple application focused on the interaction with REST API

These apis offer a basic user-management functionality, where user can login through an api /login 
and then can use the /api/<resources> by specifing the returned Bearer token in authorization header of request

There are three different roles:
* Admin: Can not filter using parameters (amount and dates)
* User: has permissions to filter using above parameters

* consumes REST resources `/api/fetch`, `/api/auth/login` and `/api/auth/logout`

This server side is implemented with Spring boot
Server implementation features the following:
*  JWT authentication with spring-boot, using grant types as password `password` (Refresh token is disabled)
* Customized In memory token vault to add statefull requirments (logout, single user sign at a time)


#  Demo Credentials

  * Admin User
    username : testadmin
    password: adminpassword
  - User
  username: testUser
password: userpassword


# Security Requirments
* Session TimeOut is 5 minutes 
* No concurent login
* Admin can search and filter
* User cannot filter date or amount

# Testing
Automated integration unit test can be run , all code in test package

# Manual Testing
It's better to go through the swagger-ui documentation to get full description on alll apis http://localhost:8081/swagger-ui.html
* Login API (/api/auth/login)
Accept json request specifing both username and password and return token that can be used in rest of apis
![Login](https://res.cloudinary.com/hafeezmnb/image/upload/v1605518898/login_umgbpk.png)

* LogOut API(api/auth/logout)
needs bearer token of authenticated user in header
![Logout](https://res.cloudinary.com/hafeezmnb/image/upload/v1605519319/logout_wy23zq.jpg)

* Fetch data api
needs bearer token of authenticated user in header
![fetch](https://res.cloudinary.com/hafeezmnb/image/upload/v1605519603/fetch_obkh9m.png)


