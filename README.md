# kadmin [![Build Status](https://travis-ci.org/ist-dsi/kadmin.svg?branch=master)](https://travis-ci.org/ist-dsi/kadmin) [![Codacy Badge](https://api.codacy.com/project/badge/grade/a5fead3a55db40cd96470ed7a8efe9c5)](https://www.codacy.com/app/Whatever/kadmin)
A type-safe wrapper around the kadmin command for Scala.

In JVM it's possible to obtain Kerberos tickets, but to create or delete principals is outright impossible.
Kerberos only offers a C API, and interfacing with it via the Java Native Interface (JNI) can be a hard task to accomplish properly.

We solve the problem of Kerberos administration in JVM via the only other alternative: by launching the kadmin
command and write to its standard input and read from its standard output.
To simplify this process we use [scala-expect](https://github.com/Lasering/scala-expect).

[Latest scaladoc documentation](http://ist-dsi.github.io/kadmin/latest/api/)

## Install
Add the following dependency to your `build.sbt`:
```scala
libraryDependencies += "pt.tecnico.dsi" %% "kadmin" % "0.0.2"
```

## Available kadmin commands
 - [Adding a principal](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@addPrincipal(options:String,principal:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Modifying a principal](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@modifyPrincipal(options:String,principal:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
   - [Expiring a principal](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@expirePrincipal(principal:String,expirationDateTime:pt.tecnico.dsi.kadmin.ExpirationDateTime):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
   - [Expiring the principal password](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@expirePrincipalPassword(principal:String,datetime:pt.tecnico.dsi.kadmin.ExpirationDateTime,force:Boolean):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Change the principal password](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@changePassword(principal:String,newPassword:Option[String],randKey:Boolean,salt:Option[String]):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Deleting a principal](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@deletePrincipal(principal:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Reading principal attributes](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@withPrincipal[R](principal:String)(f:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]=>Unit):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]])
   - [Getting the principal expiration date](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@getExpirationDate(principal:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,pt.tecnico.dsi.kadmin.ExpirationDateTime]])
   - [Getting the principal password expiration date](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@getPasswordExpirationDate(principal:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,pt.tecnico.dsi.kadmin.ExpirationDateTime]])
 - [Checking a principal password](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@checkPassword(principal:String,password:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Adding a policy](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@addPolicy(options:String,policy:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Modifying a policy](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@modifyPolicy(options:String,policy:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Deleting a policy](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@deletePolicy(policy:String):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,Boolean]])
 - [Reading policy attributes](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@withPolicy[R](policy:String)(f:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]=>Unit):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]])

All of these commands can be made with authentication, i.e. using the **kadmin** command or without authentication
using the **kadmin.local** command. Whether of not to perform authentication can be defined in the configuration.

Every command is idempotent except when changing either a password, a salt or a key.

Besides these kadmin commands the following functions are also available:

 - [`getFullPrincipalName`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@getFullPrincipalName(principal:String):String) - returns the principal name with the realm, eg: kadmin/admin@EXAMPLE.COM.
 - [`obtainTicketGrantingTicket`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@getFullPrincipalName(principal:String):String) - invokes `kinit` to obtain a ticket and returns the DateTime in which the ticket must be renewed.
 - [`withAuthentication`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@withAuthentication[R](f:work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]=>Unit):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]) - performs a kadmin command, but the details of handling the authentication
   and unknown errors in the initialization (such as the KDC not being available) have already been dealt with.
 - [`withoutAuthentication`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@withoutAuthentication[R](f:work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]=>Unit):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]) - performs a kadmin command, but the details of handling unknown errors
   in the initialization have already been dealt with.
 - [`doOperation`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@doOperation[R](f:work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]=>Unit):work.martins.simon.expect.fluent.Expect[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]) - performs a kadmin command which will be performed with authentication or not
   according to the configuration `perform-authentication`.
 - [`parseDateTime`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@parseDateTime(dateTimeString:String):pt.tecnico.dsi.kadmin.ExpirationDateTime) - parses a date time string returned by a kadmin command to a `DateTime`.
 - [`insufficientPermission`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@insufficientPermission[R](expectBlock:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):work.martins.simon.expect.fluent.RegexWhen[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]),
   [`principalDoesNotExist`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@principalDoesNotExist[R](expectBlock:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):work.martins.simon.expect.fluent.StringWhen[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]),
   [`policyDoesNotExist`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@policyDoesNotExist[R](expectBlock:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):work.martins.simon.expect.fluent.StringWhen[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]),
   [`passwordIncorrect`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@passwordIncorrect[R](expectBlock:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):work.martins.simon.expect.fluent.StringWhen[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]),
   [`passwordExpired`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@passwordExpired[R](expectBlock:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):work.martins.simon.expect.fluent.StringWhen[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]),
   [`unknownError`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@unknownError[R](expectBlock:work.martins.simon.expect.fluent.ExpectBlock[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):work.martins.simon.expect.fluent.RegexWhen[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]) -
   these functions match against common kadmin errors and return the appropriate `ErrorCase`.
 - [`preemptiveExit`](https://ist-dsi.github.io/kadmin/latest/api/index.html#pt.tecnico.dsi.kadmin.Kadmin@preemptiveExit[R](when:work.martins.simon.expect.fluent.When[Either[pt.tecnico.dsi.kadmin.ErrorCase,R]]):Unit) - allows you to gracefully terminate the kadmin cli and ensure the next `ExpectBlock`s in the current
   `Expect` do not get executed.

## How to test kadmin
In the project root run `./test.sh`. This script will run `docker-compose up` inside the docker-kerberos folder.
Be sure to have docker and docker-compose installed on your computer.

## Note on the docker-kerberos folder
This folder is a [git fake submodule](http://debuggable.com/posts/git-fake-submodules:4b563ee4-f3cc-4061-967e-0e48cbdd56cb)
to the [docker-kerberos repository](https://github.com/ist-dsi/docker-kerberos).