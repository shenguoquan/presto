/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.hive;

import org.apache.hadoop.fs.Path;
import org.testng.annotations.Test;

import static com.facebook.presto.hive.HiveTestUtils.createTestHdfsEnvironment;
import static com.facebook.presto.hive.HiveWriteUtils.isS3FileSystem;
import static com.facebook.presto.hive.HiveWriteUtils.isViewFileSystem;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestHiveWriteUtils
{
    @Test
    public void testIsS3FileSystem()
    {
        HdfsEnvironment hdfsEnvironment = createTestHdfsEnvironment(new HiveClientConfig());
        assertTrue(isS3FileSystem("user", hdfsEnvironment, new Path("s3://test-bucket/test-folder")));
        assertFalse(isS3FileSystem("user", hdfsEnvironment, new Path("/test-dir/test-folder")));
    }

    @Test
    public void testIsViewFileSystem()
    {
        HdfsEnvironment hdfsEnvironment = createTestHdfsEnvironment(new HiveClientConfig());
        Path viewfsPath = new Path("viewfs://ns-default/test-folder");
        Path nonViewfsPath = new Path("hdfs://localhost/test-dir/test-folder");

        // ViewFS check requires the mount point config
        hdfsEnvironment.getConfiguration(viewfsPath).set("fs.viewfs.mounttable.ns-default.link./test-folder", "hdfs://localhost/app");

        assertTrue(isViewFileSystem("user", hdfsEnvironment, viewfsPath));
        assertFalse(isViewFileSystem("user", hdfsEnvironment, nonViewfsPath));
    }
}
