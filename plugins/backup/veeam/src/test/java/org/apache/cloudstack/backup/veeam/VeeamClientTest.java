// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.cloudstack.backup.veeam;

import org.junit.Before;
import org.junit.Test;

public class VeeamClientTest {

    private VeeamClient client;

    @Before
    public void setUp() throws Exception {
        client = new VeeamClient("http://10.2.2.89:9399/api/", "administrator", "P@ssword123", true, 60);
    }

    @Test
    public void testBackups() {
        client.listAllBackups();
    }

    @Test
    public void testPolicies() {
        client.listBackupPolicies();
    }

    @Test
    public void testBackupLifecycle() {
        client.addVMToVeeamJob("8acac50d-3711-4c99-bf7b-76fe9c7e39c3", "i-2-9-VM", "10.2.2.52");
        client.startAdhocBackupJob("8acac50d-3711-4c99-bf7b-76fe9c7e39c3");
        client.removeVMFromVeeamJob("8acac50d-3711-4c99-bf7b-76fe9c7e39c3", "i-2-9-VM", "10.2.2.52");
    }
}