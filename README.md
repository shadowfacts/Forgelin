# Forgelin
Fork of [Emberwalker's Forgelin](https://github.com/Emberwalker/Forgelin) with some sprinkles on top.

## Additions
- Extensions for Minecraft/Forge code. See them in the [extensions package](https://github.com/shadowfacts/Forgelin/tree/master/src/main/kotlin/net/shadowfacts/forgelin/extensions/).

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

You _can_ shade Forgelin but it's not recommended, as it means users may be unintentionally downloading the entire Kotlin standard library, reflect library, and runtime multiple times.
