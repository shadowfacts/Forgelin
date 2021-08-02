# Forgelin

Fork of [Shadowfacts Forgelin](https://github.com/shadowfacts/Forgelin).

## Additions

- Shades the Kotlin standard library, runtime, coroutines-core, and reflect libraries so you don't have to.
- Provides a Forge `ILanguageAdapter` for using Kotlin `object` classes as your main mod class.

## Usage

```groovy
repositories {
	jcenter()
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation 'com.github.Licious17:Forgelin:1.9.0'
}
```

```kotlin
@Mod(
        modid = MyMod.MOD_ID,
        name = MyMod.MOD_NAME,
        version = MyMod.VERSION,
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.12.2]"
)
object MyMod {

	const val MOD_ID = "mymod"
    const val MOD_NAME = "MyMod"
    const val VERSION = "1.0.0"

}
```

Full credits to the og authors this simply updates Kotlin and the libraries version.
[EmberWalker](https://github.com/Emberwalker)
[ShadowFacts](https://github.com/shadowfacts)

**Note:** You must have the `jcenter()` call in your `repositories` block. JCenter is used to host the Kotlin coroutines libraries.
