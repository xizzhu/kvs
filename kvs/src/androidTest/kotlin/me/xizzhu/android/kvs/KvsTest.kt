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

class KvsTest {
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

    @Test(expected = KvsException::class)
    fun testGet_withEmptyKey() {
        kvs[byteArrayOf()]
    }

    @Test
    fun testGet_withNonExistingKey() {
        assertNull(kvs[byteArrayOf(1)])
    }

    @Test(expected = KvsException::class)
    fun testSet_withEmptyKey() {
        kvs[byteArrayOf()] = byteArrayOf(1)
    }

    @Test(expected = KvsException::class)
    fun testSet_withEmptyValue() {
        kvs[byteArrayOf(1)] = byteArrayOf()
    }

    @Test(expected = KvsException::class)
    fun testRemove_withEmptyKey() {
        kvs.remove(byteArrayOf())
    }

    @Test
    fun testRemove_withNonExistingKey() {
        assertFalse(kvs.remove(byteArrayOf(1)))
    }

    @Test
    fun testSetThenGet() {
        assertNull(kvs[byteArrayOf(1)])

        kvs[byteArrayOf(1)] = byteArrayOf(1, 2, 3, 4, 5)
        assertTrue(byteArrayOf(1, 2, 3, 4, 5).contentEquals(kvs[byteArrayOf(1)]))
    }

    @Test
    fun testSetGetAndRemove() {
        assertNull(kvs[byteArrayOf(1)])

        kvs[byteArrayOf(1)] = byteArrayOf(1, 2, 3, 4, 5)
        assertTrue(byteArrayOf(1, 2, 3, 4, 5).contentEquals(kvs[byteArrayOf(1)]))

        assertTrue(kvs.remove(byteArrayOf(1)))
        assertNull(kvs[byteArrayOf(1)])

        assertFalse(kvs.remove(byteArrayOf(1)))
    }
}
