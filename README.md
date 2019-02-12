# Forgelin
Fork of [Emberwalker's Forgelin](https://github.com/Emberwalker/Forgelin).

All versions can be found [here](http://maven.shadowfacts.net/net/shadowfacts/Forgelin/).

## Additions
- Shades the Kotlin standard library, runtime, and reflect libraries so you don't have to.
- Provides a Forge `IModLanguageProvider` for using Kotlin `object` classes as your main mod class and adds support for
`object` instances for `@Mod.EventBusSubscriber`

## Usage
Set up your default Kotlin dev environment (IDEA can help you with that), then in your `build.gradle`:
```groovy
repositories {
	jcenter()
	maven { url 'https://maven.shadowfacts.net' }
}

dependencies {
	compile group: 'net.shadowfacts', name: 'Forgelin', version: 'LATEST_VERSION'
}
```
**Note:** You must have the `jcenter()` call in your `repositories` block. JCenter is used to host the Kotlin coroutines libraries.

Then in your `mods.toml`:
```toml
modLoader="kotlinfml"

[[dependencies.your_mod_name]]
    modId="forgelin"
    mandatory=true
    ordering="NONE"
    side="BOTH"
    # optionally, specify version range for Forgelin
```

Finally, replace `FMLModLoadingContext` references in your code with `FMLKotlinModLoadingContext`. For more info, check
out test sources [here](https://github.com/shadowfacts/Forgelin/tree/master/src/test/kotlin/net/shadowfacts/forgelin).
