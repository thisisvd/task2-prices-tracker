<h1 align="center">
  <br>
  Task 1: API Integration with Transactions
  <br>
</h1>

<p align="center">
 <a href="#project-overview">Project Overview</a> •
 <a href="#setup-instructions">Setup Instructions</a> •
 <a href="#apk-build-instructions">APK build instructions</a> •
 <a href="#bonus-features-implemented">Bonus features implemented</a> •
 <a href="#source-code">Source Code</a> •
 <a href="#released-apk">Released Apk</a>
</p>

## Project Overview
The above project has been a task part for the tasks provided in the assignment [PDF](https://github.com/thisisvd/task1-api-transactions/blob/master/support/Android%20Development%20Assessment%20Tasks.pdf). The application contains 2 screens. One is the login screen in which the user will provide a username and password, and on pressing login, it will get a token, which will be saved securely in shared preferences, and they will be navigated to the next fragment, which is the transaction list fragment, which contains a list of transactions with 2 menu items: one to search for the items based on description, amount, date, or category, and the second is to log out, clicking on which clears the token. When the token is saved and the user launches an app, the biometric login will be called back to, and success will redirect to the transaction list fragment, and unsuccessful will close the biometric prompt and will allow the user to log in via username and password. The application also stores the transaction data in the DB using the ROOM. So, when the network is unavailable, it will fetch the data from the local DB. Used clean architecture for the project's modules and injected dependencies using Dagger-Hilt. Retrofit for the networking library, Timber for logging, and, of course, as stated, used Java for the application.

## Setup Instructions

 - Clone the project on your system.
```bash
git clone https://github.com/thisisvd/task1-api-transactions.git
```
- Sync the project -> Clean Project -> then build it and run it on your appropiate android device.

## APK build instructions
**Create Debug build**
 - Select the `build varient` (debug).
 - Select `Build` in top menu -> then `Build Apk Bundle(s) / Apk(s)` -> then `Build Apk`.
   
**Create Release build**
 - Select the `build varient` (release).
 - Select `Build` in top menu -> then `Generate Signed Apk Bundle` -> then `Select Apk or bundle` -> then add .jks key present at main root folder with key alias `key0` & password `vimaldubey` (Shared only for testing purpose else never share .jks keys) -> Locate build.
 - Note: To run release build you have to edit congfiguration and set signin config to debug to successful run of release build in emulator or connected device. 

## Bonus features implemented
- ✅ `Dark Mode:` Added dark mode, which will automatically change according to the device theme.
- ✅ `Offline Mode (using Room Database):` Implemented a ROOM database that stores the transactions and provides transactions when the network is unavailable.
- ✅ `Search/Filter functionality:` Implemented a search functionality when entering a string that will filter the transaction list based on description, amount, date, and category.
  
## Source Code
- `Tech-stack used:` Java, DI(Hilt), Retrofit, Security Crypto, Biometric, ROOM, Timber etc. 
- Clean architecture with dependency injection was used using Hilt modules followed by MVVM(Model-View-Viewmodel). Below is the project structure of the application flow.
```
task2_prices_tracker
│── application
│   └── BaseApplication
│── core
│   ├── NetworkUtils
│   ├── Resource
│   └── Utils
│── data
│   ├── local
│   │   ├── room
│   │   │   ├── TransactionDao
│   │   │   └── TransactionDatabase
│   │   └── EncryptedSharedPreference
│   ├── network
│   │   ├── Api
│   │   ├── ApiHelper
│   │   ├── ApiImpl
│   │   └── MainRepository
│── di
│   ├── DatabaseModule
│   └── NetworkModule
│── domain
│   ├── Transactions
│   ├── User
│   └── UserResponse
│── ui
│   ├── adapter
│   │   └── TransactionsAdapter
│   ├── login
│   │   └── LoginFragment
│   ├── transactions
│   │   └── TransactionsFragment
│   ├── viewmodel
│   │   └── MainViewModel
│   └── MainActivity
│── utils
│   ├── Constants
│   └── CoroutineHelper
│── res
```

## Released Apk
- Released Application path can be found [here!](https://github.com/thisisvd/task1-api-transactions/tree/master/app/release).
