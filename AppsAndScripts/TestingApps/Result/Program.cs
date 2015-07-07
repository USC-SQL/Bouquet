// ----------------------------------------------------
// This C# program reads and processes each sample in a 
// PT4 file sequentially, from the first sample to the
// last.  
//
// The name of the file to be processed is taken from 
// the command line argument. The path is relative to the executable location.
//
//
//  NOTE: This is simply used for debugging/testing/utility and is not used
//  by any of the actual PowerTool application code
//
//  Sample input files can be found in the directory 'samples'
//  (note, filepath while debuging id relative to the bin\Debug directory)
//
// Copyright (c) 2008 Monsoon Solutions, Inc.
// All Rights Reserved.
// ----------------------------------------------------

using System;
using System.IO;
using System.Diagnostics;

namespace Pt4Reader
{
    #region PT4 class
    public static class PT4
    {
        #region Constants
        // Fixed file offsets
        public const long headerOffset = 0;
        public const long statusOffset = 272;
        public const long sampleOffset = 1024;

        // Bitmasks
        private const short coarseMask = 1;
        private const ushort marker0Mask = 1;
        private const ushort marker1Mask = 2;
        private const ushort markerMask = marker0Mask | marker1Mask;

        // Missing data indicators
        private const short missingRawCurrent = unchecked((short)0x8001);
        private const ushort missingRawVoltage = unchecked((ushort)(~0));
        #endregion

        #region Structs and enums for file header
        public enum CalibrationStatus : int
        {
            OK, Failed
        }

        public enum VoutSetting : int
        {
            Typical, Low, High, Custom
        }

        [FlagsAttribute]  // bitwise-maskable
        public enum SelectField : int
        {
            None = 0x00,
            Avg = 0x01,
            Min = 0x02,
            Max = 0x04,
            Main = 0x08,
            Usb = 0x10,
            Aux = 0x20,
            Marker = 0x40,
            All = Avg | Min | Max
        }

        public enum RunMode : int
        {
            NoGUI, GUI
        }

        [FlagsAttribute] // bitwise-maskable
        public enum CaptureMask : ushort
        {
            chanMain = 0x1000,
            chanUsb = 0x2000,
            chanAux = 0x4000,
            chanMarker = 0x8000,
            chanMask = 0xf000,
        }

        // File header structure
        public struct Pt4Header
        {
            public int headSize;
            public string name;
            public int batterySize;
            public DateTime captureDate;
            public long Unixtime;
            public string serialNumber;
            public CalibrationStatus calibrationStatus;
            public VoutSetting voutSetting;
            public float voutValue;
            public int hardwareRate;
            public float softwareRate; // ignore
            public SelectField powerField;
            public SelectField currentField;
            public SelectField voltageField;
            public string captureSetting;
            public string swVersion;
            public RunMode runMode;
            public int exitCode;
            public long totalCount;
            public ushort statusOffset;
            public ushort statusSize;
            public ushort sampleOffset;
            public ushort sampleSize;
            public ushort initialMainVoltage;
            public ushort initialUsbVoltage;
            public ushort initialAuxVoltage;
            public CaptureMask captureDataMask;
            public ulong sampleCount;
            public ulong missingCount;
            public float avgMainVoltage;
            public float avgMainCurrent;
            public float avgMainPower;
            public float avgUsbVoltage;
            public float avgUsbCurrent;
            public float avgUsbPower;
            public float avgAuxVoltage;
            public float avgAuxCurrent;
            public float avgAuxPower;
		
            /// <summary>
            /// ToString display method
            /// </summary>
            /// <returns></returns>
            public override string ToString()
            {

                string str = "Pt4Header:\n" ;
                str += "headsize: " + headSize +"\n";
                str += "name: " + name+"\n";
                str += "batterySize: "+ batterySize +"\n";
                str += "captureDate: " + captureDate+"\n";
                str += "Unixtime: " + Unixtime+"\n";
                str += "serialNumber: " +serialNumber+ "\n";
                str += "calibrationStatus : " +calibrationStatus +"\n";
                str += "voutSetting: " + voutSetting + "\n";
                str += "voutValue: " + voutValue + "\n";
                str += "hardwareRate: " + hardwareRate + "\n";
                str += "powerField: " + powerField + "\n";
                str += "currentField: " + currentField + "\n";
                str += "voltageField: " + voltageField + "\n";
                str += "captureSetting: " + captureSetting + "\n";
                str += "swVersion: " + swVersion + "\n";
                str += "runMode: " + runMode + "\n";
                str += "exitCode: " + exitCode + "\n";
                str += "totalCount: " + totalCount + "\n";
                str += "statusOffset: " + statusOffset + "\n";
                str += "statusSize: " + statusSize + "\n";
                str += "initialMainVoltage: " + initialMainVoltage + "\n";
                str += "initialUsbVoltage: " + initialUsbVoltage + "\n";
                str += "initialAuxVoltage: " + initialAuxVoltage + "\n";
                str += "captureDataMask: 0x" + ((int)captureDataMask).ToString("x4") + "\n";
                str += "sampleCount: " + sampleCount + "\n";
                str += "missingCount: " + missingCount + "\n";
                str += "avgMainVoltage: " + avgMainVoltage + "\n";
                str += "avgMainCurrent: " + avgMainCurrent + "\n";
                str += "avgMainPower: " + avgMainPower + "\n";
                str += "avgUsbVoltage: " + avgUsbVoltage + "\n";
                str += "avgUsbCurrent: " + avgUsbCurrent + "\n";
                str += "avgUsbPower: " + avgUsbPower + "\n";
                str += "avgAuxVoltage: " + avgAuxVoltage + "\n";
                str += "avgAuxCurrent: " + avgAuxCurrent + "\n";
                str += "avgAuxPower: " + avgAuxPower + "\n";
                return str;
            }
        }
        #endregion

        #region Structs and enums for Status packet
        public enum PacketType : byte
        {
            set = 1, start, stop,
            status = 0x10, sample = 0x20
        }

        public struct Observation
        {
            public short mainCurrent;
            public short usbCurrent;
            public short auxCurrent;
            public ushort voltage;
        }

        [FlagsAttribute]
        public enum PmStatus : byte
        {
            unitNotAtVoltage = 0x01,
            cannotPowerOutput = 0x02,
            checksumError = 0x04,
            followsLastSamplePacket = 0x08,
            responseToStopPacket = 0x10,
            responseToResetCommand = 0x20,
            badPacketReceived = 0x40,
        }

        [FlagsAttribute]
        public enum Leds : byte
        {
            disableButtonPressed = 0x01,
            errorLedOn = 0x02,
            fanMotorOn = 0x04,
            voltageIsAux = 0x08,
        }

        public enum HardwareRev : byte
        {
            revA = 1, revB, revC, revD,
        }

        public enum UsbPassthroughMode : byte
        {
            off, on, auto, trigger, sync,
        }

        public enum EventCode : byte
        {
            noEvent = 0,
            usbConnectionLost,
            tooManyDroppedObservations,
            resetRequestedByHost,
        }

        // Status packet structure
        public struct StatusPacket
        {
            public byte packetLength;
            public PacketType packetType;
            public byte firmwareVersion;
            public byte protocolVersion;
            public Observation fineObs;
            public Observation coarseObs;
            public byte outputVoltageSetting;
            public sbyte temperature;
            public PmStatus pmStatus;
            public byte reserved;
            public Leds leds;
            public sbyte mainFineResistorOffset;
            public ushort serialNumber;
            public byte sampleRate;
            public ushort dacCalLow;
            public ushort dacCalHigh;
            public ushort powerupCurrentLimit;
            public ushort runtimeCurrentLimit;
            public byte powerupTime;
            public sbyte usbFineResistorOffset;
            public sbyte auxFineResistorOffset;
            public ushort initialUsbVoltage;
            public ushort initialAuxVoltage;
            public HardwareRev hardwareRevision;
            public byte temperatureLimit;
            public UsbPassthroughMode usbPassthroughMode;
            public sbyte mainCoarseResistorOffset;
            public sbyte usbCoarseResistorOffset;
            public sbyte auxCoarseResistorOffset;
            public sbyte factoryMainFineResistorOffset;
            public sbyte factoryUsbFineResistorOffset;
            public sbyte factoryAuxFineResistorOffset;
            public sbyte factoryMainCoarseResistorOffset;
            public sbyte factoryUsbCoarseResistorOffset;
            public sbyte factoryAuxCoarseResistorOffset;
            public EventCode eventCode;
            public ushort eventData;
            public byte checksum;
        }
        #endregion
        
        #region Sample struct
        public struct Sample
        {
            public long sampleIndex;   // 0...N-1
            public double timeStamp;   // fractional seconds

            public bool mainPresent;   // whether Main was recorded
            public double mainCurrent; // current in milliamps
            public double mainVoltage; // volts

            public bool usbPresent;    // whether Usb was recorded
            public double usbCurrent;  // current in milliamps
            public double usbVoltage;  // volts

            public bool auxPresent;    // whether Aux was recorded
            public double auxCurrent;  // current in milliamps
            public double auxVoltage;  // volts;

            public bool markerPresent; // whether markers/voltages 
                                       //      were recorded
            public bool marker0;       // Marker 0
            public bool marker1;       // Marker 1
            public bool missing;       // true if this sample was
                                       //      missing
        }
        #endregion
        
        #region Read file header
        static public void ReadHeader(BinaryReader reader,
                                      ref Pt4Header header)
        {
            // Remember original position
            long oldPos = reader.BaseStream.Position;

            // Move to start of file
            reader.BaseStream.Position = 0;

            // Read file header
            header.headSize = reader.ReadInt32();
            header.name = reader.ReadString().Trim();
            header.batterySize = reader.ReadInt32();
            header.captureDate =
                   DateTime.FromBinary(reader.ReadInt64());
	    header.Unixtime=ToUnixTime(header.captureDate);
            header.serialNumber = reader.ReadString().Trim();
            header.calibrationStatus =
                   (CalibrationStatus)reader.ReadInt32();
            header.voutSetting = (VoutSetting)reader.ReadInt32();
            header.voutValue = reader.ReadSingle();
            header.hardwareRate = reader.ReadInt32();
            header.softwareRate = (float)header.hardwareRate;
            reader.ReadSingle(); // ignore software rate
            header.powerField = (SelectField)reader.ReadInt32();
            header.currentField = (SelectField)reader.ReadInt32();
            header.voltageField = (SelectField)reader.ReadInt32();
            header.captureSetting = reader.ReadString().Trim();
            header.swVersion = reader.ReadString().Trim();
            header.runMode = (RunMode)reader.ReadInt32();
            header.exitCode = reader.ReadInt32();
            header.totalCount = reader.ReadInt64();
            header.statusOffset = reader.ReadUInt16();
            header.statusSize = reader.ReadUInt16();
            header.sampleOffset = reader.ReadUInt16();
            header.sampleSize = reader.ReadUInt16();
            header.initialMainVoltage = reader.ReadUInt16();
            header.initialUsbVoltage = reader.ReadUInt16();
            header.initialAuxVoltage = reader.ReadUInt16();
            header.captureDataMask = (CaptureMask)reader.ReadUInt16();
            header.sampleCount = reader.ReadUInt64();
            header.missingCount = reader.ReadUInt64();

            // Convert sums to averages
            ulong count = Math.Max(1, header.sampleCount -
                                      header.missingCount);
            header.avgMainVoltage = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgMainCurrent = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgMainPower = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgUsbVoltage = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgUsbCurrent = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgUsbPower = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgAuxVoltage = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgAuxCurrent = count > 0 ? reader.ReadSingle() / count : 0;
            header.avgAuxPower = count > 0 ? reader.ReadSingle() / count : 0;

            // Restore original position
            reader.BaseStream.Position = oldPos;
        }
        #endregion

        #region Read status packet
        static public void ReadStatusPacket(BinaryReader reader, 
                                            ref StatusPacket status)
        {
            // Remember original position
            long oldPos = reader.BaseStream.Position;

            // Move to start of status packet
            reader.BaseStream.Position = statusOffset;

            // Read status packet
            status.packetLength = reader.ReadByte();
            status.packetType = (PacketType)reader.ReadByte();
            Debug.Assert(status.packetType == PacketType.status);
            status.firmwareVersion = reader.ReadByte();
            status.protocolVersion = reader.ReadByte();
            Debug.Assert(status.protocolVersion >= 16);
            status.fineObs.mainCurrent = reader.ReadInt16();
            status.fineObs.usbCurrent = reader.ReadInt16();
            status.fineObs.auxCurrent = reader.ReadInt16();
            status.fineObs.voltage = reader.ReadUInt16();
            status.coarseObs.mainCurrent = reader.ReadInt16();
            status.coarseObs.usbCurrent = reader.ReadInt16();
            status.coarseObs.auxCurrent = reader.ReadInt16();
            status.coarseObs.voltage = reader.ReadUInt16();
            status.outputVoltageSetting = reader.ReadByte();
            status.temperature = reader.ReadSByte();
            status.pmStatus = (PmStatus)reader.ReadByte();
            status.reserved = reader.ReadByte();
            status.leds = (Leds)reader.ReadByte();
            status.mainFineResistorOffset = reader.ReadSByte();
            status.serialNumber = reader.ReadUInt16();
            status.sampleRate = reader.ReadByte();
            status.dacCalLow = reader.ReadUInt16();
            status.dacCalHigh = reader.ReadUInt16();
            status.powerupCurrentLimit = reader.ReadUInt16();
            status.runtimeCurrentLimit = reader.ReadUInt16();
            status.powerupTime = reader.ReadByte();
            status.usbFineResistorOffset = reader.ReadSByte();
            status.auxFineResistorOffset = reader.ReadSByte();
            status.initialUsbVoltage = reader.ReadUInt16();
            status.initialAuxVoltage = reader.ReadUInt16();
            status.hardwareRevision = (HardwareRev)reader.ReadByte();
            status.temperatureLimit = reader.ReadByte();
            status.usbPassthroughMode =
                       (UsbPassthroughMode)reader.ReadByte();
            status.mainCoarseResistorOffset = reader.ReadSByte();
            status.usbCoarseResistorOffset = reader.ReadSByte();
            status.auxCoarseResistorOffset = reader.ReadSByte();
            status.factoryMainFineResistorOffset = reader.ReadSByte();
            status.factoryUsbFineResistorOffset = reader.ReadSByte();
            status.factoryAuxFineResistorOffset = reader.ReadSByte();
            status.factoryMainCoarseResistorOffset =
                       reader.ReadSByte();
            status.factoryUsbCoarseResistorOffset =
                       reader.ReadSByte();
            status.factoryAuxCoarseResistorOffset =
                       reader.ReadSByte();
            status.eventCode = (EventCode)reader.ReadByte();
            status.eventData = reader.ReadUInt16();
            status.checksum = reader.ReadByte();

            // Restore original position
            reader.BaseStream.Position = oldPos;
        }
        #endregion

        #region Utility functions 
	 public static long ToUnixTime(this DateTime date)
    	{
    	    var epoch = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
    	    return Convert.ToInt64((date.ToUniversalTime() - epoch).TotalMilliseconds);
    	}
        static public long BytesPerSample(CaptureMask captureDataMask)
        {
            long result = sizeof(ushort); // voltage is always present

            // Add lengths for optional current channels
            if ((captureDataMask & CaptureMask.chanMain) != 0)
                result += sizeof(short);

            if ((captureDataMask & CaptureMask.chanUsb) != 0)
                result += sizeof(short);

            if ((captureDataMask & CaptureMask.chanAux) != 0)
                result += sizeof(short);

            return result;
        }

        static public long SamplePosition(long sampleIndex,
                                          CaptureMask captureDataMask)
        {
            long result = sampleOffset +
                BytesPerSample(captureDataMask) * sampleIndex;

            return result;
        }

        static public long SamplePosition(double seconds,
                                   CaptureMask captureDataMask,
                                   ref StatusPacket statusPacket)
        {
            seconds = Math.Max(0, seconds);

            long bytesPerSample = BytesPerSample(captureDataMask);
            long freq = 1000 * statusPacket.sampleRate;

            long result = (long)(seconds * freq * bytesPerSample);
            long err = result % bytesPerSample;
            if (err > 0)   // must fall on boundary
                result += (bytesPerSample - err);
            result += sampleOffset;

            return result;
        }

        static public long SampleCount(BinaryReader reader,
                                       CaptureMask captureDataMask)
        {
            return (reader.BaseStream.Length - sampleOffset)
                   / BytesPerSample(captureDataMask);
        }
        #endregion


        #region Retrieve a sample from the file
        static public void GetSample(long sampleIndex,
                              CaptureMask captureDataMask,
                              StatusPacket statusPacket,
                              BinaryReader reader,
                              ref Sample sample)
        {
            // Remember the index and time
            sample.sampleIndex = sampleIndex;
            sample.timeStamp = (double)sampleIndex 
                               / (double)(statusPacket.sampleRate);

            // Intial settings for all flags
            sample.mainPresent = 
                  (captureDataMask & CaptureMask.chanMain) != 0;
            sample.usbPresent = 
                  (captureDataMask & CaptureMask.chanUsb) != 0;
            sample.auxPresent = 
                  (captureDataMask & CaptureMask.chanAux) != 0;
            sample.markerPresent = true;
            sample.missing = false;

            // Abort if no data was selected
            long bytesPerSample = BytesPerSample(captureDataMask);
            if (bytesPerSample == 0)
                return;

            // Remember original position
            long oldPos = reader.BaseStream.Position;

            // Position the file to the start of the desired sample (if necessary)
            long newPos = SamplePosition(sampleIndex, captureDataMask);
            if (oldPos != newPos)
                reader.BaseStream.Position = newPos;

            // Get default voltages (V) for the three channels
            sample.mainVoltage =
                2.0 + statusPacket.outputVoltageSetting * 0.01;

            sample.usbVoltage =
                (double)statusPacket.initialUsbVoltage * 125 / 1e6f;
            if (statusPacket.hardwareRevision < HardwareRev.revB)
                sample.usbVoltage /= 2;

            sample.auxVoltage =
                (double)statusPacket.initialAuxVoltage * 125 / 1e6f;
            if (statusPacket.hardwareRevision < HardwareRev.revC)
                sample.auxVoltage /= 2;

            // Main current (mA)
            if (sample.mainPresent)
            {
                short raw = reader.ReadInt16();

                sample.missing = sample.missing || 
                                 raw == missingRawCurrent;
                if (!sample.missing)
                {
                    bool coarse = (raw & coarseMask) != 0;
                    raw &= ~coarseMask;
                    sample.mainCurrent = raw;   // uA -> mA
                    if (coarse)
                        sample.mainCurrent *= 250;
                }
            }

            // USB current (mA)
            if (sample.usbPresent)
            {
                short raw = reader.ReadInt16();

                sample.missing = sample.missing || 
                                 raw == missingRawCurrent;
                if (!sample.missing)
                {
                    bool coarse = (raw & coarseMask) != 0;
                    raw &= ~coarseMask;
                    sample.usbCurrent = raw;   
                    if (coarse)
                        sample.usbCurrent *= 250;
                }
            }

            // Aux current (mA)
            if (sample.auxPresent)
            {
                short raw = reader.ReadInt16();

                sample.missing = sample.missing || 
                                 raw == missingRawCurrent;
                if (!sample.missing)
                {
                    bool coarse = (raw & coarseMask) != 0;
                    raw &= ~coarseMask;
                    sample.auxCurrent = raw ;  
                    if (coarse)
                        sample.auxCurrent *= 250;
                }
            }

            // Markers and Voltage (V)
            {
                ushort uraw = reader.ReadUInt16();

                sample.missing = sample.missing ||                    
                                 uraw == missingRawVoltage;
                if (!sample.missing)
                {
                    // Strip out marker bits
                    sample.marker0 = (uraw & marker0Mask) != 0;
                    sample.marker1 = (uraw & marker1Mask) != 0;
                    uraw &= unchecked((ushort)~markerMask);

                    // Calculate voltage
                    double voltage = (double)uraw * 125 / 1e6f;

                    // Assign the high-res voltage, as appropriate
                    if ((statusPacket.leds & Leds.voltageIsAux) != 0)
                    {
                        sample.auxVoltage = voltage;
                        if (statusPacket.hardwareRevision 
                               < HardwareRev.revC)
                        {
                            sample.auxVoltage /= 2;
                        }
                    }
                    else
                    {
                        sample.mainVoltage = voltage;
                        if (statusPacket.hardwareRevision 
                               < HardwareRev.revB)
                        {
                            sample.mainVoltage /= 2;
                        }
                    }
                }
            }

            // Restore original position, if we moved it earlier
            if (oldPos != newPos)
                reader.BaseStream.Position = oldPos;
        }
        #endregion
    }
    #endregion

    #region Main program
    class Program
    {
        static void Main(string[] args)
        {
            // Open the file named in the command line argument
            string fileName = args[0];
            if (!File.Exists(fileName))
                return;


            Console.WriteLine("reading in file: "+fileName);
            FileStream pt4Stream = File.Open(fileName,
                                              FileMode.Open,
                                              FileAccess.Read,
                                              FileShare.ReadWrite);
            BinaryReader pt4Reader = new BinaryReader(pt4Stream);

            // Read the file header
            PT4.Pt4Header header = new PT4.Pt4Header();
            PT4.ReadHeader(pt4Reader, ref header);

            Console.WriteLine("header : "+header.ToString());


            //if this is a pt3 file, jump ship here. the rest of the format is different
            //technically the header format is a bit different too.. but we can get enough
            //out of it for testing purposes.
            if (fileName.EndsWith(".pt3"))
            {
                Console.WriteLine("Exiting, PT3 file found.  Only PT4 format files are fully supported.");
                //cleanup
                pt4Reader.Close();

                //so we can see output before console vanishes
                Console.WriteLine("Please press Enter to exit...");
                Console.ReadLine();

                return;
            }


            // Read the Status Packet
            PT4.StatusPacket statusPacket = new PT4.StatusPacket();
            PT4.ReadStatusPacket(pt4Reader, ref statusPacket);


            // Determine the number of samples in the file
            long sampleCount = PT4.SampleCount(pt4Reader,
                                               header.captureDataMask);

            Console.WriteLine("Sample count :"+sampleCount);

            // Pre-position input file to the beginning of the sample 
            // data (saves a lot of repositioning in the GetSample 
            // routine)
            pt4Reader.BaseStream.Position = PT4.sampleOffset;


        long missingCount = 0;

            // Process the samples sequentially, beginning to end
            PT4.Sample sample = new PT4.Sample();
            for (long sampleIndex = 0;
                 sampleIndex < sampleCount;
                 sampleIndex++)
            {
                // Read the next sample
                PT4.GetSample(sampleIndex, header.captureDataMask,
                              statusPacket, pt4Reader, ref sample);

                // Process the sample
                Console.WriteLine("#{0} {1} millisec {2} uA {3} V",
                                  sampleIndex,
                                  sample.timeStamp,
                                  sample.mainCurrent,
                                  sample.mainVoltage);

                if (sample.missing)
                    missingCount++;
            }

            // Close input file

            pt4Reader.Close();



        }
    }
    #endregion
}

