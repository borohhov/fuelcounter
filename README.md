# fuelco


# How to run
Build via your favourite tools and run the file via "java -jar <<filename>filename>.jar.

It is configured to run on port 8080, so you can run it via http://localhost:8080

# Endpoints

    "POST /records/bulk",
    Bulk data entry, use JSON in Body. Example:
    {
    "records": [
        {
            "fuelType": "PETROL_95",
            "price": 15,
            "volume": 20,
            "date": "2015-12-10",
            "driverId": 1234
        },
        {
            "fuelType": "PETROL_95",
            "price": 15.3,
            "volume": 20.9,
            "date": "2012-12-11",
            "driverId": 1234
        }
    ]
}

    "GET /records-by-month/{month}",
    Month format: YYYY-MM, example: 2019-10
    "GET /statistics",
    
    "GET /{driverId}/records-by-month/{month}",
    driverId is integer, Month format: YYYY-MM, example: 2019-10
    
    "GET /{driverId}/statistics",
    driverId is integer
    
    "DELETE /records/{recordId}",
    
    "POST /records",
    Single entry addition via URI parameters, example:
    /rest/records?driverId=1234&price=15&volume=20&date=12.11.2010&fuelType=PETROL_98
    
    "GET /amount-by-month",
    
    "GET /{driverId}/amount-by-month"

Homework application.

# Self-assessment
<ul>
	<li>RESTful API with JSON payloads in response - <i>YEP</i></li>
  
<li>Language: Java/Kotlin - <i>Java</i></li>
<li>Frameworks: You can use any framework (preferably Spring boot) but keep in mind that usage of framework should be reasonable and architecture of application should allow to switch frameworks.
<p><i>Designing this application with most logic decoupled from Spring would be break the KISS principle - Spring and then switching to something else.Spring JPA and Spring @Controller used. Switching frameworks is possible with switching a JPA provider with little hassle. Switching the provider for API is more work, but at the same time it's just one class. </i><p></li>

<li>Use Gradle or Maven (use dependencies from public repositories) - <i>Maven</i></li>
<li>Unit tests at least for business logic(use any framework of your choice) - <i>used Mockito and JUnit</i> </li>
<li>Use default Maven project structure - <i>Yep</i></li>
<li>Write production-ready code (clean, modular, testable) - <i>hope so, see Technical Debt below</i></li>
<li>Apply object oriented design principles</li>
</ul>
# Technical Debt
<ul>
<li>FuelRecord POJO has 2 fields it does not need to have, but the interface-based projections wouldn't work otherwise. The downside is quite small atm, but if this application grew bigger, the number of such transient fields would grow and cause confusion. 
<li>Testing can always be better. The API is tested with MockMVC and compared against predefined JSON strings. A more comprehensive testing for critical parts (parsing JSON and comparing Lists against business requests) could be done, but it's not feasible for the current scope</li>
</ul>
