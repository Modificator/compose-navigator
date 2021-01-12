# Compose Navigator
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/modificator/Compose/compose-navigator/images/download.svg) ](https://bintray.com/modificator/Compose/compose-navigator/_latestVersion)

Easier to complete Jetpack Compose navigation

## Getting started

In your `build.gradle`:

```groovy
dependencies {
    implementation 'com.patchself:compose-navigator:0.1.3'
}
```
Or if you use gradle.kts, in your`build.gradle.kts`
```kotlin
dependencies {
    implementation("com.patchself:compose-navigator:0.1.3")
}
```

## Usage

1. Add config your compose activity
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set open app first page
        navigationController.initController(SplashPage())
        setContent {
            navigationController.viewContent()
        }
    }

    override fun onBackPressed() {
        // navigator go back handler
        if (!navigationController.onBackPressed()){
            super.onBackPressed()
        }
    }
}

```

2. Write your custom page
```kotlin
class CustomPage : PageController() {
    override fun getId() = R.id.CustomPage

    /**
     * write page content in this method
     */
    @Composable
    override fun screenContent() {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Navigator Sample") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(asset = Icons.Filled.ArrowBack)
                    }
                },
                elevation = 4.dp
            )
        }) {
        }
    }
}
```
3. In your Custom Page,which extend `PageController`, you can use `navigateTo(PageController)` to new page, and `navigateBack()` go back to last page
4. You can use `resetTo(pageId)` back to specify page, and change page args like
   ```kotlin
   resetTo<HomePage>(R.id.HomePage){
       argsOfHomePage = newValue
   }
   ```
![preview](./images/preview.gif "")