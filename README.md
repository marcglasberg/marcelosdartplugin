# Marcelo's Flutter/Dart Essentials

## Features

**1) Show Class Separators.** Draws horizontal line separators above **class, enum and extension**
definitions in the Editor, similar to the native "Show Method Separator" setting, but applied to
classes instead. This enhances code readability, making it easier to distinguish where one class
ends and another begins.

<img src="https://github.com/marcglasberg/marcelosdartplugin/raw/master/PluginEffect_Classes.jpg" width="1200" alt="Plugin effect for classes">
<p>&nbsp;</p>

**2) Show Test and Test Group Separators.** Draws horizontal line separators above `test()`
and `group()` method calls in the Editor, for files that end in `_test.dart`. That's similar to the
native "Show Method Separator" setting, but applied to tests instead. This enhances code
readability, making it easier to distinguish where one test or test group ends and another begins.
This plugin is needed because tests are function *calls*, not function *definitions*, which is why
they don't have the native method separators applied to them.

<img src="https://github.com/marcglasberg/marcelosdartplugin/raw/master/PluginEffect_Tests.jpg" width="1200" alt="Plugin effect for tests">

**3) Show Bdd Separators.** This is for the <b>BDD Framework</b> package:
<a href='https://pub.dev/packages/bdd_framework'>pub.dev/packages/bdd_framework</a></html>.
Draws horizontal line separators above `Bdd()` calls, but also between the BDD
keywords: `.given(...)`, `.when(...)`, `.then(...)`
and `.example(...)()`. Also, in the BDD body you may want to separate the
implementation of the given/when/then parts. To that end, it will also add separators above
any comments that start with `// Given:`, `// When:`, `// Then:`,
`// Given/When:` or `// When/Then:`. Note all these separators
are only added for files with names that start with "bdd_" or end with "_test.dart".

## Configuration

To configure this plugin:

1. Open IntelliJ IDEA.
2. For Windows, go to **File** -> **Settings** (or press `Ctrl+Alt+S`). For MacOS, go to **IntelliJ
   IDEA** -> **Preferences** (or press `Command+`).
3. In the Settings (or Preferences) window, on the left panel, click **Marcelo's Flutter/Dart
   Essentials**.
4. Configure the plugin, then click **Apply**, or **OK** to close the Settings (or Preferences)
   window.

You may also turn on a related native setting called "Show Method Separator":

1. In the Settings (or Preferences) window, on the left panel, click to expand **Editor**.
2. Under **Editor**, click to expand **General**.
3. Under **General**, click on **Appearance**.
4. On the right panel, find the checkbox **Show method separators**.

## Note:

This plugin has my name on it, because:

1) It will provide support for the 15+ plugins that I've developed, which you can explore
   at [https://pub.dev/publishers/glasberg.dev/packages](https://pub.dev/publishers/glasberg.dev/packages).

2) It will incorporate features that reflect my personal preferences for IDE functionalities. While
   it may prioritize a specific approach and not always offer extensive customization options, you
   will have the flexibility to disable any features that do not align with your requirements.

Currently, this plugin only includes the ability to draw separators. However, I plan to gradually
enhance its capabilities in the future.

<img src="https://github.com/marcglasberg/marcelosdartplugin/raw/master/PluginSettings.png" width="1200" alt="Plugin settings">
