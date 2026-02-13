# The Wasteland Exchange

## Purpose:
In a post-apocalyptic world, survival hinges on efficient 
distribution of resources. This system tracks varying inventories  of 
food, medicine, and weapons across scattered colonies and efficiently processes trades.
By tracking the various trade requests submitted by colonies, this system 
manages orderly allocation of resources to colonies in need.

## Features:
- Manages colonies by generating uniques ID's for each colony, giving them locations using the Point2D Class, and having risk factors based on how dangerous the area is
- Secures inventories for each colony through encapsulation
- Resource system implements Polymorphism allowing the broad class of resources to have diverse subclasses
- Trade requests are automatically logged upon instantiation with unique ID's and timestamps using the Date library
- Trade request are processed according to the order they are received to ensure priority-based sorting

## File Structure:
- Colony.java: Manages colony name, location, risk level, and inventory
- Resource.java: Abstract class that defines name and amount
- Subclasses(Food.java,Medicine.java, Weapon.java): Inherits from Resource and defines more specific attributes such as durability and type
- TradeRequest.java: Stores information for the trade requests like which colony is the requestor, what resources is being requested, and the date and time of the request
- Comparable.java: Used for sorting trade requests
- Identifiable.java: Used for unique identification
- TradeManger.java: Stores queue of trade requests and matches them based on criteria like risk factor and distance
-  Tester.java: The "main" program where all the objects are initialized and ran

## Data Model Decisions:
- HashMap for the inventory to allow for fast lookup
- UnmodifiableMap of deep copies for viewing the inventory to keep colonies' inventories secure
- Polymorphism to allow diverse subclasses of the broad and general superclass Resource
- Priority Queue for trade requests allows for efficient trade matches to be made

## How to Run:
Compile and execute the Tester.java file

## Limitations:
- Colonies cannot request more than one type of item at a time
- Data is not persistent, all trade and colony information is wiped when the program is terminated

## Future Implementations:
- Including risk factor as a criteria for matching trade requests
- Utilizing Point2D's .distance() method to calculate distances between colonies

