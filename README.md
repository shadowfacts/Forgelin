# Forgelin
Fork of [Emberwalker's Forgelin](https://github.com/Emberwalker/Forgelin).

## Additions
- Shades the Kotlin standard library, runtime, and reflect libraries so you don't have to.
- Provides a Forge `ILanguageAdapter` for using Kotlin `object` classes as your main mod class.

## Usage
```groovy
repositories {
	maven {
		url "http://mvn.rx14.co.uk/shadowfacts/
	}
}

dependencies {
	compile group: "net.shadowfacts", name: "Forgelin", version: "LATEST_VERSION"
}
```

All versions can be seen [here](http://mvn.rx14.co.uk/shadowfacts/net/shadowfacts/Forgelin/).
