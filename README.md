# point_mainapp_demo Android Application

## Project status

<p align="center">
<a href='https://android-arsenal.com/api?level=21'><img alt='MIN API' src='https://img.shields.io/badge/min%20api-23-blue?style=for-the-badge'/></a>
<a href='https://developer.android.com/studio/releases#4.2.1'><img alt='ANDROID STUDIO PLUGIN VERSION' src='https://img.shields.io/badge/android%20studio-4.2.1-blue?style=for-the-badge'/></a>
<a href='https://docs.gradle.org/7.4.0/release-notes.html'><img alt='GRADLE VERSION' src='https://img.shields.io/badge/gradle-7.4.0-blue?style=for-the-badge'/></a>
</p>

## What is this?

This is a demo application that use the **Mercadopago Integration SDK** to connect a thirty party app with the **MercadoPago presencial Payments ecosystem** and specific **hardware capabilities** like Camera for Barcode Reader, embedded Printer, bluetooth, and others.

## How to download the Integration Kit?

To download the **Integration Kit**, go to the **Releases** section, select the last available version and go into the assets to find:
- SDK Integration lib(aar)
- MainApp Demo (apk)
- SDK documentation (zip)

For more information, check [the MainApp playbook](https://www.mercadopago.com.br/developers/es/docs/main-apps/landing)

## Contributing

This is not written in rock like you must be guessing so write us and let's make everyone's life a little easier.

For more information about in house distribution please check [the wiki](https://sites.google.com/mercadolibre.com/mobile/arquitectura/in-house-distribution-mds)

## Repo usage

Create branch or fork from *develop*, then push or create pull requests (if you don't have access) to that branch.

## Developing and contribuing

### Running checks

We run [SCA](https://github.com/Monits/static-code-analysis-plugin) checks for static code analysis and some built in Android lints among all tests. To run everything:

``` bash
./gradlew check
```

and if you want to check an specific module you can:

``` bash
./gradlew module:check
```

### Compiling locally

#### app

For testing your module locally you can run the integrated **app**. If you want to build it:

``` bash
./gradlew app:assembleDebug
```

or if you have already a device connected to **adb**:

``` bash
./gradlew app:installDebug
```

### Deploying

For deploying you **must** create a branch named _release/X.Y_ where X.Y is the major and minor number to be published. Once you have created it, push your changes and run the command `fury create-version`.

## FAQ

### Having issues accessing to files though URI?

If you are having a `NullPointerException`, this is because you are missing the provider in your manifest to provide your app the permission to access to files. Add this snippet to your `AndroidManifest.xml` from your app file:

``` xml

<!-- Needed for secure management of files. It is not longer permitted to access a file with file:///-->
<!-- https://developer.android.com/reference/android/support/v4/content/FileProvider.html -->
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/resource_file_provider_path" />
</provider>
```

And create `resource_file_provider_path.xml` under `res/xml`, which content should be:

``` xml

<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path name="external_files" path="."/>
</paths>

```
