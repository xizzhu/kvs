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

import androidx.annotation.WorkerThread
import me.xizzhu.android.kvs.lmdb.LmdbKvs
import java.io.File

data class KvsConfig(
        var dir: String = ""
)

/**
 * @throws [KvsException]
 */
@WorkerThread
fun newKvs(config: KvsConfig.() -> Unit): Kvs {
    val kvsConfig = KvsConfig().apply { config(this) }

    if (kvsConfig.dir.isEmpty()) throw KvsException("Missing dir")
    val dir = File(kvsConfig.dir)
    if (!dir.exists()) throw KvsException("Dir '${kvsConfig.dir}' not exist")
    if (!dir.isDirectory) throw KvsException("'${kvsConfig.dir}' is not a directory")
    if (!dir.canRead()) throw KvsException("Cannot read from '${kvsConfig.dir}'")
    if (!dir.canWrite()) throw KvsException("Cannot write to '${kvsConfig.dir}'")

    return LmdbKvs(kvsConfig)
}
