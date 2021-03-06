/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.storage.azure;

import com.microsoft.azure.storage.StorageException;
import org.easymock.EasyMockSupport;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.easymock.EasyMock.expect;

public class AzureByteSourceTest extends EasyMockSupport
{

  @Test
  public void openStreamTest() throws IOException, URISyntaxException, StorageException
  {
    final String containerName = "container";
    final String blobPath = "/path/to/file";
    AzureStorage azureStorage = createMock(AzureStorage.class);
    InputStream stream = createMock(InputStream.class);

    expect(azureStorage.getBlobInputStream(containerName, blobPath)).andReturn(stream);

    replayAll();

    AzureByteSource byteSource = new AzureByteSource(azureStorage, containerName, blobPath);

    byteSource.openStream();

    verifyAll();
  }

  @Test(expected = IOException.class)
  public void openStreamWithRecoverableErrorTest() throws URISyntaxException, StorageException, IOException
  {
    final String containerName = "container";
    final String blobPath = "/path/to/file";
    AzureStorage azureStorage = createMock(AzureStorage.class);

    expect(azureStorage.getBlobInputStream(containerName, blobPath)).andThrow(
        new StorageException(
            "",
            "",
            500,
            null,
            null
        )
    );

    replayAll();

    AzureByteSource byteSource = new AzureByteSource(azureStorage, containerName, blobPath);

    byteSource.openStream();

    verifyAll();
  }
}
