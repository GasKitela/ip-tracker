#-Ip-Tracker


Web app to retrieve information about a given IP

#Requirements


* Scala 2.12.10
* sbt 0.13.17
* MongoDB 4.2.6 or higher

#Usage

* run with 'sbt run' ip-tracker directory.

#Routes


* GET    /ip-tracker/search

Initial view, enter here the IP you wish to get information about.
This will call /ip-tracker/result and show the desired information.

* GET    /ip-tracker/ip-information/:ip

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

* GET     /ip-tracker/min-search-distance
* GET     /ip-tracker/max-search-distance

These routes respectively get the information about the requested IP closest and furthest to Buenos Aires

Response

````
{
    ip: String,
    country: String,
    distance: Int
}

````

GET       /ip-tracker/avg-search-distance

Get the average distance for all requests as an Integer. 

#Notes

* Database name and collection are specified in application.conf.