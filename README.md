# NHL Stats API Java

This API allows users to access NHL.com's complete stats compilation and database

You can also check out [this repository](https://github.com/dword4/nhlapi) for a more complete and condensed list of api endpoints

## Getting Started

You can link the api to your project via Gradle or maven by finding the lastest version and linking it

http://maven.theprogrammingturkey.com/index.php?dir=com%2Ftheprogrammingturkey%2Fnhlapi%2FNHLAPI-Java

Maven
```
<repository>
	<id>turkey-repo</id>
    <name>TheProgrammingTurkey Repo</name>
    <url>http://maven.theprogrammingturkey.com</url>
</repository>
```
```
<dependency>
	<groupId>com.theprogrammingturkey.nhlapi</groupId>
	<artifactId>NHLAPI-Java</artifactId>
	<version>(versionNumber)</version>
</dependency>
```

Gradle
```
repositories {
	mavenCentral()
	maven{
		name "NHLAPI"
		url "http://maven.theprogrammingturkey.com"
	}
}

dependencies {
	compile "com.theprogrammingturkey.nhlapi:NHLAPI-Java:(versionNumber)"
}
```

## Examples

See [Examples](https://github.com/Turkey2349/NHLStatsAPI-Java/tree/master/examples/com/theprogrammingturkey/nhlapi) for examples running the API

## Contributing

Feel free to contribute to this project!
All that I ask is that you follow my formatting style as best as you can and also comment any public facing methods and code that isn't obvious.
Also please include a message in the pull request with why you made the changes that you did.

## Versioning

I try to follow and use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/Turkey2349/NHLStatsAPI-Java/tags). 

## Authors

* **Ryan Turk (A.K.A Turkey2349)** - *Initial work* - [Github](https://github.com/Turkey2349)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

Thank you to NHL.com for having all of these stats available to everyone!
