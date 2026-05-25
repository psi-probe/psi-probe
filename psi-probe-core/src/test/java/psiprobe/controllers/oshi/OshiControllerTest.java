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

import oshi.hardware.Display;
import oshi.hardware.GraphicsCard;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSService;
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
}
