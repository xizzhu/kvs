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
        assertFalse(kvs.contains("key"))
        assertNull(kvs["key"])

        kvs["key"] = byteArrayOf(1, 2, 3, 4, 5)
        assertTrue(kvs.contains("key"))
        assertTrue(byteArrayOf(1, 2, 3, 4, 5).contentEquals(kvs["key"]))
        assertFalse(kvs.contains("non-exist"))
        assertNull(kvs["non-exist"])

        assertTrue(kvs.remove("key"))
        assertFalse(kvs.contains("key"))
        assertNull(kvs["key"])
        assertFalse(kvs.remove("non-exists"))
        assertFalse(kvs.contains("non-exist"))
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

    @Test
    fun testFloatExtension() {
        assertNull(kvs["key"])
        assertEquals(0, kvs.getFloat("key").compareTo(0.0F))
        assertEquals(0, kvs.getFloat("key", 1989.64F).compareTo(1989.64F))

        kvs["key"] = 64.1989F
        assertEquals(0, kvs.getFloat("key").compareTo(64.1989F))

        kvs["key"] = 89.64F
        assertEquals(0, kvs.getFloat("key").compareTo(89.64F))

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertEquals(0, kvs.getFloat("key").compareTo(0.0F))
        assertEquals(0, kvs.getFloat("key", 1989.64F).compareTo(1989.64F))
    }

    @Test(expected = KvsException::class)
    fun testFloatExtension_withException() {
        kvs["key"] = byteArrayOf(1, 2, 3)
        kvs.getFloat("key")
    }

    @Test
    fun testIntExtension() {
        assertNull(kvs["key"])
        assertEquals(0, kvs.getInt("key"))
        assertEquals(8964, kvs.getInt("key", 8964))

        kvs["key"] = 6489
        assertEquals(6489, kvs.getInt("key"))

        kvs["key"] = 64
        assertEquals(64, kvs.getInt("key"))

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertEquals(0, kvs.getInt("key"))
        assertEquals(89, kvs.getInt("key", 89))
    }

    @Test(expected = KvsException::class)
    fun testIntExtension_withException() {
        kvs["key"] = byteArrayOf(1, 2, 3)
        kvs.getInt("key")
    }

    @Test
    fun testLongExtension() {
        assertNull(kvs["key"])
        assertEquals(0L, kvs.getLong("key"))
        assertEquals(8964L, kvs.getLong("key", 8964L))

        kvs["key"] = 6489L
        assertEquals(6489L, kvs.getLong("key"))

        kvs["key"] = 64L
        assertEquals(64L, kvs.getLong("key"))

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertEquals(0L, kvs.getLong("key"))
        assertEquals(19890604L, kvs.getLong("key", 19890604L))
    }

    @Test(expected = KvsException::class)
    fun testLongExtension_withException() {
        kvs["key"] = byteArrayOf(1, 2, 3, 4)
        kvs.getLong("key")
    }

    @Test
    fun testStringExtension() {
        assertNull(kvs["key"])
        assertTrue(kvs.getString("key").isEmpty())

        kvs["key"] = "value"
        assertEquals("value", kvs.getString("key"))
        assertNull(kvs["non-exist"])

        assertTrue(kvs.remove("key"))
        assertNull(kvs["key"])
        assertFalse(kvs.remove("non-exists"))
        assertNull(kvs["non-exist"])

        assertFalse(kvs.remove("key"))
    }
}
