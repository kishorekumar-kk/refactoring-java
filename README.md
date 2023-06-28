# Refactoring Java

The code creates an information slip about movie rentals.
Rewrite and improve the code after your own liking.

Think: you are responsible for the solution, this is a solution you will have to put your name on.


## Handing in the assignment

Reason how you have been thinking and the decisions you took. 
You can hand in the result any way you feel (git patch, pull-request or ZIP-file).
Note: the Git history must be included.


## To run the test:

```
javac src/*.java
java -cp src Main
```

# Points considered when refactoring

* Added packages with classes grouped based on functionality and scope
* Overall application is around buying/renting products for specific period of time. So, following functionalities added with Order interface.
    * Create cart
    * Add product to cart
    * Delete product from cart
    * Checkout
* Abstract class product added with pricing calculation method. There is a presumption that each product category has a different pricing approach. Therefore, the mechanism used to determine prices can be changed depending on the category of the product.
* Introduced Enum type for movie category as NEW, REGULAR and CHILDREN.
* As the pricing approach depends on type of movie, added calculate rentals method to MovieType enum.
* Movie is a special type of product, rented for specific span of time. So, added MovieOrder class which will be used for checking out movies.
* Intermediary model cart is required while selecting multiple products. Hence added Cart and CartItem which is mapped to customer.
* Added MovieOrder data class with the responsibility to save the rentals made by the user.
* Updated certain member variables of customer based on the scope.