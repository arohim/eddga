# Android Clean Architecture Generator

Intellij Idea, Android Studio plugin for JSON to POJO conversion.

This is an internal plugin but openly to use, I make for using very specific Android Clean Architecture [https://github.com/bufferapp/android-clean-architecture-boilerplate]
        The road map of this project is generate the whole things from plain JSON all the way through to Domain Layer and this plugin will be extend to generate android UI template
        Currently, Generate Kotlin POJO files from JSON <br>
          - Domain POJO <br>
          - Data POJO <br>
          - Cache POJO <br>
          - Remote POJO <br>
          And Generate mapper with Unit testing is coming soon.

          This project is on top of https://github.com/robohorse/RoboPOJOGenerator <br>
          Thank you to Vadim Shchenev to make me easier to extend.
          
Supports: primitive types, multiple inner JSONArrays.

<p><img src="images/tutorial_v3.gif" width="100%" height="50%"></p>


# Download
get it and install from <a href="https://plugins.jetbrains.com/plugin/12213-android-clean-architecture-generator">plugin repository</a> or simply find it in "Preferences" -> "Plugins" -> "Browse Repositories" -> "Android Clean Architecture Generator"

<p>
Waiting for image
</p>

# How to use

Select target package -> new -> Domain POJO from JSON
Select target package -> new -> Data POJO from JSON
Select target package -> new -> Cache POJO from JSON
Select target package -> new -> Remote POJO from JSON

<p>
Waiting for image
</p>

put JSON into window and select target POJO type

<p>
Waiting for image
</p>

see log of changes

<p>
<img src="images/plugin_log_v3.png" height="200">
</p>

# People, who help
<ul>
<li>
<a href="https://github.com/arohim">a-rohim</a> - Rewrite as needed
</li>
<li>
<a href="https://github.com/wafer-li">wafer-li</a> - Kotlin support (release 1.7)
</li>
<li>
<a href="https://github.com/ccqy66">ccqy66</a> - toString support (release 1.8.1)
</li>
</ul>

# About
Copyright 2016 Vadim Shchenev, and licensed under the MIT license. No attribution is necessary but it's very much appreciated. Star this project if you like it.
