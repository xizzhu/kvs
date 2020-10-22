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

package me.xizzhu.android.kvs

import java.io.File
import kotlin.test.*

class KvsExtensionTest {
    private lateinit var kvs: Kvs

    @BeforeTest
    fun setup() {
        kvs = newKvs {
            dir = getTestDir().apply { mkdirs() }.absolutePath
        }
    }

    private fun getTestDir(): File = File(System.getProperty("java.io.tmpdir"), "KvsTest")

    @AfterTest
    fun tearDown() {
        kvs.close()
        getTestDir().deleteRecursively()
    }

    @Test
    fun testBasicExtension() {
        assertNull(kvs["key"])

        kvs["key"] = byteArrayOf(1, 2, 3, 4, 5)
        assertTrue(byteArrayOf(1, 2, 3, 4, 5).contentEquals(kvs["key"]))
        assertNull(kvs["non-exist"])

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertFalse(kvs.remove("non-exists"))
        assertNull(kvs["non-exist"])

        assertFalse(kvs.remove("key"))
    }

    @Test
    fun testBooleanExtension() {
        assertNull(kvs["key"])
        assertFalse(kvs.getBoolean("key"))
        assertTrue(kvs.getBoolean("key", true))

        kvs["key"] = true
        assertTrue(kvs.getBoolean("key"))

        kvs["key"] = false
        assertFalse(kvs.getBoolean("key", true))

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertFalse(kvs.getBoolean("key"))
        assertTrue(kvs.getBoolean("key", true))
    }

    @Test
    fun testDoubleExtension() {
        assertNull(kvs["key"])
        assertEquals(0, kvs.getDouble("key").compareTo(0.0))
        assertEquals(0, kvs.getDouble("key", 1989.64).compareTo(1989.64))

        kvs["key"] = 64.1989
        assertEquals(0, kvs.getDouble("key").compareTo(64.1989))

        kvs["key"] = 89.64
        assertEquals(0, kvs.getDouble("key").compareTo(89.64))

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertEquals(0, kvs.getDouble("key").compareTo(0.0))
        assertEquals(0, kvs.getDouble("key", 1989.64).compareTo(1989.64))
    }

    @Test(expected = KvsException::class)
    fun testDoubleExtension_withException() {
        kvs["key"] = byteArrayOf(1, 2, 3, 4)
        kvs.getDouble("key")
    }
}
