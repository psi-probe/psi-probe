/**
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.Firmware;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.UsbDevice;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;
import oshi.util.Util;
import psiprobe.controllers.AbstractTomcatContainerController;

/**
 * Creates an instance of Operating System and Hardware Information.
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

    logger.debug("Initializing System...");
    SystemInfo si = new SystemInfo();

    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();

    oshi.add(String.valueOf(os));

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

    logger.debug("Checking Sensors...");
    printSensors(hal.getSensors());

    logger.debug("Checking Power sources...");
    printPowerSources(hal.getPowerSources());

    logger.debug("Checking Disks...");
    printDisks(hal.getDiskStores());

    logger.debug("Checking File System...");
    printFileSystem(os.getFileSystem());

    logger.debug("Checking Network interfaces...");
    printNetworkInterfaces(hal.getNetworkIFs());

    logger.debug("Checking Network parameters...");
    printNetworkParameters(os.getNetworkParams());

    // hardware: displays
    logger.debug("Checking Displays...");
    printDisplays(hal.getDisplays());

    // hardware: USB devices
    logger.debug("Checking USB Devices...");
    printUsbDevices(hal.getUsbDevices(true));

    StringBuilder output = new StringBuilder();
    for (int i = 0; i < oshi.size(); i++) {
      output.append(oshi.get(i));
      if (!"\n".equals(oshi.get(i))) {
        output.append('\n');
      }
    }
    logger.debug("Printing List: {}", output);

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
   * Prints the computer system.
   *
   * @param computerSystem the computer system
   */
  private static void printComputerSystem(final ComputerSystem computerSystem) {

    oshi.add("manufacturer: " + computerSystem.getManufacturer());
    oshi.add("model: " + computerSystem.getModel());
    oshi.add("serialnumber: " + computerSystem.getSerialNumber());
    final Firmware firmware = computerSystem.getFirmware();
    oshi.add("firmware:");
    oshi.add("  manufacturer: " + firmware.getManufacturer());
    oshi.add("  name: " + firmware.getName());
    oshi.add("  description: " + firmware.getDescription());
    oshi.add("  version: " + firmware.getVersion());
    oshi.add("  release date: " + (firmware.getReleaseDate() == null ? "unknown"
        : firmware.getReleaseDate() == null ? "unknown"
            : FormatUtil.formatDate(firmware.getReleaseDate())));
    final Baseboard baseboard = computerSystem.getBaseboard();
    oshi.add("baseboard:");
    oshi.add("  manufacturer: " + baseboard.getManufacturer());
    oshi.add("  model: " + baseboard.getModel());
    oshi.add("  version: " + baseboard.getVersion());
    oshi.add("  serialnumber: " + baseboard.getSerialNumber());
  }

  /**
   * Prints the processor.
   *
   * @param processor the processor
   */
  private static void printProcessor(CentralProcessor processor) {
    oshi.add(String.valueOf(processor));
    oshi.add(" " + processor.getPhysicalProcessorCount() + " physical CPU(s)");
    oshi.add(" " + processor.getLogicalProcessorCount() + " logical CPU(s)");

    oshi.add("Identifier: " + processor.getIdentifier());
    oshi.add("ProcessorID: " + processor.getProcessorID());
  }

  /**
   * Prints the memory.
   *
   * @param memory the memory
   */
  private static void printMemory(GlobalMemory memory) {
    oshi.add("Memory: " + FormatUtil.formatBytes(memory.getAvailable()) + "/"
        + FormatUtil.formatBytes(memory.getTotal()));
    oshi.add("Swap used: " + FormatUtil.formatBytes(memory.getSwapUsed()) + "/"
        + FormatUtil.formatBytes(memory.getSwapTotal()));
  }

  /**
   * Prints the cpu.
   *
   * @param processor the processor
   */
  private static void printCpu(CentralProcessor processor) {
    oshi.add("Uptime: " + FormatUtil.formatElapsedSecs(processor.getSystemUptime()));

    long[] prevTicks = processor.getSystemCpuLoadTicks();
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
    long totalCpu = user + nice + sys + idle + iowait + irq + softirq;

    oshi.add(String.format(
        "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%%%n",
        100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu,
        100d * idle / totalCpu, 100d * iowait / totalCpu, 100d * irq / totalCpu,
        100d * softirq / totalCpu));
    oshi.add(String.format("CPU load: %.1f%% (counting ticks)%n",
        processor.getSystemCpuLoadBetweenTicks() * 100));
    oshi.add(String.format("CPU load: %.1f%% (OS MXBean)%n", processor.getSystemCpuLoad() * 100));
    double[] loadAverage = processor.getSystemLoadAverage(3);
    oshi.add("CPU load averages:"
        + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
        + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
        + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
    // per core CPU
    StringBuilder procCpu = new StringBuilder("CPU load per processor:");
    double[] load = processor.getProcessorCpuLoadBetweenTicks();
    for (double avg : load) {
      procCpu.append(String.format(" %.1f%%", avg * 100));
    }
    oshi.add(procCpu.toString());
  }

  /**
   * Prints the processes.
   *
   * @param os the os
   * @param memory the memory
   */
  private static void printProcesses(OperatingSystem os, GlobalMemory memory) {
    oshi.add("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
    // Sort by highest CPU
    List<OSProcess> procs = Arrays.asList(os.getProcesses(5, ProcessSort.CPU));

    oshi.add("   PID  %CPU %MEM       VSZ       RSS Name");
    for (int i = 0; i < procs.size() && i < 5; i++) {
      OSProcess p = procs.get(i);
      oshi.add(String.format(" %5d %5.1f %4.1f %9s %9s %s%n", p.getProcessID(),
          100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
          100d * p.getResidentSetSize() / memory.getTotal(),
          FormatUtil.formatBytes(p.getVirtualSize()),
          FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
    }
  }

  /**
   * Prints the sensors.
   *
   * @param sensors the sensors
   */
  private static void printSensors(Sensors sensors) {
    oshi.add("Sensors:");
    oshi.add(String.format(" CPU Temperature: %.1f°C%n", sensors.getCpuTemperature()));
    oshi.add(" Fan Speeds: " + Arrays.toString(sensors.getFanSpeeds()));
    oshi.add(String.format(" CPU Voltage: %.1fV%n", sensors.getCpuVoltage()));
  }

  /**
   * Prints the power sources.
   *
   * @param powerSources the power sources
   */
  private static void printPowerSources(PowerSource[] powerSources) {
    StringBuilder sb = new StringBuilder("Power: ");
    if (powerSources.length == 0) {
      sb.append("Unknown");
    } else {
      double timeRemaining = powerSources[0].getTimeRemaining();
      if (timeRemaining < -1d) {
        sb.append("Charging");
      } else if (timeRemaining < 0d) {
        sb.append("Calculating time remaining");
      } else {
        sb.append(String.format("%d:%02d remaining", (int) (timeRemaining / 3600),
            (int) (timeRemaining / 60) % 60));
      }
    }
    for (PowerSource powerSource : powerSources) {
      sb.append(String.format("%n %s @ %.1f%%", powerSource.getName(),
          powerSource.getRemainingCapacity() * 100d));
    }
    oshi.add(sb.toString());
  }

  /**
   * Prints the disks.
   *
   * @param diskStores the disk stores
   */
  private static void printDisks(HWDiskStore[] diskStores) {
    oshi.add("Disks:");
    for (HWDiskStore disk : diskStores) {
      boolean readwrite = disk.getReads() > 0 || disk.getWrites() > 0;
      oshi.add(String.format(
          " %s: (model: %s - S/N: %s) size: %s, reads: %s (%s), writes: %s (%s), xfer: %s ms%n",
          disk.getName(), disk.getModel(), disk.getSerial(),
          disk.getSize() > 0 ? FormatUtil.formatBytesDecimal(disk.getSize()) : "?",
          readwrite ? disk.getReads() : "?",
          readwrite ? FormatUtil.formatBytes(disk.getReadBytes()) : "?",
          readwrite ? disk.getWrites() : "?",
          readwrite ? FormatUtil.formatBytes(disk.getWriteBytes()) : "?",
          readwrite ? disk.getTransferTime() : "?"));
      HWPartition[] partitions = disk.getPartitions();
      if (partitions == null) {
        // TODO Remove when all OS's implemented
        continue;
      }
      for (HWPartition part : partitions) {
        oshi.add(String.format(" |-- %s: %s (%s) Maj:Min=%d:%d, size: %s%s%n",
            part.getIdentification(), part.getName(), part.getType(), part.getMajor(),
            part.getMinor(), FormatUtil.formatBytesDecimal(part.getSize()),
            part.getMountPoint().isEmpty() ? "" : " @ " + part.getMountPoint()));
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

    oshi.add(String.format(" File Descriptors: %d/%d%n", fileSystem.getOpenFileDescriptors(),
        fileSystem.getMaxFileDescriptors()));

    OSFileStore[] fsArray = fileSystem.getFileStores();
    for (OSFileStore fs : fsArray) {
      long usable = fs.getUsableSpace();
      long total = fs.getTotalSpace();
      oshi.add(String.format(" %s (%s) [%s] %s of %s free (%.1f%%) is %s and is mounted at %s%n",
          fs.getName(), fs.getDescription().isEmpty() ? "file system" : fs.getDescription(),
          fs.getType(), FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()),
          100d * usable / total, fs.getVolume(), fs.getMount()));
    }
  }

  /**
   * Prints the network interfaces.
   *
   * @param networkIFs the network I fs
   */
  private static void printNetworkInterfaces(NetworkIF[] networkIFs) {
    oshi.add("Network interfaces:");
    for (NetworkIF net : networkIFs) {
      oshi.add(String.format(" Name: %s (%s)%n", net.getName(), net.getDisplayName()));
      oshi.add(String.format("   MAC Address: %s %n", net.getMacaddr()));
      oshi.add(String.format("   MTU: %s, Speed: %s %n", net.getMTU(),
          FormatUtil.formatValue(net.getSpeed(), "bps")));
      oshi.add(String.format("   IPv4: %s %n", Arrays.toString(net.getIPv4addr())));
      oshi.add(String.format("   IPv6: %s %n", Arrays.toString(net.getIPv6addr())));
      boolean hasData = net.getBytesRecv() > 0 || net.getBytesSent() > 0 || net.getPacketsRecv() > 0
          || net.getPacketsSent() > 0;
      oshi.add(String.format("   Traffic: received %s/%s%s; transmitted %s/%s%s %n",
          hasData ? net.getPacketsRecv() + " packets" : "?",
          hasData ? FormatUtil.formatBytes(net.getBytesRecv()) : "?",
          hasData ? " (" + net.getInErrors() + " err)" : "",
          hasData ? net.getPacketsSent() + " packets" : "?",
          hasData ? FormatUtil.formatBytes(net.getBytesSent()) : "?",
          hasData ? " (" + net.getOutErrors() + " err)" : ""));
    }
  }

  /**
   * Prints the network parameters.
   *
   * @param networkParams the network params
   */
  private static void printNetworkParameters(NetworkParams networkParams) {
    oshi.add("Network parameters:");
    oshi.add(String.format(" Host name: %s%n", networkParams.getHostName()));
    oshi.add(String.format(" Domain name: %s%n", networkParams.getDomainName()));
    oshi.add(String.format(" DNS servers: %s%n", Arrays.toString(networkParams.getDnsServers())));
    oshi.add(String.format(" IPv4 Gateway: %s%n", networkParams.getIpv4DefaultGateway()));
    oshi.add(String.format(" IPv6 Gateway: %s%n", networkParams.getIpv6DefaultGateway()));
  }

  /**
   * Prints the displays.
   *
   * @param displays the displays
   */
  private static void printDisplays(Display[] displays) {
    oshi.add("Displays:");
    int i = 0;
    for (Display display : displays) {
      oshi.add(" Display " + i + ":");
      oshi.add(String.valueOf(display));
      i++;
    }
  }

  /**
   * Prints the usb devices.
   *
   * @param usbDevices the usb devices
   */
  private static void printUsbDevices(UsbDevice[] usbDevices) {
    oshi.add("USB Devices:");
    for (UsbDevice usbDevice : usbDevices) {
      oshi.add(String.valueOf(usbDevice));
    }
  }

}
