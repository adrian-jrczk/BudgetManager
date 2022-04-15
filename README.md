# BudgetManager

## Table of contents
* [About](#about)
* [Features](#features)
* [Usage](#usage)
* [Installation](#installation)
* [Technologies used](#technologies-used)
* [Screenshots](#screenshots)


## About

BudgetManager is a simple program that helps you organize and manage your daily expenses.
With different filtering and ordering options, it allows you to have a detailed view of your past purchases and control your budget.

## Features

- Add categorized purchases to the database
- Add money to your budget and control it's balance
- Get purchases lists with expenses sum and different filtering and ordering options

## Usage

To use this program open it with console and type instructions.

### Adding purchase
To add purchase type:<br/>
`add CATEGORY`<br/>
Available categories: food, clothes, entertainment, other.
Then you will be asked to specify purchase name, price and quantity.

### Getting purchases list
To get purchases list type:<br/>
`get CATEGORY`<br/>
Available categories: all, food, clothes, entertainment, other.
Additionally you can specify time scope and sorting of given purchases.
Correct order of adding more options is:<br/>
`get CATEGORY TIME SORT`<br/>
Available time scopes: last-week, month, year.
Available sort types: oldest, recent, cheapest, expensive.

### Budget
In order to add money to your budget type:<br/>
`add balance AMOUNT`<br/>
To get current balance type:<br/>
`get balance`

### Other
`help`   - shows this message<br/>
`exit`   - closes program 

## Installation

1. Import this repository to some folder with `git clone https://github.com/adrian-jrczk/BudgetManager.git`
2. Open this folder and install with `mvn clean install`
3. In `target` folder there will be executable jar file `budgetmanager.jar` which you can move freely and run with `java -jar budgetmanager.jar`

This program also creates .budgetmanager folder inside home directory to store purchases.

## Technologies used

- Java 17
- H2 database 2.1.210

## Screenshots

![screenshot 1](images/screenshot01.png?raw=true "Commands example 1")
***
![screenshot 2](images/screenshot02.png?raw=true "Commands example 2")
***
![screenshot 3](images/screenshot03.png?raw=true "Commands example 3")
