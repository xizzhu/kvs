KVS - A Key-Value Store for Android
===================================

[![Build Status](https://img.shields.io/travis/com/xizzhu/kvs)](https://travis-ci.com/xizzhu/kvs)
[![Coverage Status](https://img.shields.io/coveralls/github/xizzhu/kvs.svg)](https://coveralls.io/github/xizzhu/kvs)
[![API](https://img.shields.io/badge/API-21%2B-green.svg?style=flat)](https://developer.android.com/about/versions/android-5.0.html)
[![Languages](https://img.shields.io/badge/languages-Kotlin-blue.svg?longCache=true&style=flat)](https://kotlinlang.org/)

KVS is a simple wrapper on top of the [Lightning Memory-Mapped Database (LMDB)](https://symas.com/lmdb/) key-value store.

Usage
-----

```kotlin
// Create a new Kvs instance:
val kvs = newKvs {
    dir = File(filesDir, "kvs").apply { mkdirs() }.absolutePath
}

// Put values into the store:
kvs.edit {
  it["key"] = "value"
  it["answer to everything"] = 42
}

// Get values from the store:
val str = kvs.getString("key")
val int = kvs.getInt("answer to everything")

// Close the store:
kvs.close()
```

License
-------
    Copyright (C) 2020 Xizhi Zhu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
