# Assignment Deliverables
This is a Group Assignment - please submit the following deliverables as one team.  Please upload the PDF documents to Canvas.  Your team needs to provide three (3) deliverables for this assignment:

[1] The first deliverable is an updated Sequence Diagram, provided to us as a PDF document labeled "sequence_diagram_team<your team number>" in Canvas (for example, "sequence_diagram_team17").  Your sequence diagram should focus on how your system implements the exceptional case of ensuring that groups of drones (i.e., swarms) correctly determine whether or not the conditions permit them to move to a new destination/location.

[2] The second deliverable is an updated Design Class Diagram, provided to us as a PDF document labeled "design_class_diagram_team<your team number>" in Canvas (for example, "design_class_diagram_team17").  We are looking forward to seeing continual improvement in your designs from the standpoint of distributing the data and operations across the various objects in your problem domain while "fighting the gravitational pull" of it all winding up in the InterfaceLoop() class...

The true and sincere purpose of these design artifacts is to help foster your team's efforts to build additional capabilities on the Minimally Viable Product (MVP).  You should review your feedback from the earlier assignments and take time to continually revise and update your design artifacts as you implement your system.  Code a little, design a little, code a little more, design a little more...

[3] The third deliverable is a repository of working Java Source Code that implements the new required capabilities on top of the MVP that you've developed so far.  Though you may do some of your design and development on other platforms (e.g., a "local" laptop), you must publish your solution in the GT Github repository that you created in the earlier assignments.  We also highly recommend that you develop your solution in that same repository - it takes time to get used to using Github, but the benefits are worth the time invested.

This phase of the project essentially adds the "personnel layer" to your MVP, along with a few other functional requirements such as drone swarms.  The main "happy path" functionality is still present:

delivery services purchasing drones
--> drones being fueled and carrying ingredients
--> drones flying and delivering those ingredients to different restaurants at different locations
--> restaurants purchasing those ingredients
Below, we will describe the new capabilities that you must implement for this phase of the project, and we will also describe how the new requirements affect some of the existing capabilities.

The following operations are new capabilities that you must implement:

`makePerson(String init_username, String init_fname, String init_lname, Integer init_year, Integer init_month, Integer init_date, String init_address)`

This operation must create some representation for a new person within your system.
All persons must have a unique username, along with a first and last name, birth date (in Year, Month, Date format) and address.
All persons are initially unemployed but have the potential to become (regular) workers, managers, or pilots.
Persons serve various important roles in our system, and some operations can't be completed without them.

`displayPersons()`

This operation must display the information for all of the persons in the system.
The information for a person must include their username, first and last name, birthdate and address, along with information about their status as a manager or pilot.  Please see the test case examples for more details.
hireWorker(String service_name, String user_name)

This operation must change the status of a person such that the person is now considered to be an employee of the designated delivery service.
A person can be an employee at multiple delivery services at the same time, but with some exceptions.
A person cannot be hired by a delivery service if they are managing a different delivery service.
A person cannot be hired by a delivery service if they are piloting drones for a different delivery service.
A person can't manage, fly drones or otherwise support a delivery service unless they've been hired by that service.

`fireWorker(String service_name, String user_name)`

This operation must change the status of a person such that the person is no longer considered to be an employee of the designated delivery service.
A person cannot be fired from a delivery service if they are managing that service.
A person cannot be fired from a delivery service if they are piloting drones for that service.

`appointManager(String service_name, String user_name)`

This operation must change the status of a person such that the person is now the manager of the delivery service.
The person must be an employee of the delivery service.
This appointment cannot occur if the person is flying any drone, or if they are working at two or more delivery services.
If the appointment is otherwise successful, then this person replaces the previous manager.  A delivery service can only have one manager at a time.
Managers must be established to to allow a delivery service to train pilots and collect revenue.

`trainPilot(String service_name, String user_name, String init_license, Integer init_experience)`

This operation must change the status of a person such that the person now has a valid pilot's license, and an initial amount of piloting experience.
The person must be an employee of the delivery service.
The person can't complete the training if they are currently a manager.
The delivery service cannot grant the license unless they have a valid manager.
The license that is earned by the person belongs to that person and is "universal": the person can use that same license to fly drones with different delivery services.
The experience represents the number of successful drone flights that the pilot has flown.  This value should be incremented by one for each successful flight - specifically, a single flyDrone() call - that the pilot conducts in the system.  A single flyDrone() call counts as one flight whether it's a single drone or a swarm of two or more drones.
Employees must have a valid license to be allowed to fly drones.

`appointPilot(String service_name, String user_name, Integer drone_tag)`

This operation must change the status of a person such that the person is now piloting the specifically named/tagged drone for the delivery service.
The person must be an employee of the delivery service and must have a valid pilot's license.
The person can't pilot the drone if they are currently a manager, or if they are working at two or more delivery services.
A person can fly multiple drones, whether individual or as swarms.
The pilot takes direct control of the drone, whether the drone was being directly controlled by a different pilot, or was flying a part of a swarm.
Pilots must control the drones to be able to fly/move them to different locations.

`joinSwarm(String service_name, Integer lead_drone_tag, Integer swarm_drone_tag)`

This operation must change the status of the swarm drone such that the swarm drone now takes all of its flight directions from the lead drone instead of directly from a pilot.
The lead and swarm drones must belong to the same delivery service, and must be at the same location.
The lead drone must be directly controlled by a pilot (i.e., the lead drone can't also be a swarm drone).
Drones in a swarm act "as one": when flying to a new location, the entire swarm must (be able to) move to the new location, or they must not move either way, they must stay together.
This "swarm" capability allows pilots to control multiple drones more easily.

`leaveSwarm(String service_name, Integer swarm_drone_tag)`

This operation must change the status of the swarm drone such that the swarm drone now takes all of its flight directions directly from the pilot of its current lead drone.  Effectively, the drone no longer belongs to the swarm.
Being able to form swarms gives the delivery services the ability to invest in small fleets of drones that can work together to delivery large amounts of goods if needed without having to purchase larger, more expensive (and maintenance-prone) drones.

`collectRevenue(String service_name)`

This operation must change the status of the delivery service by transferring all of the sales earned by its drone's sales accounts to its revenue account.
The delivery service cannot transfer the sales to revenue unless they have a valid manager.
At the end of this operation, the all of the sales from the delivery service's drone should be added to the current revenue value.  Also, the sales value for each drone should be reset to zero.
Modifications to Existing Requirements
The following operations work as they did before with some additional constraints and/or requirements:
`loadIngredient(String service_name, Integer drone_tag, String barcode, Integer quantity, Integer unit_price)`
`loadFuel(String service_name, Integer drone_tag, Integer petrol)`
These operations will only be executed if the delivery service has at least one "regular" (i.e., non-manager, non-pilot) employee/worker.

`flyDrone(String service_name, Integer drone_tag, String destination_name)`
This operations will only be executed if the drone - either individual, or as the leader of a swarm - has a pilot with a valid license.
The joinSwarm() and leaveSwarm() capabilities mean that the flyDrone() operation must be able to move a swarm of two or more drones as well as an individual drone.  This means accounting for the fuel and destination space requirements for the swarm as a whole, and ensuring that the swarm doesn't get split up (i.e., leave one or more drones behind at the previous location).
calcDistance(GeoLocation destination)

Here is the slightly modified/updated code for calculating the distance between two locations. We've added a check to the function that the distance to the same destination will be zero.  The locations are objects that will provide their X and Y coordinates as needed:
```java
Integer calcDistance(GeoLocation destination) {
  if (this.equals(destination)) {
    return 0;
  } else {
    return 1 + (int) Math.floor(Math.sqrt(Math.pow(getXCoord() - destination.getXCoord(), 2) + Math.pow(getYCoord() - destination.getYCoord(), 2)));
  }
}
```

This is normally executed as Integer distance = <location object 1>.calcDistance(<location object 2>)

# How We Will Evaluate Your Submission
Your code will be [1] compared to your design artifacts for consistency; and, [2] tested to ensure that it works correctly and meets the functional requirements.  We will evaluate your submission based on these two main factors:

[1] Consistency: Are your design artifacts consistent with your source code?

The classes in your Design Class Diagram must match the classes in your source code in terms of attributes, data types, parameters, cardinalities, methods, navigation aspects, accessibility (e.g., public/protected/private), etc.  Also, the behavior of your flyDrone() functionality described by your Sequence Diagram must match the operational behavior of your source code as well.  The messages between objects, values returned, parameters passed, along with the selection and iteration structures used, must be reflected with the appropriately labeled solid and dotted arrows, properly sequenced and nested alt and loop boxes, etc.  And all diagrams must use clear, correct and consistent notation per the class slides and/or the Larman text.

[2] Correctness: Does your source code work correctly with respect to the problem description and requirements?

The main functionality tests will be against the "logically-oriented" errors described above - for example, drones being required to have enough fuel to move to a new location, etc.  There are also three (3) other common types of errors that you should handle appropriately for all of the designated procedures (in addition to the "logically-oriented" errors mentioned above:

A. If an identifier for an instance is invalid, doesn't exist, etc., then referencing that identifier will very likely cause a null reference error.  In these cases, simply halt execution of the procedure and display an appropriate error message.

B. If creating a new instance, then be sure that the identifier for the instance will be distinct (i.e., unique) within that class.  The init_barcode parameter is the identifier that must be unique for Ingredients.  The init_name parameter is the identifier that must be unique for Locations, Delivery Services and Restaurants.  For Drones, the service_name parameter must represent a valid Delivery Service that is buying the Drone, and then the Drone must have a distinct init_tag value within that service.  The overall combination of (service_name, init_tag) must be distinct for each Drone.  Don't create instances with duplicate identifiers, because this will very likely cause problems later. [And yes - this is a slight change from the original requirements - but it's still a reasonable change to implement within this phase.]

C. All "physical values" - for example, drone package-carrying capacities, fuel levels, ingredient quantities and weights, etc. - must be zero or greater.  If one or more of these parameter values is negative, then simply halt execution of the procedure and display an appropriate error message.  Note that the X and Y coordinates for a Location are permitted to be negative.

The general protocol that we will follow for errors of this nature: display an appropriate error message, and then otherwise avoid executing any actions that would change the state of the system.  And if you've already changed the state of the system (even partially), then you must revert it back to its original "value" immediately before the operation was executed.

I will NOT test your program against: [1] undefined commands; [2] commands with the incorrect number of parameters; or, [3] invalid parameter data types (i.e., strings will be valid strings, numbers will be valid integers).
