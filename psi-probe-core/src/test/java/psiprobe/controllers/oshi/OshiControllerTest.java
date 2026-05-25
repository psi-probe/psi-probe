/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.oshi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.PhysicalProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.Firmware;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.LogicalVolumeGroup;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSService;
import oshi.software.os.OSSession;
import oshi.software.os.OperatingSystem;

/**
 * Tests for {@link OshiController}.
 */
class OshiControllerTest {

  @BeforeEach
  void setUp() throws Exception {
    Field oshiField = OshiController.class.getDeclaredField("oshi");
    oshiField.setAccessible(true);
    oshiField.set(null, new ArrayList<String>());
  }

  @SuppressWarnings("unchecked")
  private List<String> getOshiData() throws Exception {
    Field oshiField = OshiController.class.getDeclaredField("oshi");
    oshiField.setAccessible(true);
    return (List<String>) oshiField.get(null);
  }

  private void invokeStatic(String methodName, Class<?> parameterType, Object argument)
      throws Exception {
    Method method = OshiController.class.getDeclaredMethod(methodName, parameterType);
    method.setAccessible(true);
    method.invoke(null, argument);
  }

  @Test
  void handleRequestInternalUsesCachedData() throws Exception {
    getOshiData().add("cached-entry");

    OshiController controller = new OshiController();
    controller.setViewName("oshi");

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    ModelAndView mv = controller.handleRequestInternal(request, response);

    assertEquals("oshi", mv.getViewName());
    assertNotNull(mv.getModel().get("oshi"));
    assertEquals(1, ((List<?>) mv.getModel().get("oshi")).size());
  }

  @Test
  void printPowerSourcesHandlesEmptyAndPopulatedLists() throws Exception {
    invokeStatic("printPowerSources", List.class, List.of());
    assertTrue(getOshiData().get(0).contains("Unknown"));

    PowerSource source = mock(PowerSource.class);
    when(source.toString()).thenReturn("Battery-A");
    invokeStatic("printPowerSources", List.class, List.of(source));
    assertTrue(getOshiData().get(1).contains("Battery-A"));
  }

  @Test
  void printNetworkInterfacesHandlesEmptyAndNonEmptyLists() throws Exception {
    invokeStatic("printNetworkInterfaces", List.class, List.of());
    assertTrue(getOshiData().get(0).contains("Unknown"));

    NetworkIF networkIF = mock(NetworkIF.class);
    when(networkIF.toString()).thenReturn("eth0");
    invokeStatic("printNetworkInterfaces", List.class, List.of(networkIF));
    assertTrue(getOshiData().get(1).contains("eth0"));
  }

  @Test
  void printGraphicsCardsHandlesEmptyAndNonEmptyLists() throws Exception {
    invokeStatic("printGraphicsCards", List.class, List.of());
    assertTrue(getOshiData().contains(" None detected."));

    GraphicsCard card = mock(GraphicsCard.class);
    when(card.toString()).thenReturn("GPU-1");
    invokeStatic("printGraphicsCards", List.class, List.of(card));
    assertTrue(getOshiData().contains(" GPU-1"));
  }

  @Test
  void printDisplaysUsbAndSoundCardsAddEntries() throws Exception {
    Display display = mock(Display.class);
    UsbDevice usbDevice = mock(UsbDevice.class);
    SoundCard soundCard = mock(SoundCard.class);

    when(display.toString()).thenReturn("Display-1");
    when(usbDevice.toString()).thenReturn("USB-A");
    when(soundCard.toString()).thenReturn("Sound-A");

    invokeStatic("printDisplays", List.class, List.of(display));
    invokeStatic("printUsbDevices", List.class, List.of(usbDevice));
    invokeStatic("printSoundCards", List.class, List.of(soundCard));

    List<String> data = getOshiData();
    assertTrue(data.contains("Display-1"));
    assertTrue(data.contains("USB-A"));
    assertTrue(data.contains(" Sound-A"));
  }

  @Test
  void printServicesIncludesRunningAndStoppedServices() throws Exception {
    OperatingSystem os = mock(OperatingSystem.class);

    OSService running = mock(OSService.class);
    when(running.getState()).thenReturn(OSService.State.RUNNING);
    when(running.getProcessID()).thenReturn(111);
    when(running.getName()).thenReturn("svc-running");

    OSService stopped = mock(OSService.class);
    when(stopped.getState()).thenReturn(OSService.State.STOPPED);
    when(stopped.getProcessID()).thenReturn(222);
    when(stopped.getName()).thenReturn("svc-stopped");

    when(os.getServices()).thenReturn(List.of(running, stopped));

    invokeStatic("printServices", OperatingSystem.class, os);

    String rendered = String.join("\n", getOshiData());
    assertTrue(rendered.contains("svc-running"));
    assertTrue(rendered.contains("svc-stopped"));
  }

  @Test
  void printNetworkAndInternetProtocolStatsRenderValues() throws Exception {
    NetworkParams networkParams = mock(NetworkParams.class);
    when(networkParams.toString()).thenReturn("host=example");

    InternetProtocolStats internetProtocolStats = mock(InternetProtocolStats.class);
    when(internetProtocolStats.getTCPv4Stats())
        .thenReturn(mock(InternetProtocolStats.TcpStats.class));
    when(internetProtocolStats.getTCPv6Stats())
        .thenReturn(mock(InternetProtocolStats.TcpStats.class));
    when(internetProtocolStats.getUDPv4Stats())
        .thenReturn(mock(InternetProtocolStats.UdpStats.class));
    when(internetProtocolStats.getUDPv6Stats())
        .thenReturn(mock(InternetProtocolStats.UdpStats.class));

    invokeStatic("printNetworkParameters", NetworkParams.class, networkParams);
    invokeStatic("printInternetProtocolStats", InternetProtocolStats.class, internetProtocolStats);

    String rendered = String.join("\n", getOshiData());
    assertTrue(rendered.contains("host=example"));
    assertTrue(rendered.contains("Internet Protocol statistics:"));
    assertTrue(rendered.contains("TCPv4"));
  }

  @Test
  void printLogicalVolumeGroupsAndDisksRenderEntries() throws Exception {
    LogicalVolumeGroup lvg = mock(LogicalVolumeGroup.class);
    when(lvg.toString()).thenReturn("vg0");

    invokeStatic("printLogicalVolumegroups", List.class, List.of(lvg));

    HWDiskStore disk = mock(HWDiskStore.class);
    HWPartition partition = mock(HWPartition.class);
    when(disk.toString()).thenReturn("disk0");
    when(disk.getPartitions()).thenReturn(List.of(partition));
    when(partition.toString()).thenReturn("sda1");

    invokeStatic("printDisks", List.class, List.of(disk));

    String rendered = String.join("\n", getOshiData());
    assertTrue(rendered.contains("Logical Volume Groups:"));
    assertTrue(rendered.contains("vg0"));
    assertTrue(rendered.contains("Disks:"));
    assertTrue(rendered.contains("disk0"));
    assertTrue(rendered.contains("sda1"));
  }

  @Test
  void printFileSystemFormatsFileStoreAndDescriptors() throws Exception {
    FileSystem fileSystem = mock(FileSystem.class);
    OSFileStore storeWithoutLogical = mock(OSFileStore.class);
    OSFileStore storeWithLogical = mock(OSFileStore.class);

    when(fileSystem.getOpenFileDescriptors()).thenReturn(10L);
    when(fileSystem.getMaxFileDescriptors()).thenReturn(100L);
    when(fileSystem.getFileStores()).thenReturn(List.of(storeWithoutLogical, storeWithLogical));

    when(storeWithoutLogical.getName()).thenReturn("store0");
    when(storeWithoutLogical.getDescription()).thenReturn("");
    when(storeWithoutLogical.getType()).thenReturn("ext4");
    when(storeWithoutLogical.getUsableSpace()).thenReturn(50L);
    when(storeWithoutLogical.getTotalSpace()).thenReturn(100L);
    when(storeWithoutLogical.getFreeInodes()).thenReturn(20L);
    when(storeWithoutLogical.getTotalInodes()).thenReturn(40L);
    when(storeWithoutLogical.getVolume()).thenReturn("/dev/sda1");
    when(storeWithoutLogical.getLogicalVolume()).thenReturn("");
    when(storeWithoutLogical.getMount()).thenReturn("/");

    when(storeWithLogical.getName()).thenReturn("store1");
    when(storeWithLogical.getDescription()).thenReturn("data");
    when(storeWithLogical.getType()).thenReturn("xfs");
    when(storeWithLogical.getUsableSpace()).thenReturn(75L);
    when(storeWithLogical.getTotalSpace()).thenReturn(100L);
    when(storeWithLogical.getFreeInodes()).thenReturn(10L);
    when(storeWithLogical.getTotalInodes()).thenReturn(20L);
    when(storeWithLogical.getVolume()).thenReturn("/dev/mapper/vg-lv");
    when(storeWithLogical.getLogicalVolume()).thenReturn("vg-lv");
    when(storeWithLogical.getMount()).thenReturn("/data");

    invokeStatic("printFileSystem", FileSystem.class, fileSystem);

    String rendered = String.join("\n", getOshiData());
    assertTrue(rendered.contains("File Descriptors: 10/100"));
    assertTrue(rendered.contains("store0"));
    assertTrue(rendered.contains("store1"));
    assertTrue(rendered.contains("is mounted at /"));
    assertTrue(rendered.contains("is mounted at /data"));
  }

  @Test
  void printOperatingSystemRendersSessionsAndPermissionStatus() throws Exception {
    OperatingSystem os = mock(OperatingSystem.class);
    OSSession session = mock(OSSession.class);
    when(session.toString()).thenReturn("user-session");

    when(os.toString()).thenReturn("MyOS");
    when(os.getSystemBootTime()).thenReturn(1L);
    when(os.getSystemUptime()).thenReturn(10L);
    when(os.getSessions()).thenReturn(List.of(session));
    when(os.isElevated()).thenReturn(false, true);

    invokeStatic("printOperatingSystem", OperatingSystem.class, os);
    invokeStatic("printOperatingSystem", OperatingSystem.class, os);

    String rendered = String.join("\n", getOshiData());
    assertTrue(rendered.contains("MyOS"));
    assertTrue(rendered.contains("user-session"));
    assertTrue(rendered.contains("without elevated permissions"));
    assertTrue(rendered.contains("with elevated permissions"));
  }

  @Test
  void printComputerSystemProcessorMemoryAndSensorsRenderValues() throws Exception {
    ComputerSystem computerSystem = mock(ComputerSystem.class);
    Firmware firmware = mock(Firmware.class);
    Baseboard baseboard = mock(Baseboard.class);
    when(computerSystem.toString()).thenReturn("computer");
    when(firmware.toString()).thenReturn("firmware");
    when(baseboard.toString()).thenReturn("baseboard");
    when(computerSystem.getFirmware()).thenReturn(firmware);
    when(computerSystem.getBaseboard()).thenReturn(baseboard);
    invokeStatic("printComputerSystem", ComputerSystem.class, computerSystem);

    CentralProcessor processor = mock(CentralProcessor.class);
    PhysicalProcessor physicalProcessor = mock(PhysicalProcessor.class);
    when(processor.toString()).thenReturn("cpu");
    when(processor.getPhysicalProcessors()).thenReturn(List.of(physicalProcessor));
    when(processor.getPhysicalPackageCount()).thenReturn(2);
    when(physicalProcessor.getPhysicalPackageNumber()).thenReturn(1);
    when(physicalProcessor.getPhysicalProcessorNumber()).thenReturn(2);
    when(physicalProcessor.getEfficiency()).thenReturn(3);
    when(physicalProcessor.getIdString()).thenReturn("cpu-id");
    invokeStatic("printProcessor", CentralProcessor.class, processor);

    GlobalMemory memory = mock(GlobalMemory.class);
    VirtualMemory virtualMemory = mock(VirtualMemory.class);
    PhysicalMemory physicalMemory = mock(PhysicalMemory.class);
    when(memory.toString()).thenReturn("memory");
    when(virtualMemory.toString()).thenReturn("virtual-memory");
    when(physicalMemory.toString()).thenReturn("dimm0");
    when(memory.getVirtualMemory()).thenReturn(virtualMemory);
    when(memory.getPhysicalMemory()).thenReturn(List.of(physicalMemory));
    invokeStatic("printMemory", GlobalMemory.class, memory);

    Sensors sensors = mock(Sensors.class);
    when(sensors.toString()).thenReturn("sensor-data");
    invokeStatic("printSensors", Sensors.class, sensors);

    String rendered = String.join("\n", getOshiData());
    assertTrue(rendered.contains("computer"));
    assertTrue(rendered.contains("firmware"));
    assertTrue(rendered.contains("baseboard"));
    assertTrue(rendered.contains("cpu-id"));
    assertTrue(rendered.contains("virtual-memory"));
    assertTrue(rendered.contains("dimm0"));
    assertTrue(rendered.contains("sensor-data"));
  }
}
