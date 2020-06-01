# ML-Ip-Tracker


Web app to retrieve information about a given IP

#Requirements


* Scala 2.12.10
* sbt 0.13.17
* MongoDB 4.2.6 or higher

#Usage

* run with 'sbt run' ml-ip-tracker directory.

#Routes


* GET    /meli-ip-tracker/search

Initial view, enter here the IP you wish to get information about.
This will call /meli-ip-tracker/result and show the desired information.

* GET    /meli-ip-tracker/ip-information/:ip

Service to get IP information as JSON.

Response

````
{
    ip: String,
    date: String,
    country: String,
    isoCode: String,
    capital: String,
    languages: List[String],
    currency: {
        currency: String,
        usdRate: Option[Double]
    },
    hours: List[String],
    distance: Int
}

````

* GET     /meli-ip-tracker/min-search-distance
* GET     /meli-ip-tracker/max-search-distance

These routes respectively get the information about the requested IP closest and furthest to Buenos Aires

Response

````
{
    ip: String,
    country: String,
    distance: Int
}

````

GET       /meli-ip-tracker/avg-search-distance

Get the average distance for all requests as an Integer. 
