# Jalali Date Picker
![JitPack](https://img.shields.io/jitpack/version/com.github.williamgates99/jalalidatepicker)&nbsp;![GitHub](https://img.shields.io/github/license/WilliamGates99/JalaliDatePicker)&nbsp;![platform](https://img.shields.io/badge/platform-android-success)

**Jalali Date Picker** is a highly customizable and user-friendly date picker component designed for **Android Jetpack Compose**.<br>
Tailored specifically for the Persian (Jalali) calendar, it empowers users to select dates effortlessly through an elegant, modern interface.<br>
The library offers a seamless experience with fluid animations, intuitive controls, and a sleek design, making it an ideal choice for developers seeking to enhance their apps with a visually appealing and culturally relevant date selection tool.
<p align="middle">
  <img src="/resources/record_english.gif" width="33%"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="/resources/record_persian.gif" width="33%"/>
</p>

## Installation
### 1. Add the JitPack repository to your build file:
<details open>
<summary><b>Kotlin DSL</b></summary>
  
```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```
</details>

<details>
<summary><b>Groovy</b></summary>
  
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
</details>

### 2. Add the dependency to your build.gradle.kts file:
<details open>
<summary><b>Kotlin DSL</b></summary>
  
```kotlin
dependencies {
    implementation("com.github.williamgates99:jalalidatepicker:1.0.4")
}
```
</details>

<details>
<summary><b>Groovy</b></summary>
  
```groovy
dependencies {
    implementation 'com.github.williamgates99:jalalidatepicker:1.0.4'
}
```
</details>

## Usage
Simply use the `JalaliDatePicker` composable:
```kotlin
JalaliDatePicker(
    onSelectedDateChange = { newSelectedDate ->
        val (year, month, day) = newSelectedDate
        println("Selected Jalali Date: $year/$month/$day")
    },
    modifier = Modifier.fillMaxWidth()
)
```

</br>Tailor the `JalaliDatePicker` to your needs with these parameters:
 * `initialSelectedDate`: The initial selected date in the Gregorian calendar.
 * `selectableYearsRange`: An optional range of years that can be selected.
 * `isSelectFromFutureEnabled`: If `false`, the maximum selectable date will be the current date. If `true`, future dates can be selected.
 * `datePickerDefaults`: Configuration options for the date picker, such as the number of selectable years and future years.
 * `dividersHeight`: The height of the dividers between the date picker wheels.
 * `dividersColor`: The color of the dividers.
 * `textStyle`: The text style to use for the date picker labels.
 * `showMonthNumber`: If `true`, the month number will be displayed along with the month name.
 * `onSelectedDateChange`: A callback that is invoked when the selected date changes.

## Contribution
We welcome any contributions from the community!<br>
Found a bug or have a feature idea? Please open an [issue](https://github.com/WilliamGates99/JalaliDatePicker/issues) on our GitHub repository to let us know.<br>
Ready to contribute code? Simply fork the repository and submit a pull request.

## License
This project is licensed under the [MIT License](LICENSE).
