# Assignment Deliverables
This is a Group Assignment - please submit the following deliverables as one team.  Please upload the PDF documents to Canvas.  Your team needs to provide three (3) deliverables for this assignment:

[1] The first deliverable is a Sequence Diagram, provided to us as a PDF document labeled "sequence_diagram_team<your team number>" in Canvas (for example, "sequence_diagram_team17").  Your sequence diagram should provide enough examples to identify how your solution (i.e., source code structure) handles not only the "happy path" use case, but also how your code handles the exceptional case of ensuring that a drone has the correct conditions to move to a new destination.

[2] The second deliverable is a Design Class Diagram, provided to us as a PDF document labeled "design_class_diagram_team<your team number>" in Canvas (for example, "design_class_diagram_team17").

The true and sincere purpose of these design artifacts is to help foster your team's efforts to implement the Minimally Viable Product (MVP) that will also be required.  You should review your feedback from the earlier assignment, and take the time to revise and update your Domain Model and Use Case Artifacts in preparation for developing these deliverables.

[3] The third deliverable is a repository of working Java Source Code that implements the Minimally Viable Product that we've defined. Please develop your solution in the GT Github repository that you created in the earlier assignments.  Our MVP will focus on these essential aspects of the problem description/scenario:

delivery services purchasing drones --> drones being fueled and carrying ingredients
--> drones flying and delivering those ingredients to different restaurants at different locations
--> restaurants purchasing those ingredients
Other aspects of the problem scenario - for example, restaurant owners, employees, workers, pilots, managers, etc. - will be introduced in later phases/iterations of the project.  Your code will be [A] compared to your design artifacts for consistency; and, [B] tested to ensure that it works correctly and meets the functional requirements.

As promised, here is the code for calculating the distance between two locations.  The locations are objects that will provide their X and Y coordinates as needed:

```java
Integer calcDistance(GeoLocation destination) {
    return 1 + (int) Math.floor(Math.sqrt(Math.pow(getXCoord() - destination.getXCoord(), 2) + Math.pow(getYCoord() - destination.getYCoord(), 2))); }
```

This is normally executed as Integer distance = <location object 1>.calcDistance(<location object 2>)

# Helpful Reminders, Clarifications & Hints:
There are three (3) common types of errors that you should handle appropriately for all of the designated procedures:
1. If an identifier for an instance is invalid, doesn't exist, etc., then referencing that identifier will very likely cause a null reference error.  In these cases, simply halt execution of the procedure and display an appropriate error message.

2. If creating a new instance, then be sure that the identifier for the instance will be distinct (i.e., unique) within that class.  The init_barcode parameter is the identifier that must be unique for Ingredients.  The init_name parameter is the identifier that must be unique for Locations, Delivery Services and Restaurants.  For Drones, the service_name parameter must represent a valid Delivery Service that is buying the Drone, and then the Drone must have a distinct init_tag value within that service.  The overall combination of (service_name, init_tag) must be distinct for each Drone.  Don't create instances with duplicate identifiers, because this will very likely cause problems later. [And yes - this is a slight change from the original requirements - but it's still a reasonable change to implement within this phase.]

3. All "physical values" - for example, drone package-carrying capacities, fuel levels, ingredient quantities and weights, etc. - must be zero or greater.  If one or more of these parameter values is negative, then simply halt execution of the procedure and display an appropriate error message.  Note that the X and Y coordinates for a Location are permitted to be negative.

I will NOT test your program against: [1] undefined commands; [2] commands with the incorrect number of parameters; or, [3] invalid parameter data types (i.e., strings will be valid strings, numbers will be valid integers).

The general protocol that we will follow for invalid parameters, or any other errors, when executing a designated procedure: display an appropriate error message, and then otherwise avoid executing any actions that would change the state of the system.  And if you've already changed the system state, then you must revert it back to its condition/value immediately before the operation was executed.

Here are the other more specific errors for the other procedures:

`checkDistance(String departure_point, String arrival_point)`
Ensure that the departure_point and arrival_point parameters refer to valid Locations

`makeDeliveryService(String init_name, Integer init_revenue, String located_at)`
Each delivery service must have a valid home base for repairing, restocking and refueling drones, so ensure that the located_at parameter refers to a valid Location

`makeRestaurant(String init_name, String located_at)`
Similarly, each restaurant must have a valid location where drones can come to offer their ingredients, so ensure that the located_at parameter refers to a valid Location

`makeDrone(String service_name, Integer init_tag, Integer init_capacity, Integer init_fuel)`
All drones are created at the same location as their owning delivery service's home base.  A drone can only be created if there's available space at that location.

`flyDrone(String service_name, Integer drone_tag, String destination_name)`
Remember that a delivery service never wants to leave a drone stranded without enough fuel to return to home base.  Therefore, before a drone moves from its current location, it must have enough fuel to reach the intended destination AND must also have enough fuel to get from the destination back to its home base.  Also, it must ensure that there is enough space at the intended destination.

`loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price)`
A number of packages (i.e., quantity) can be loaded onto that drone if - and only if - the drone is located at its service's home base, and the drone has enough free slots to hold the new packages.

`loadFuel(String service_name, Integer drone_tag, Integer petrol)`
A drone can be refueled if - and only if - the drone is located at its service's home base.

`purchaseIngredient(String restaurant_name, String service_name, Integer drone_tag, String barcode, Integer quantity)`
A restaurant can only purchase ingredients from a drone when the drone is at the restaurant's location.  Also, the drone must be carrying enough of the ingredient (i.e., quantity) being requested - otherwise, the order must fail/be refused in its entirety (i.e., no "partial orders").

# How We Will Evaluate Your Submission
We will evaluate your submission based on two main factors:

Correctness: Does your source code work correctly with respect to the problem description and requirements?
Consistency: Are your design artifacts consistent with your source code?
