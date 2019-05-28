# Forgelin
Fork of [Emberwalker's Forgelin](https://github.com/Emberwalker/Forgelin).

## Additions
- Shades the Kotlin standard library, runtime, coroutines-core, and reflect libraries so you don't have to.
- Provides a Forge `ILanguageAdapter` for using Kotlin `object` classes as your main mod class.

## Usage
```groovy
repositories {
	jcenter()
	maven {
		url "http://maven.shadowfacts.net/"
	}
}

dependencies {
	compile group: "net.shadowfacts", name: "Forgelin", version: "LATEST_VERSION"
}
```

All versions can be seen [here](http://maven.shadowfacts.net/net/shadowfacts/Forgelin/).

**Note:** You must have the `jcenter()` call in your `repositories` block. JCenter is used to host the Kotlin coroutines libraries.
