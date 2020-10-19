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

apply(plugin = "jacoco")

tasks {
    val debugCoverageReport by registering(JacocoReport::class)
    debugCoverageReport {
        dependsOn("testDebugUnitTest")

        val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/debug")
        val coverageSourceDirs = arrayOf("src/main/kotlin")
        val executionDataDirs = fileTree("$buildDir") {
            setIncludes(listOf("jacoco/testDebugUnitTest.exec"))
        }

        classDirectories.setFrom(files(kotlinClasses))
        additionalSourceDirs.setFrom(files(coverageSourceDirs))
        sourceDirectories.setFrom(coverageSourceDirs)
        executionData.setFrom(executionDataDirs)
    }
}
