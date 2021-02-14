## Initial Observations

1. Going through the code, there seems to be some hints as to what can be expected of this service namely:

    Exceptions:
    1. The currency can be mismatched. Initial assumptions are that either the incorrect currency was used and it's just a symbol change that's required, or the invoice amount was calculated for the incorrect currency and we therefore have to recalculate for the expected currency.
    2. A customer might not be found. If so, we should mark those invoices in such a way that they are not processed again.
       There are enums for this, however it only has values for PAID and PENDING. New values should be added to indicate that a customer does not exist.
    3. Entity not found could be similar to above. *Have a close look at this*
    4. Invoices can be missing. If they are, maybe generate a new invoice? Otherwise why would we bother with handling missing invoices.
    5. Network exception. We should handle unexpected errors and not lose track of the invoices we have processed.
    6. There are exceptions, but no logging of said exceptions.
2. In the core module, there is some external API mock. 
3. According to the specification, there are invoices for different markets. The current situation has a small subset of data inserted into the database however, we can assume that the system will grow. We could possibly group transactions by market and execute it in that order. Possibly also only execute small subsets of data at once. (We do not want to run hundreds of thousands of payments at once)
4. The AntaeusDal class is handling multiple types of Entities. This violates S and I of SOLID.  
5. According to the Javelin setup, InvoiceNotFoundException should be returned as a 404, however, we are setting EntityNotFoundException for that (based on the comment). This should be updated to use InvoiceNotFoundException.
6. The application is synchronous. Execution time would be horrible, especially with large datasets.

## Proposed Solutions:
1. Exception Logic: 
   1. We can assume that the Customer.Currency is based off of the customer's location of personal settings. If so, it's more likely that the mismatch is the usage of the incorrect symbol and not a miscalculation against a different currency. We should just do a swap of the symbol. We should log this error.
   2. new enum values to add:
      1. CUSTOMER_DOES_NOT_EXIST - for when there is no customer for the particular invoice.
      2. NETWORK_ERROR - for when there are IO issues.
   4. If an invoice is not found, we should generate one and send it to the client. Then mark the invoice with a new state so that it can be picked up in the next run.
   5. Use retry in coroutines-core if there are network errors. Delay the retry to give the network some time to fix itself. If it fails after 3 times (should be configurable), then mark the invoices matching error enum.
   6. Introduce logging. Exceptions and errors to log file and transactions should go to DB for querying.
2. Using a Clean/Onion architecture approach, this should be in an infrastructure layer where all external systems should live.
3. SQLite is quite limited and won't be able to do row-level locking. Consider the performance loss here and be wary of multi-threading that may get blocked here due to table locks introduced by SQLite.
4. Refactor this code to use interfaces and develop against those instead. The dal concrete implementation is being used as a dependency, we should use an interface. We should use a DI container to help us manage our dependencies for this.
5. Updated to InvoiceNotFoundException.
6. Introduction of concurrency is necessary. Use coroutines.

## Stretch Goals
1. Add some event sourcing to the solution. Because we are dealing with money, there are bound to be disputes. We need to ensure that we log whether the transactions were successful or failed and the reasons for that failure. Decide on NoSQL/SQL/log4j text file.
2. Add a UI to navigate the logs (or just explain that a tool could be used to read logs from a text file similar to Sumo Logic).

## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

## Developing

Requirements:
- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file and it is gonna setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!
