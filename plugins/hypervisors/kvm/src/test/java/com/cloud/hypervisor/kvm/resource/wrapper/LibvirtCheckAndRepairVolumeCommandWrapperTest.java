/*
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

package com.cloud.hypervisor.kvm.resource.wrapper;

import com.cloud.agent.api.storage.CheckAndRepairVolumeAnswer;
import com.cloud.agent.api.storage.CheckAndRepairVolumeCommand;
import com.cloud.agent.api.to.StorageFilerTO;
import com.cloud.hypervisor.kvm.resource.LibvirtComputingResource;
import com.cloud.hypervisor.kvm.storage.KVMPhysicalDisk;
import com.cloud.hypervisor.kvm.storage.KVMStoragePool;
import com.cloud.hypervisor.kvm.storage.KVMStoragePoolManager;
import com.cloud.storage.Storage;

import org.apache.cloudstack.engine.subsystem.api.storage.VolumeInfo;
import org.apache.cloudstack.utils.qemu.QemuImg;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class LibvirtCheckAndRepairVolumeCommandWrapperTest {

    @Spy
    LibvirtCheckAndRepairVolumeCommandWrapper libvirtCheckAndRepairVolumeCommandWrapperSpy = Mockito.spy(LibvirtCheckAndRepairVolumeCommandWrapper.class);

    @Mock
    LibvirtComputingResource libvirtComputingResourceMock;

    @Mock
    CheckAndRepairVolumeCommand checkAndRepairVolumeCommand;

    @Mock
    QemuImg qemuImgMock;

    @Before
    public void init() {
        Mockito.when(libvirtComputingResourceMock.getCmdsTimeout()).thenReturn(60);
    }

    @Test
    @PrepareForTest(LibvirtCheckAndRepairVolumeCommandWrapper.class)
    public void testCheckAndRepairVolume() throws Exception {

        CheckAndRepairVolumeCommand cmd = Mockito.mock(CheckAndRepairVolumeCommand.class);
        Mockito.when(cmd.getPath()).thenReturn("cbac516a-0f1f-4559-921c-1a7c6c408ccf");
        Mockito.when(cmd.getRepair()).thenReturn(null);
        StorageFilerTO spool = Mockito.mock(StorageFilerTO.class);
        Mockito.when(cmd.getPool()).thenReturn(spool);

        KVMStoragePoolManager storagePoolMgr = Mockito.mock(KVMStoragePoolManager.class);
        Mockito.when(libvirtComputingResourceMock.getStoragePoolMgr()).thenReturn(storagePoolMgr);
        KVMStoragePool pool = Mockito.mock(KVMStoragePool.class);
        Mockito.when(spool.getType()).thenReturn(Storage.StoragePoolType.PowerFlex);
        Mockito.when(spool.getUuid()).thenReturn("b6be258b-42b8-49a4-ad51-3634ef8ff76a");
        Mockito.when(storagePoolMgr.getStoragePool(Storage.StoragePoolType.PowerFlex, "b6be258b-42b8-49a4-ad51-3634ef8ff76a")).thenReturn(pool);

        KVMPhysicalDisk vol = Mockito.mock(KVMPhysicalDisk.class);
        Mockito.when(pool.getPhysicalDisk("cbac516a-0f1f-4559-921c-1a7c6c408ccf")).thenReturn(vol);
        Mockito.when(vol.getFormat()).thenReturn(QemuImg.PhysicalDiskFormat.QCOW2);

        VolumeInfo volume = Mockito.mock(VolumeInfo.class);
        Mockito.when(volume.getPoolId()).thenReturn(1L);
        Mockito.when(volume.getPath()).thenReturn("cbac516a-0f1f-4559-921c-1a7c6c408ccf");

        String checkResult = "{\n" +
                "    \"image-end-offset\": 6442582016,\n" +
                "    \"total-clusters\": 163840,\n" +
                "    \"check-errors\": 0,\n" +
                "    \"leaks\": 124,\n" +
                "    \"allocated-clusters\": 98154,\n" +
                "    \"filename\": \"/var/lib/libvirt/images/26be20c7-b9d0-43f6-a76e-16c70737a0e0\",\n" +
                "    \"format\": \"qcow2\",\n" +
                "    \"fragmented-clusters\": 96135\n" +
                "}";

        PowerMockito.whenNew(QemuImg.class).withArguments(Mockito.anyInt()).thenReturn(qemuImgMock);
        Mockito.when(qemuImgMock.checkAndRepair(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(checkResult);

        CheckAndRepairVolumeAnswer result = (CheckAndRepairVolumeAnswer) libvirtCheckAndRepairVolumeCommandWrapperSpy.execute(cmd, libvirtComputingResourceMock);

        Assert.assertEquals(checkResult, result.getVolumeCheckExecutionResult());
    }

}
