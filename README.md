# Kadmin
A type-safe, idempotent wrapper around the kadmin command for Scala.

In the JVM its possible to obtain Kerberos tickets but to create or delete principals is outright impossible.
The reason is that kerberos only offers a C API and interfacing with C via the Java Native Interface (JNI) is
very hard to do correctly.

We solve the problem of Kerberos administration in JVM via the only other alternative which is to launch the kadmin
command and write to its standard input and read from its standard output.
To simplify this process we use [scala-expect](https://github.com/Lasering/scala-expect).

## Available functions
 - Adding a principal
 - Modifying a principal
   - Expiring a principal
   - Expiring the principal password
 - Change the principal password
 - Deleting a principal
 - Reading principal attributes
   - Getting the principal expiration date
   - Getting the principal password expiration date
 - Checking a principal password
 - Adding a policy
 - Modifying a policy
 - Deleting a policy
 - Reading policy attributes

All of these commands can be made with authentication, ie using the **kadmin** command or without authentication
using the **kadmin.local** command. Whether of not to perform authentication can be defined in the configuration.
Besides the kadmin commands the following functions are also available:

 - `getFullPrincipalName` - returns the principal name with the realm, eg: kadmin/admin@EXAMPLE.COM.
 - `obtainTicketGrantingTicket` - invokes `kinit` to obtain a ticket and returns the DateTime in which the ticket must be renewed.
 - `withAuthentication` - performs a kadmin command, but the details of handling the authentication
   and unknown errors in the initialization (such as the KDC not being available) have already been dealt with.
 - `withoutAuthentication` - performs a kadmin command, but the details of handling unknown errors
   in the initialization have already been dealt with.
 - `doOperation` - performs a kadmin command which will be performed with authentication or not
   according to the configuration `perform-authentication`.
 - `parseDateTime` - parses a date time string returned by a kadmin command to a `DateTime`.
 - `insufficientPermission`, `principalDoesNotExist`, `policyDoesNotExist`, `passwordIncorrect`, `passwordExpired`, `unknownError` -
   these functions match against common kadmin errors and return the appropriate `ErrorCase`.
 - `preemptiveExit` - allows you to gracefully terminate the kadmin cli and ensure the next `ExpectBlock`s in the current
   `Expect` do not get executed.