# Project 02 — Java_Bootcamp

**Summary:** In this project, you will learn how to apply OOP/procedural/multiparadigm approaches in Java, and you will also write code following the functional paradigm.

💡 [Click here](https://new.oprosso.net/p/4cb31ec3f47a4596bc758ea1861fb624) to share your feedback on this project. It's anonymous and will help our team improve the training. We recommend filling out the survey right after completing the project.

## Contents

- [Project 02 — Java\_Bootcamp](#project-02--java_bootcamp)
  - [Contents](#contents)
  - [Chapter I](#chapter-i)
    - [Instructions](#instructions)
  - [Chapter II](#chapter-ii)
    - [General Information](#general-information)
  - [Chapter III](#chapter-iii)
    - [Project: Pet Info](#project-pet-info)
      - [Task 1. Pet List](#task-1-pet-list)
      - [Task 2. Determining the Amount of Pet Food](#task-2-determining-the-amount-of-pet-food)
      - [Task 3. Lists of Herbivorous and Omnivorous Pets](#task-3-lists-of-herbivorous-and-omnivorous-pets)
      - [Task 4. Increasing the Age of Certain Pets in the Functional Paradigm](#task-4-increasing-the-age-of-certain-pets-in-the-functional-paradigm)
      - [Task 5. Tracking Pet Walks](#task-5-tracking-pet-walks)
      - [Task 6. Pet Iterator](#task-6-pet-iterator)


## Chapter I

### Instructions

1. Throughout the course, you will experience uncertainty and a severe lack of information — this is normal. Remember that the repository and Google are always available to you, as are your peers and Rocket.Chat. Communicate. Search. Rely on common sense. Do not be afraid of making mistakes.
1. Pay attention to sources of information. Verify, think, analyze, compare.
1. Read the assignments carefully. Reread them several times.
1. It's best to read the examples carefully as well. They may contain something not explicitly stated in the assignment itself.
1. You might encounter inconsistencies when something new in the task or example contradicts what you already know. If that happens, try to figure it out. If you fail, make a note under “open questions” and resolve it during your work. Do not leave open questions unresolved.
1. If a task seems unclear or unachievable, it only seems that way. Try decomposing it. Most likely, individual parts will become clearer.
1. Along the way, you'll encounter many different tasks. Those marked with an asterisk (\*) are for more meticulous learners. They are of higher complexity and are not mandatory, but if you do them, you'll gain additional experience and knowledge.
1. Do not try to fool the system or those around you. You'll only be fooling yourself.
1. Have a question? Ask the neighbor on your right. If that doesn't help, ask the neighbor on your left.
1. When using someone's help, always make sure you understand why, how, and what for. Otherwise, that help is meaningless.
1. Always push only to the **develop** branch! The **master** branch will be ignored. Work in the **src** directory.
1. Your directory should not contain any files other than those specified in the tasks.

## Chapter II

### General Information

**Topics to study:**

- OOP/procedural/multiparadigm approach in Java;
- Differences from C and C++;
- Functional paradigm;
- Asynchronous/parallel programming.

## Chapter III

### Project: Pet Info

We will explore OOP/procedural/multiparadigm approaches in Java, writing code according to the functional paradigm, as well as asynchronous/parallel programming through a project that is a set of modules for displaying information about pets.

**Attention!** Each task should be organized as a separate project. For example,
`T02/src/exercise0`, `T02/src/exercise1`, … , `T02/src/exerciseN-1`, where **N** is the number of tasks. If a previous task is needed for the next one, simply copy the previous project into the directory for the next task and continue development there.


#### Task 1. Pet List

Develop a module that compiles a list of pets and outputs information about each pet.

- Create an abstract class Animal with two private fields: a String for the pet's name and an int for the pet's age.
- Implement a constructor for the abstract class Animal that takes two parameters (pet name as a String, pet age as an int) and assigns these values to the corresponding fields.
- Implement getters for the private fields (pet name as a String, pet age as an int).
- Create a class Dog that inherits from the abstract class Animal.
- Implement a constructor in Dog that takes two parameters (pet name as a String, pet age as an int) and passes them to the base class constructor.
- Override the toString() method in Dog so that it produces the following string:

  "Dog name = [pet\_name], age = [pet\_age]"

- Create a class Cat that inherits from the abstract class Animal.
- Implement a constructor in Cat that takes two parameters (pet name as a String, pet age as an int) and passes them to the base class constructor.
- Override the toString() method in Cat so that it produces the following string:

  "Cat name = [pet\_name], age = [pet\_age]"

- The program reads the number of pets.
- The program reads the type of the pet: dog/cat.
- Each pet is added to a general pets list.
- If an incorrect pet type is entered, the program outputs:

  "Incorrect input. Unsupported pet type"

  and proceeds to the next input.

- If a negative or zero age is entered, the program outputs:

  "Incorrect input. Age <= 0"

  and proceeds to the next input.

- The program should not terminate with an error on invalid input. Instead, it outputs:

  "Could not parse a number. Please, try again"

  and retries the input.

- The program must output information about each pet.
- The program works with reference data types.

|**Input**|**Output**|
| :- | :- |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> dog <br/> Snowball2 <br/> 10 <br/> dog <br/> Snowball3 <br/> 9 <br/>  | Dog name = Snowball, age = 12 <br/> Dog name = Snowball2, age = 10 <br/> Dog name = Snowball3, age = 9 <br/>  |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> cat <br/> Kitty <br/> 10 <br/> dog <br/> Balloon <br/> 9 <br/>        | Dog name = Snowball, age = 12 <br/> Cat name = Kitty, age = 10 <br/> Dog name = Balloon, age = 9 <br/>        |
| 3 <br/> hamster <br/> cat <br/> Kitty <br/> -10 <br/> cat <br/> Fura <br/> 9 <br/>                              | Incorrect input. Unsupported pet type <br/> Incorrect input. Age <= 0 <br/> Cat name = Fura, age = 9 <br/>     |

#### Task 2. Determining the Amount of Pet Food

Develop a module that determines how many grams of food a pet needs per serving based on its type.

- Create an abstract class Animal with three private fields: a String for the pet's name, an int for the pet's age, and a Double for the pet's weight.
- Implement a constructor for the abstract class Animal that takes three parameters (pet name as a String, pet age as an int, pet weight as a double) and assigns them to the corresponding fields.
- Implement getters for the private fields (pet name, pet age, pet weight).
- Declare a method getFeedInfoKg() in the abstract class Animal that returns a double representing the amount of food needed.
- Create a class Dog that inherits from the abstract class Animal.
- Implement a constructor in Dog that takes three parameters (pet name, pet age, pet weight) and passes them to the base class constructor.
- Implement the getFeedInfoKg() method in Dog, which calculates the required amount of food using the following formula:

  food amount = pet weight \* 0.3

- Override the toString() method in Dog to produce the following string:

  "Dog name = [pet\_name], age = [pet\_age], mass = [pet\_weight], feed = [food\_portion]"

- Create a class Cat that inherits from the abstract class Animal.
- Implement a constructor in Cat that takes three parameters (pet name, pet age, pet weight) and passes them to the base class constructor.
- Implement the getFeedInfoKg() method in Cat, which calculates the required amount of food using the following formula:

  food amount = pet weight \* 0.1

- Override the toString() method in Cat to produce the following string:

  "Cat name = [pet\_name], age = [pet\_age], mass = [pet\_weight], feed = [food\_portion]"

- The program reads the number of pets.
- The program reads the pet type to be input: dog/cat.
- Each pet is added to a common pets list.
- If an incorrect pet type is entered, the program outputs:

  "Incorrect input. Unsupported pet type"

  and proceeds to the next input.

- If a negative or zero age is entered, the program outputs:

  "Incorrect input. Age <= 0"

  and proceeds to the next input.

- If a negative or zero weight is entered, the program outputs:

  "Incorrect input. Mass <= 0"

  and proceeds to the next input.

- The program should not terminate with an error on invalid input. It should output:

  "Could not parse a number. Please, try again"

  and retry the input.

- The program must output information about each pet and the amount of food it needs.
- The program works with reference data types.


|**Input**|**Output**|
| :- | :- |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> 5.0 <br/> dog <br/> Snowball2 <br/> 10 <br/> 10.0 <br/> dog <br/> Snowball3 <br/> 9 <br/> 9.0 <br/> | Dog name = Snowball, age = 12, mass = 5.00, feed = 1.50 <br/> Dog name = Snowball2, age = 10, mass = 10.00, feed = 3.00 <br/> Dog name = Snowball3, age = 9, mass = 9.00, feed = 2.70 <br/> |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> 5.0 <br/> cat <br/> Kitty <br/> 10 <br/> 10.0 <br/> dog <br/> Balloon <br/> 9 <br/> 9.0 <br/>       | Dog name = Snowball, age = 12, mass = 5.00, feed = 1.50 <br/> Cat name = Kitty, age = 10, mass = 10.00, feed = 1.00 <br/> Dog name = Balloon, age = 9, mass = 9.00, feed = 2.70 <br/>       |
| 4 <br/> hamster <br/> cat <br/> Kitty <br/> -10 <br/> dog <br/> Balloon <br/> 9 <br/> -9 <br/> cat <br/> Fura <br/> 9 <br/> 12.5 <br/>        | Incorrect input. Unsupported pet type <br/> Incorrect input. Age <= 0 <br/> Incorrect input. Mass <= 0 <br/> Cat name = Fura, age = 9, mass = 12.50, feed = 1.25                             |

#### Task 3. Lists of Herbivorous and Omnivorous Pets

Develop a module that first outputs only herbivorous animals and then only omnivorous animals.

- Create an abstract class Animal with two private fields: a String for the pet's name, and an int for the pet's age.
- Implement a constructor in the abstract class Animal that takes two parameters (pet's name as a String, pet's age as an int) and assigns these values to the corresponding fields.
- Implement getters for the private fields (pet's name and pet's age).
- Create an interface Herbivore.
- In the Herbivore interface, declare a method chill() that returns a String.
- Create an interface Omnivore.
- In the Omnivore interface, declare a method hunt() that returns a String.
- Create a class Dog that inherits from the abstract class Animal and implements the Omnivore interface.
  - Implement a constructor in Dog that takes two parameters (pet's name, pet's age).
  - The Dog constructor should pass the pet's name and age to the base class constructor.
  - In Dog, implement the hunt() method so that it produces the following string:

    "I can hunt for robbers"

  - In Dog, override the toString() method so that it produces the following string:

    "Dog name = [pet\_name], age = [pet\_age]. " + hunt()

- Create a class Cat that inherits from the abstract class Animal and implements the Omnivore interface.
  - Implement a constructor in Cat that takes two parameters (pet's name, pet's age).
  - The Cat constructor should pass the pet's name and age to the base class constructor.
  - In Cat, implement the hunt() method so that it produces the following string:

    "I can hunt for mice"

  - In Cat, override the toString() method so that it produces the following string:

    "Cat name = [pet\_name], age = [pet\_age]. " + hunt()

- Create a class Hamster that inherits from the abstract class Animal and implements the Herbivore interface.
  - Implement a constructor in Hamster that takes two parameters (pet's name, pet's age).
  - The Hamster constructor should pass the pet's name and age to the base class constructor.
  - In Hamster, implement the chill() method so that it produces the following string:

    "I can chill for 8 hours"

  - In Hamster, override the toString() method so that it produces the following string:

    "Hamster name = [pet\_name], age = [pet\_age]. " + chill()

- Create a class GuineaPig that inherits from the abstract class Animal and implements the Herbivore interface.
  - Implement a constructor in GuineaPig that takes two parameters (pet's name, pet's age).
  - The GuineaPig constructor should pass the pet's name and age to the base class constructor.
  - In GuineaPig, implement the chill() method so that it produces the following string:

    "I can chill for 12 hours"

  - In GuineaPig, override the toString() method so that it produces the following string:

    "GuineaPig name = [pet\_name], age = [pet\_age]. " + chill()

- The program reads the number of pets.
- The program reads the type of pet being entered: dog/cat/hamster/guinea.
- Each pet is added to a common list pets.
- If an incorrect pet type is entered, the program outputs:

  "Incorrect input. Unsupported pet type"

  and proceeds to the next input.

- If a negative or zero age is entered, the program outputs:

  "Incorrect input. Age <= 0"

  and proceeds to the next input.

- The program should not terminate with an error on invalid input. It outputs:

  "Could not parse a number. Please, try again"

  and retries the input.

- The program must first output information about all herbivorous pets, then about all omnivorous pets.
- The program works with reference data types.

**Examples**

|**Input**|**Output**|
| :- | :- |
| 4 <br/> dog <br/> Snowball <br/> 12 <br/> guinea <br/> Piggy <br/> 5 <br/> cat <br/> Snowball <br/> 9 <br/> hamster <br/> Wave <br/> 2 <br/> | GuineaPig name = Piggy, age = 5. I can chill for 12 hours <br/> Hamster name = Wave, age = 2. I can chill for 8 hours <br/> Dog name = Snowball, age = 12. I can hunt for robbers <br/> Cat name = Snowball, age = 9. I can hunt for mice <br/> |
| 2 <br/> dog <br/> Snowball <br/> 12  <br/> cat <br/> Kitty <br/> 10 <br/>                                                                    | Dog name = Snowball, age = 12. I can hunt for robbers <br/> Cat name = Kitty, age = 10. I can hunt for mice <br/>                                                                                                                                    |
| 3 <br/> turtle <br/> cat <br/> Kitty <br/> -10 <br/> guinea <br/> Piggy <br/> 3 <br/>                                                        | Incorrect input. Unsupported pet type <br/> Incorrect input. Age <= 0 <br/> GuineaPig name = Piggy, age = 3. I can chill for 12 hours <br/>                                           |

#### Task 4. Increasing the Age of Certain Pets in the Functional Paradigm

Develop a module that increases the age of pets older than 10 years, while adhering to the functional paradigm.

- Create an abstract class Animal with two private fields: a String for the pet's name, and an int for the pet's age.
- Implement a constructor in the abstract class Animal that takes two parameters (pet's name as a String, pet's age as an int) and assigns these values to the corresponding fields.
- Implement getters for the private fields (pet's name and pet's age).
- Create a class Dog that inherits from the abstract class Animal.
  - Implement a constructor in Dog that takes two parameters (pet's name, pet's age) and passes them to the base class constructor.
  - Override the toString() method in Dog so that it produces the following string:

    "Dog name = [pet\_name], age = [pet\_age]"

- Create a class Cat that inherits from the abstract class Animal.
  - Implement a constructor in Cat that takes two parameters (pet's name, pet's age) and passes them to the base class constructor.
  - Override the toString() method in Cat so that it produces the following string:

    "Cat name = [pet\_name], age = [pet\_age]"

- The program reads the number of pets.
- The program reads the type of the pet to be entered: dog/cat.
- Each pet is added to a common list pets.
- If an incorrect pet type is entered, the program outputs:

  "Incorrect input. Unsupported pet type"

  and proceeds to the next input.

- If a negative or zero age is entered, the program outputs:

  "Incorrect input. Age <= 0"

  and proceeds to the next input.

- The program should not terminate with an error on invalid input. It outputs:

  "Could not parse a number. Please, try again"

  and retries the input.

- The program must increment the age of all pets older than 10 years by 1 year.
- The program must then output information about each pet.
- The program works with reference data types.
- The program must adhere to the functional paradigm.
- The program must use the Stream API.
- The use of loop operators is prohibited.


|**Input**|**Output**|
| :- | :- |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> dog <br/> Snowball2 <br/> 8 <br/> dog <br/> Snowball3 <br/> 10 <br/>       | Dog name = Snowball, age = 13 <br/> Dog name = Snowball2, age = 8 <br/> Dog name = Snowball3, age = 10                                 |
| 3 <br/> dog <br/> Snowball <br/> 8 <br/> cat <br/> Kitty <br/> 9 <br/> dog <br/> Balloon <br/> 9 <br/>               | Dog name = Snowball, age = 8 <br/> Cat name = Kitty, age = 9 <br/> Dog name = Balloon, age = 9 <br/>                                   |
| 4 <br/> hamster <br/> cat <br/> Kitty <br/> -10 <br/> dog <br/> Balloon <br/> 10 <br/> cat <br/> Fura <br/> 9 <br/>  | Incorrect input. Unsupported pet type <br/> Incorrect input. Age <= 0 <br/> Dog name = Balloon, age = 10 <br/> Cat name = Fura, age = 9 |

#### Task 5. Tracking Pet Walks
Develop a module that tracks the start and end time of a pet's walk.

- Create an abstract class Animal with two private fields: a String for the pet's name and an int for the pet's age.
- Implement a constructor in the abstract class Animal that takes two parameters (the pet's name as a String and the pet's age as an int) and assigns these values to the corresponding fields.
- Implement getters for the private fields (pet's name and pet's age).
- Declare a method double goToWalk() in the abstract class Animal.
- Create a class Dog that inherits from the abstract class Animal.
  - Implement a constructor in Dog that takes two parameters (the pet's name, the pet's age) and passes them to the base class constructor.
  - Override the toString() method in Dog to produce the following string:

    "Dog name = [pet\_name], age = [pet\_age]"

  - Override the goToWalk() method in Dog as follows:
    - The method calculates the time to walk in seconds.
    - It calls TimeUnit.SECONDS.sleep() for the calculated time.
    - It returns the calculated time.
- Create a class Cat that inherits from the abstract class Animal.
  - Implement a constructor in Cat that takes two parameters (the pet's name, the pet's age) and passes them to the base class constructor.
  - Override the toString() method in Cat to produce the following string:

    "Cat name = [pet\_name], age = [pet\_age]"

  - Override the goToWalk() method in Cat as follows:
    - The method calculates the time to walk in seconds.
    - It calls TimeUnit.SECONDS.sleep() for the calculated time.
    - It returns the calculated time.
- The walk time calculation for Dog is based on the following formula:

  [walk\_time] = [pet\_age] \* 0.5

- The walk time calculation for Cat is based on the following formula:

  [walk\_time] = [pet\_age] \* 0.25

- The program reads the number of pets.
- The program reads the type of pet being entered: dog/cat.
- Each pet is added to a common list pets.
- If an incorrect pet type is entered, the program outputs:

  "Incorrect input. Unsupported pet type"

  and proceeds to the next input.

- If a negative or zero age is entered, the program outputs:

  "Incorrect input. Age <= 0"

  and proceeds to the next input.

- The program should not terminate with an error on invalid input. Instead, it outputs:

  "Could not parse a number. Please, try again"

  and retries the input.

- The program must call the goToWalk() method for each pet.
- Each call to goToWalk() must be made asynchronously in a separate thread.
- The program must wait for all goToWalk() method calls to complete before exiting.
- When the walk is finished, the program must print the following information to a line in the console: the pet's info, the start time of the walk, and the end time of the walk.
- The start and end time of the walk must be calculated relative to the program start time.
- The program works with reference data types.
- The difference in walk start time between pets relative to each other must not exceed 1 second.


|**Input**|**Output** |
| :- | :- |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> dog <br/> Snowball2 <br/> 8 <br/> dog <br/> Snowball3 <br/> 10 <br/>        | Dog name = Snowball2, age = 8, start time = 0.20, end time = 4.20  <br/> Dog name = Snowball3, age = 10, start time = 0.30, end time = 5.30 <br/> Dog name = Snowball, age = 12, start time = 0.10, end time = 6.10 |
| 3 <br/> dog <br/> Snowball <br/> 8 <br/> cat <br/> Kitty <br/> 9 <br/> dog <br/> Balloon <br/> 9 <br/>                | Cat name = Kitty, age = 9, start time = 0.20, end time = 2.45 <br/> Dog name = Snowball, age = 8, start time = 0.10, end time = 4.10 <br/> Dog name = Balloon, age = 9, start time = 0.30, end time = 4.80          |
| 4 <br/> hamster <br/> cat <br/> Kitty <br/> -10 <br/> dog <br/> Balloon <br/> 11 <br/> cat <br/> Fura <br/> 9 <br/>   | Incorrect input. Unsupported pet type <br/> Incorrect input. Age <= 0 <br/> Cat name = Fura, age = 9, start time = 0.20, end time = 2.45 <br/> Dog name = Balloon, age = 11, start time = 0.10, end time = 5.60      |


#### Task 6. Pet Iterator

Develop a module that implements an iterator for pets.

1. Create an abstract class Animal with two private fields: a String for the pet's name and an int for the pet's age.
2. Implement a constructor in the abstract class Animal that takes two parameters (the pet's name as a String and the pet's age as an int) and assigns them to the corresponding fields.
3. Implement getters for the private fields (pet's name and pet's age).
4. Create a class Dog that inherits from the abstract class Animal.
   1. Implement a constructor in Dog that takes two parameters (the pet's name, the pet's age) and passes them to the base class constructor.
   2. Override the toString() method in Dog so it produces the following string:

      "Dog name = [pet\_name], age = [pet\_age]"

5. Create a class Cat that inherits from the abstract class Animal.
   1. Implement a constructor in Cat that takes two parameters (the pet's name, the pet's age) and passes them to the base class constructor.
   2. Override the toString() method in Cat so it produces the following string:

      "Cat name = [pet\_name], age = [pet\_age]"

6. Create a BaseIterator interface.
   1. In the BaseIterator interface, declare a method next(), that returns an element of type T.
   2. In the BaseIterator interface, declare a method hasNext() that returns a boolean.
   3. In the BaseIterator interface, declare a method reset() that returns nothing.
7. Create an AnimalIterator class that implements the BaseIterator interface.
   1. In AnimalIterator, declare 2 private fields: a list of animals and an integer index for the current element in the list.
   2. Implement a constructor for AnimalIterator that takes the list of animals and assigns it to the corresponding field.
   3. In AnimalIterator, implement the next() method as follows: this method returns the current element from the list of animals, then increments the current index by one.
   4. In AnimalIterator, implement the hasNext() method as follows: this method returns true if the current index is less than the number of elements in the list of animals; otherwise, it returns false.
   5. In AnimalIterator, implement the reset() method so that it resets the current index to zero.
8. The program reads the number of pets.
9. The program reads the type of pet to be entered: dog/cat.
10. Each pet is added to a common list pets.
11. If an incorrect pet type is entered, the program outputs:

    "Incorrect input. Unsupported pet type"

    and proceeds to the next input.

12. If a negative or zero age is entered, the program outputs:

    "Incorrect input. Age <= 0"

    and proceeds to the next input.

13. The program should not terminate with an error on invalid input. Instead, it outputs:

    "Could not parse a number. Please, try again"

    and retries the input.

14. The program must output information about each pet.
15. The program must iterate through the pets list using the AnimalIterator.
16. The program works with reference data types.


|**Input**|**Output**|
| :- | :- |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> dog <br/> Snowball2 <br/> 10 <br/> dog <br/> Snowball3 <br/> 9 <br/> | Dog name = Snowball, age = 12 <br/> Dog name = Snowball2, age = 10 <br/> Dog name = Snowball3, age = 9 <br/> |
| 3 <br/> dog <br/> Snowball <br/> 12 <br/> cat <br/> Kitty <br/> 10 <br/> dog <br/> Balloon <br/> 9 <br/>       | Dog name = Snowball, age = 12 <br/> Cat name = Kitty, age = 10 <br/> Dog name = Balloon, age = 9 <br/>       |
| 3 <br/> hamster <br/> cat <br/> Kitty <br/> -10 <br/> cat <br/> Fura <br/> 9 <br/>                             | Incorrect input. Unsupported pet type <br/> Incorrect input. Age <= 0 <br/> Cat name = Fura, age = 9 <br/>    |


