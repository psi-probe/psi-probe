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

/*
 * MIT License
 *
 * Copyright (c) 2010 - 2020 The OSHI Project Contributors: https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package psiprobe.controllers.oshi;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import oshi.PlatformEnum;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.PhysicalProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
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
import oshi.software.os.OSProcess;
import oshi.software.os.OSService;
import oshi.software.os.OSSession;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessFiltering;
import oshi.software.os.OperatingSystem.ProcessSorting;
import oshi.util.FormatUtil;
import oshi.util.Util;

import psiprobe.controllers.AbstractTomcatContainerController;

/**
 * Creates an instance of Operating System and Hardware Information based on Oshi SystemInfoTest.
 * <p>
 * A demonstration of access to many of OSHI's capabilities
 */
@Controller
public class OshiController extends AbstractTomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(OshiController.class);

  /** Oshi Cache. */
  private static List<String> oshi = new ArrayList<>();

  @RequestMapping(path = "/adm/oshi.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    if (!oshi.isEmpty()) {
      ModelAndView mv = new ModelAndView(getViewName());
      mv.addObject("oshi", oshi);
      return mv;
    }

    // TODO: Remove once no longer experimental
    oshi.add(
        "Oshi results are performed as a system dump to screen here using Oshi SystemInfoTest logic.");
    oshi.add(
        "Please be advised this is experimental in use and is slow. Resulting data is entirely cached.");
    oshi.add("Issues with library should be directed to https://github.com/oshi/oshi");
    oshi.add("For issues with our library usage of Oshi, please submit pull requests");
    oshi.add("");
    oshi.add("");

    this.initialize();

    ModelAndView mv = new ModelAndView(getViewName());
    mv.addObject("oshi", oshi);
    return mv;
  }

  @Value("oshi")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

  /**
   * Process initialization using Oshi System Info Test.
   * <p>
   * Code copied and adjusted for Psi Probem from Oshi SystemInfoTest.main at revision
   *
   * <pre>
   * https://github.com/oshi/oshi/blob/cf45b1f528f99ca353655dea5f154940c76c0bdb/oshi-core/src/test/java/oshi/SystemInfoTest.java
   * </pre>
   *
   * <pre>
   * Psi Probe differences
   * - Noted directly in area of change as possible
   * - Logging switched from 'info' to 'debug'
   * - Javadocs throughout
   * - Formatting differences (2 vs 4 spaces)
   * </pre>
   */
  private void initialize() {
    logger.debug("Initializing System...");
    SystemInfo si = new SystemInfo();

    // Psi Probe adjusted oshi initial test to confirm platform supported before attempting to run
    if (PlatformEnum.UNKNOWN.equals(SystemInfo.getCurrentPlatform())) {
      logger.error("Oshi not supported on current platform");
      oshi.add("Oshi not supported on current platform");
      oshi.add("");
      return;
    }

    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();

    printOperatingSystem(os);

    logger.debug("Checking computer system...");
    printComputerSystem(hal.getComputerSystem());

    logger.debug("Checking Processor...");
    printProcessor(hal.getProcessor());

    logger.debug("Checking Memory...");
    printMemory(hal.getMemory());

    logger.debug("Checking CPU...");
    printCpu(hal.getProcessor());

    logger.debug("Checking Processes...");
    printProcesses(os, hal.getMemory());

    logger.debug("Checking Services...");
    printServices(os);

    logger.debug("Checking Sensors...");
    printSensors(hal.getSensors());

    logger.debug("Checking Power sources...");
    printPowerSources(hal.getPowerSources());

    logger.debug("Checking Disks...");
    printDisks(hal.getDiskStores());

    logger.debug("Checking Logical Volume Groups ...");
    printLVgroups(hal.getLogicalVolumeGroups());

    logger.debug("Checking File System...");
    printFileSystem(os.getFileSystem());

    logger.debug("Checking Network interfaces...");
    printNetworkInterfaces(hal.getNetworkIFs());

    logger.debug("Checking Network parameters...");
    printNetworkParameters(os.getNetworkParams());

    logger.debug("Checking IP statistics...");
    printInternetProtocolStats(os.getInternetProtocolStats());

    logger.debug("Checking Displays...");
    printDisplays(hal.getDisplays());

    logger.debug("Checking USB Devices...");
    printUsbDevices(hal.getUsbDevices(true));

    logger.debug("Checking Sound Cards...");
    printSoundCards(hal.getSoundCards());

    logger.debug("Checking Graphics Cards...");
    printGraphicsCards(hal.getGraphicsCards());

    // Psi Probe addition to note finished
    oshi.add("Finished Operating System and Hardware Info Dump");

    StringBuilder output = new StringBuilder();
    for (String element : oshi) {
      output.append(element);
      // Psi Probe fix as output check 'endsWith' was wrong
      if (!"\n".equals(element)) {
        output.append('\n');
      }
    }
    logger.info("Printing Operating System and Hardware Info:{}{}", '\n', output);
  }

  /**
   * Prints the operating system.
   *
   * @param operatingSystem the operating system
   */
  private static void printOperatingSystem(final OperatingSystem operatingSystem) {
    oshi.add(String.valueOf(operatingSystem));
    oshi.add("Booted: " + Instant.ofEpochSecond(operatingSystem.getSystemBootTime()));
    oshi.add("Uptime: " + FormatUtil.formatElapsedSecs(operatingSystem.getSystemUptime()));
    oshi.add(
        "Running with" + (operatingSystem.isElevated() ? "" : "out") + " elevated permissions.");
    oshi.add("Sessions:");
    for (OSSession s : operatingSystem.getSessions()) {
      oshi.add(" " + s.toString());
    }
  }

  /**
   * Prints the computer system.
   *
   * @param computerSystem the computer system
   */
  private static void printComputerSystem(final ComputerSystem computerSystem) {
    oshi.add("System: " + computerSystem.toString());
    oshi.add(" Firmware: " + computerSystem.getFirmware().toString());
    oshi.add(" Baseboard: " + computerSystem.getBaseboard().toString());
  }

  /**
   * Prints the processor.
   *
   * @param processor the processor
   */
  private static void printProcessor(CentralProcessor processor) {
    oshi.add(processor.toString());
    oshi.add(" Cores:");
    for (PhysicalProcessor p : processor.getPhysicalProcessors()) {
      oshi.add(
          "  " + (processor.getPhysicalPackageCount() > 1 ? p.getPhysicalPackageNumber() + "," : "")
              + p.getPhysicalProcessorNumber() + ": efficiency=" + p.getEfficiency() + ", id="
              + p.getIdString());
    }
  }

  /**
   * Prints the memory.
   *
   * @param memory the memory
   */
  private static void printMemory(GlobalMemory memory) {
    oshi.add("Physical Memory: \n " + memory.toString());
    VirtualMemory vm = memory.getVirtualMemory();
    oshi.add("Virtual Memory: \n " + vm.toString());
    List<PhysicalMemory> pmList = memory.getPhysicalMemory();
    if (!pmList.isEmpty()) {
      oshi.add("Physical Memory: ");
      for (PhysicalMemory pm : pmList) {
        oshi.add(" " + pm.toString());
      }
    }
  }

  /**
   * Prints the cpu.
   *
   * @param processor the processor
   */
  private static void printCpu(CentralProcessor processor) {
    oshi.add("Context Switches/Interrupts: " + processor.getContextSwitches() + " / "
        + processor.getInterrupts());

    long[] prevTicks = processor.getSystemCpuLoadTicks();
    long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
    oshi.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks));
    // Wait a second...
    Util.sleep(1000);
    long[] ticks = processor.getSystemCpuLoadTicks();
    oshi.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks));
    long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
    long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
    long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
    long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
    long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
    long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
    long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
    long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
    long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

    oshi.add(String.format(
        "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
        100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu,
        100d * idle / totalCpu, 100d * iowait / totalCpu, 100d * irq / totalCpu,
        100d * softirq / totalCpu, 100d * steal / totalCpu));
    oshi.add(
        String.format("CPU load: %.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));
    double[] loadAverage = processor.getSystemLoadAverage(3);
    oshi.add("CPU load averages:"
        + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
        + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
        + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
    // per core CPU
    StringBuilder procCpu = new StringBuilder("CPU load per processor:");
    double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
    for (double avg : load) {
      procCpu.append(String.format(" %.1f%%", avg * 100));
    }
    oshi.add(procCpu.toString());
    long freq = processor.getProcessorIdentifier().getVendorFreq();
    if (freq > 0) {
      oshi.add("Vendor Frequency: " + FormatUtil.formatHertz(freq));
    }
    freq = processor.getMaxFreq();
    if (freq > 0) {
      oshi.add("Max Frequency: " + FormatUtil.formatHertz(freq));
    }
    long[] freqs = processor.getCurrentFreq();
    if (freqs[0] > 0) {
      StringBuilder sb = new StringBuilder("Current Frequencies: ");
      for (int i = 0; i < freqs.length; i++) {
        if (i > 0) {
          sb.append(", ");
        }
        sb.append(FormatUtil.formatHertz(freqs[i]));
      }
      oshi.add(sb.toString());
    }
  }

  /**
   * Prints the processes.
   *
   * @param os the os
   * @param memory the memory
   */
  private static void printProcesses(OperatingSystem os, GlobalMemory memory) {
    OSProcess myProc = os.getProcess(os.getProcessId());
    // current process will never be null. Other code should check for null here
    oshi.add("My PID: " + myProc.getProcessID() + " with affinity "
        + Long.toBinaryString(myProc.getAffinityMask()));
    oshi.add("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
    // Sort by highest CPU
    List<OSProcess> procs =
        os.getProcesses(ProcessFiltering.ALL_PROCESSES, ProcessSorting.CPU_DESC, 5);
    oshi.add("   PID  %CPU %MEM       VSZ       RSS Name");
    for (int i = 0; i < procs.size() && i < 5; i++) {
      OSProcess p = procs.get(i);
      oshi.add(String.format(" %5d %5.1f %4.1f %9s %9s %s", p.getProcessID(),
          100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
          100d * p.getResidentSetSize() / memory.getTotal(),
          FormatUtil.formatBytes(p.getVirtualSize()),
          FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
    }
    OSProcess p = os.getProcess(os.getProcessId());
    oshi.add("Current process arguments: ");
    for (String s : p.getArguments()) {
      oshi.add("  " + s);
    }
    oshi.add("Current process environment: ");
    for (Entry<String, String> e : p.getEnvironmentVariables().entrySet()) {
      oshi.add("  " + e.getKey() + "=" + e.getValue());
    }
  }

  /**
   * Prints the services.
   *
   * @param os the operating system
   */
  private static void printServices(OperatingSystem os) {
    oshi.add("Services: ");
    oshi.add("   PID   State   Name");
    // DO 5 each of running and stopped
    int i = 0;
    for (OSService s : os.getServices()) {
      if (s.getState().equals(OSService.State.RUNNING) && i++ < 5) {
        oshi.add(String.format(" %5d  %7s  %s", s.getProcessID(), s.getState(), s.getName()));
      }
    }
    i = 0;
    for (OSService s : os.getServices()) {
      if (s.getState().equals(OSService.State.STOPPED) && i++ < 5) {
        oshi.add(String.format(" %5d  %7s  %s", s.getProcessID(), s.getState(), s.getName()));
      }
    }
  }

  /**
   * Prints the sensors.
   *
   * @param sensors the sensors
   */
  private static void printSensors(Sensors sensors) {
    oshi.add("Sensors: " + sensors.toString());
  }

  /**
   * Prints the power sources.
   *
   * @param list the power sources
   */
  private static void printPowerSources(List<PowerSource> list) {
    StringBuilder sb = new StringBuilder("Power Sources: ");
    if (list.isEmpty()) {
      sb.append("Unknown");
    }
    for (PowerSource powerSource : list) {
      sb.append("\n ").append(powerSource.toString());
    }
    oshi.add(sb.toString());
  }

  /**
   * Prints the disks.
   *
   * @param list the disk stores
   */
  private static void printDisks(List<HWDiskStore> list) {
    oshi.add("Disks:");
    for (HWDiskStore disk : list) {
      oshi.add(" " + disk.toString());

      List<HWPartition> partitions = disk.getPartitions();
      for (HWPartition part : partitions) {
        oshi.add(" |-- " + part.toString());
      }
    }

  }

  /**
   * Prints the logical volume groups.
   *
   * @param list the logical volume groups
   */
  private static void printLVgroups(List<LogicalVolumeGroup> list) {
    if (!list.isEmpty()) {
      oshi.add("Logical Volume Groups:");
      for (LogicalVolumeGroup lvg : list) {
        oshi.add(" " + lvg.toString());
      }
    }
  }

  /**
   * Prints the file system.
   *
   * @param fileSystem the file system
   */
  private static void printFileSystem(FileSystem fileSystem) {
    oshi.add("File System:");

    oshi.add(String.format(" File Descriptors: %d/%d", fileSystem.getOpenFileDescriptors(),
        fileSystem.getMaxFileDescriptors()));

    for (OSFileStore fs : fileSystem.getFileStores()) {
      long usable = fs.getUsableSpace();
      long total = fs.getTotalSpace();
      oshi.add(String.format(
          " %s (%s) [%s] %s of %s free (%.1f%%), %s of %s files free (%.1f%%) is %s "
              + (fs.getLogicalVolume() != null && fs.getLogicalVolume().length() > 0 ? "[%s]"
                  : "%s")
              + " and is mounted at %s",
          fs.getName(), fs.getDescription().isEmpty() ? "file system" : fs.getDescription(),
          fs.getType(), FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()),
          100d * usable / total, FormatUtil.formatValue(fs.getFreeInodes(), ""),
          FormatUtil.formatValue(fs.getTotalInodes(), ""),
          100d * fs.getFreeInodes() / fs.getTotalInodes(), fs.getVolume(), fs.getLogicalVolume(),
          fs.getMount()));
    }
  }

  /**
   * Prints the network interfaces.
   *
   * @param list the network interfaces
   */
  private static void printNetworkInterfaces(List<NetworkIF> list) {
    StringBuilder sb = new StringBuilder("Network Interfaces:");
    if (list.isEmpty()) {
      sb.append(" Unknown");
    } else {
      for (NetworkIF net : list) {
        sb.append("\n ").append(net.toString());
      }
    }
    oshi.add(sb.toString());
  }

  /**
   * Prints the network parameters.
   *
   * @param networkParams the network params
   */
  private static void printNetworkParameters(NetworkParams networkParams) {
    oshi.add("Network parameters:\n " + networkParams.toString());
  }

  /**
   * Prints the internet protocol stats.
   *
   * @param internetProtocolStats the internet protocal stats
   */
  private static void printInternetProtocolStats(InternetProtocolStats internetProtocolStats) {
    oshi.add("Internet Protocol statistics:");
    oshi.add(" TCPv4: " + internetProtocolStats.getTCPv4Stats());
    oshi.add(" TCPv6: " + internetProtocolStats.getTCPv6Stats());
    oshi.add(" UDPv4: " + internetProtocolStats.getUDPv4Stats());
    oshi.add(" UDPv6: " + internetProtocolStats.getUDPv6Stats());
  }

  /**
   * Prints the displays.
   *
   * @param list the displays
   */
  private static void printDisplays(List<Display> list) {
    oshi.add("Displays:");
    int i = 0;
    for (Display display : list) {
      oshi.add(" Display " + i + ":");
      oshi.add(String.valueOf(display));
      i++;
    }
  }

  /**
   * Prints the usb devices.
   *
   * @param list the usb devices
   */
  private static void printUsbDevices(List<UsbDevice> list) {
    oshi.add("USB Devices:");
    for (UsbDevice usbDevice : list) {
      oshi.add(String.valueOf(usbDevice));
    }
  }

  /**
   * Prints the sound cards.
   *
   * @param list the sound cards
   */
  private static void printSoundCards(List<SoundCard> list) {
    oshi.add("Sound Cards:");
    for (SoundCard card : list) {
      oshi.add(" " + String.valueOf(card));
    }
  }

  /**
   * Prints the graphic cards.
   *
   * @param list the graphics cards
   */
  private static void printGraphicsCards(List<GraphicsCard> list) {
    oshi.add("Graphics Cards:");
    if (list.isEmpty()) {
      oshi.add(" None detected.");
    } else {
      for (GraphicsCard card : list) {
        oshi.add(" " + String.valueOf(card));
      }
    }
  }

}
