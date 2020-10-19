/*
 * Copyright (C) 2020 Xizhi Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.gradle.api.JavaVersion

object Configurations {
    val sampleAppId = "me.xizzhu.android.kvs.sample"
}

object Versions {
    object Kvs {
        const val code = 100
        val name: String by lazy {
            "${code / 10000}.${(code % 10000) / 100}.${code % 100} " +
                    "(${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))})"
        }
    }

    object AndroidMaven {
        const val classpath = "2.1"
    }

    object Sdk {
        const val classpath = "4.1.0"
        const val buildTools = "30.0.2"
        const val compile = 30
        const val min = 21
        const val target = 30
    }

    val cmake = "3.10.2"
    val java = JavaVersion.VERSION_1_8

    object Kotlin {
        const val core = "1.4.10"
    }

    object AndroidX {
        const val annotation = "1.1.0"
    }
}

object Dependencies {
    object Sdk {
        const val classpath = "com.android.tools.build:gradle:${Versions.Sdk.classpath}"
    }

    object AndroidMaven {
        const val classpath = "com.github.dcendents:android-maven-gradle-plugin:${Versions.AndroidMaven.classpath}"
    }

    object Kotlin {
        const val classpath = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.core}"
        const val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.Kotlin.core}"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:${Versions.AndroidX.annotation}"
    }
}
