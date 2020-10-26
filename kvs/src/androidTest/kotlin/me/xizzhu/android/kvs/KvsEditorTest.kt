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

class KvsEditorTest {
    private lateinit var kvs: Kvs

    @BeforeTest
    fun setup() {
        kvs = newKvs {
            dir = getTestDir().apply { mkdirs() }.absolutePath
        }
    }

    private fun getTestDir(): File = File(System.getProperty("java.io.tmpdir"), "KvsTransactionTest")

    @AfterTest
    fun tearDown() {
        kvs.close()
        getTestDir().deleteRecursively()
    }

    @Test
    fun testEditingAbortedWithCrash() {
        var called = false
        try {
            kvs.edit {
                it["key"] = "value"
                throw IllegalStateException("random exception")
            }
        } catch (e: IllegalStateException) {
            called = true
            assertEquals("random exception", e.message)
        }
        assertTrue(called)
        assertFalse(kvs.contains("key"))
    }

    @Test
    fun testEditingVisibility() {
        kvs.edit { it["key"] = "value" }
        assertEquals("value", kvs.getString("key"))

        kvs.edit {
            it.remove("key")
            assertEquals("value", kvs.getString("key")) // the mutation is not committed yet

            it["key"] = "new value"
            assertEquals("value", kvs.getString("key")) // the mutation is not committed yet
        }

        assertEquals("new value", kvs.getString("key"))
    }
}
